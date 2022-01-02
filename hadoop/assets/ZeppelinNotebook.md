```hive
--Load data from google storage into an external hive table

DROP TABLE IF EXISTS wdi_gs;

CREATE EXTERNAL TABLE wdi_gs(year INT, country_name STRING, country_code STRING, indicator_name STRING, indicator_code STRING, indicator_value FLOAT)
    ROW FORMAT DELIMITED
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    LOCATION 'gs://jarvis_data_eng_tomas/datasets/wdi_2016'
    TBLPROPERTIES ("skip.header.line.count"="1");
```
Query executed successfully. Affected rows : -1


```hive
--Load GS data from external table into HDFS

DROP TABLE IF EXISTS wdi_csv_text;

--Create new table
CREATE EXTERNAL TABLE wdi_csv_text(year INT, country_name STRING, country_code STRING, indicator_name STRING, indicator_code STRING, indicator_value FLOAT)
    ROW FORMAT DELIMITED
    FIELDS TERMINATED BY ','
    LINES TERMINATED BY '\n'
    LOCATION 'hdfs:///user/tomatoes907/hive/wdi/wdi_csv_text';
    
--Insert data into new table
INSERT OVERWRITE TABLE wdi_csv_text
SELECT * FROM wdi_gs;
```
Query executed successfully. Affected rows : -1


```hive
--Run count query twice to see time difference between first and second execution

SELECT count(*) AS first FROM wdi_csv_text; --36 seconds total, 19 seconds computation
SELECT count(*) AS second FROM wdi_csv_text; --7 seconds total, 5 seconds computaion

--The second query runs faster than the first because relevant data is cached during the first,
--hence speeding up the read times for the second identical query.
```
INFO  : Compiling command(queryId=hive_20211223013836_58815e92-d5aa-4d73-88d1-bf1aa351ea60): 

```hive
SELECT count(*) AS first FROM wdi_csv_text
```
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:first, type:bigint, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20211223013836_58815e92-d5aa-4d73-88d1-bf1aa351ea60); Time taken: 0.167 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20211223013836_58815e92-d5aa-4d73-88d1-bf1aa351ea60): 

```hive
SELECT count(*) AS first FROM wdi_csv_text
```
INFO  : Query ID = hive_20211223013836_58815e92-d5aa-4d73-88d1-bf1aa351ea60
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20211223013836_58815e92-d5aa-4d73-88d1-bf1aa351ea60
INFO  : Tez session hasn't been created yet. Opening session
INFO  : Dag name: SELECT count(*) AS first FROM wdi_csv_text (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1640191836849_0057)


```sh
#copy files from hdfs to master's local filesystem
hdfs dfs -get -f hdfs:///user/tomatoes907/hive/wdi/wdi_csv_text ~

#bash row count
sum=0
for file in ~/wdi_csv_text/*
do
    lines=`wc -l "$file"`;
    lines=`echo "$lines" | grep '^[0-9]*' -o`;
    sum=$((sum+lines));
done

echo "Number of rows = $sum";

#The bash approach surprisingly came out faster than the Hive approach in first time execution (i.e., no caching).
#The Hive query took a total of 36 seconds, of which 19 seconds were actually spent on the computation itself while 
#the bash approach running only on the master node took around 18 seconds without caching. This may simply be because 
#the file is not big enough for the performance of parallelization to outweigh the overhead associated with managing 
#the distributed system.

#However, with data loaded into the cache, the Hive approach outperforms the bash approach. Hive takes around 4 seconds, 
#while bash takes around 11 seconds.
```
Number of rows = 21759408

```hive
--Parsing issue

SELECT distinct(indicator_code)
FROM wdi_csv_text
ORDER BY indicator_code
LIMIT 20;

```
INFO  : Compiling command(queryId=hive_20211223013924_47289cbc-9aa6-41b5-ab28-306f8ea1ad98): 

```hive
SELECT distinct(indicator_code)
FROM wdi_csv_text
ORDER BY indicator_code
LIMIT 20
```
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:indicator_code, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20211223013924_47289cbc-9aa6-41b5-ab28-306f8ea1ad98); Time taken: 0.185 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20211223013924_47289cbc-9aa6-41b5-ab28-306f8ea1ad98): 

```hive
SELECT distinct(indicator_code)
FROM wdi_csv_text
ORDER BY indicator_code
LIMIT 20
```
INFO  : Query ID = hive_20211223013924_47289cbc-9aa6-41b5-ab28-306f8ea1ad98
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20211223013924_47289cbc-9aa6-41b5-ab28-306f8ea1ad98
INFO  : Tez session hasn't been created yet. Opening session
INFO  : Dag name: SELECT distinct(indicator_code)
FROM wd...20 (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1640191836849_0058)


```hive
--Debug table

DROP TABLE IF EXISTS wdi_gs_debug;

CREATE EXTERNAL TABLE wdi_gs_debug(line STRING)
    ROW FORMAT DELIMITED
    LINES TERMINATED BY '\n'
    LOCATION 'gs://jarvis_data_eng_tomas/datasets/wdi_2016'
    TBLPROPERTIES ("skip.header.line.count"="1");
    

SELECT *
FROM wdi_gs_debug
WHERE line like "%\(\% of urban population\)\"%"
```
Query executed successfully. Affected rows : -1


```hive
--Create a new Hive table with OpenCSVSerDe to fix the problem

DROP TABLE IF EXISTS wdi_opencsv_gs;

CREATE EXTERNAL TABLE wdi_opencsv_gs(year INT, country_name STRING, country_code STRING, indicator_name STRING, indicator_code STRING, indicator_value FLOAT)
    ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
    LOCATION 'gs://jarvis_data_eng_tomas/datasets/wdi_2016'
    TBLPROPERTIES ("skip.header.line.count"="1");
    
DROP TABLE IF EXISTS wdi_opencsv_text;

--Create new table
CREATE EXTERNAL TABLE wdi_opencsv_text(year INT, country_name STRING, country_code STRING, indicator_name STRING, indicator_code STRING, indicator_value FLOAT)
    ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
    LOCATION 'hdfs:///user/tomatoes907/hive/wdi/wdi_opencsv_text';
    
--Insert data into new table
INSERT OVERWRITE TABLE wdi_opencsv_text
SELECT * FROM wdi_opencsv_gs;
```
Query executed successfully. Affected rows : -1


```hive
--Verify parsing in new OpenCSV table

SELECT distinct(indicator_code)
FROM wdi_opencsv_text
ORDER BY indicator_code
LIMIT 20;

--The openCSVSerDe table takes 97 seconds to execute above query
```
INFO  : Compiling command(queryId=hive_20211222184249_7cf53ae9-888a-440c-a04a-57efbb19402e): 

```hive
SELECT distinct(indicator_code)
FROM wdi_opencsv_text
ORDER BY indicator_code
LIMIT 20
```
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:indicator_code, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20211222184249_7cf53ae9-888a-440c-a04a-57efbb19402e); Time taken: 0.328 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20211222184249_7cf53ae9-888a-440c-a04a-57efbb19402e): 

```hive
SELECT distinct(indicator_code)
FROM wdi_opencsv_text
ORDER BY indicator_code
LIMIT 20
```
INFO  : Query ID = hive_20211222184249_7cf53ae9-888a-440c-a04a-57efbb19402e
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20211222184249_7cf53ae9-888a-440c-a04a-57efbb19402e
INFO  : Tez session hasn't been created yet. Opening session
INFO  : Dag name: SELECT distinct(indicator_code)
FROM wd...20 (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1640191836849_0011)


```hive
--OpenCSVSerDe limitation

DROP VIEW IF EXISTS wdi_opencsv_text_view;

CREATE VIEW wdi_opencsv_text_view AS
SELECT CAST(year AS INT) year, country_name, country_code, indicator_name, indicator_code, CAST(indicator_value AS FLOAT) indicator_value
FROM wdi_opencsv_text;

DESCRIBE FORMATTED wdi_opencsv_text_view;
```
Query executed successfully. Affected rows : -1


```hive
SELECT indicator_value AS GDP_growth_value, year, country_name
FROM wdi_opencsv_text_view
WHERE year = 2015 AND country_name = "Canada" AND indicator_name LIKE "%GDP growth%";

--Takes 83 seconds
```
INFO  : Compiling command(queryId=hive_20211222205642_a3006ea3-dc04-4cda-9abd-be674c3f6fc0): 

```hive
SELECT indicator_value AS GDP_growth_value, year, country_name
FROM wdi_opencsv_text_view
WHERE year = 2015 AND country_name = "Canada" AND indicator_name LIKE "%GDP growth%"
```
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:gdp_growth_value, type:float, comment:null), FieldSchema(name:year, type:int, comment:null), FieldSchema(name:country_name, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20211222205642_a3006ea3-dc04-4cda-9abd-be674c3f6fc0); Time taken: 0.213 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20211222205642_a3006ea3-dc04-4cda-9abd-be674c3f6fc0): 

```hive
SELECT indicator_value AS GDP_growth_value, year, country_name
FROM wdi_opencsv_text_view
WHERE year = 2015 AND country_name = "Canada" AND indicator_name LIKE "%GDP growth%"
```
INFO  : Query ID = hive_20211222205642_a3006ea3-dc04-4cda-9abd-be674c3f6fc0
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20211222205642_a3006ea3-dc04-4cda-9abd-be674c3f6fc0
INFO  : Tez session hasn't been created yet. Opening session
INFO  : Dag name: SELECT indicator_value AS GDP_gro...growth%" (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1640191836849_0027)


INFO  : Completed executing command(queryId=hive_20211222205642_a3006ea3-dc04-4cda-9abd-be674c3f6fc0); Time taken: 96.41 seconds
INFO  : OK
INFO  : Concurrency mode is disabled, not creating a lock manager

```hive
--Partitioned table

DROP TABLE IF EXISTS wdi_opencsv_text_partitions;

CREATE EXTERNAL TABLE wdi_opencsv_text_partitions(country_name STRING, country_code STRING, indicator_name STRING, indicator_code STRING, indicator_value FLOAT)
    PARTITIONED BY (year INT)
    LOCATION 'hdfs:///user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions';

SET hive.exec.dynamic.partition.mode=nonstrict;

INSERT OVERWRITE TABLE wdi_opencsv_text_partitions
PARTITION(year)
SELECT country_name, country_code, indicator_name, indicator_code, indicator_value, year
FROM wdi_opencsv_text_view;
```
Query executed successfully. Affected rows : -1


```sh
#Show partitions created

hdfs dfs -ls /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions
```
Found 57 items
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1960
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1961
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1962
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1963
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1964
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1965
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1966
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1967
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1968
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1969
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1970
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1971
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1972
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1973
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1974
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1975
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1976
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1977
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1978
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1979
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1980
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1981
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1982
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1983
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1984
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1985
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1986
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1987
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1988
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1989
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1990
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1991
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1992
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1993
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1994
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1995
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1996
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1997
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1998
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=1999
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2000
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2001
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2002
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2003
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2004
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2005
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2006
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2007
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2008
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2009
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2010
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2011
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2012
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2013
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2014
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2015
drwxr-xr-x   - gpadmin hadoop          0 2021-12-22 20:52 /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions/year=2016

```hive
--Re-run GDP growth query against partitioned table
--Takes only 7 seconds

SELECT indicator_value AS GDP_growth_value, year, country_name
FROM wdi_opencsv_text_partitions
WHERE year = 2015 AND country_name = "Canada" AND indicator_name LIKE "%GDP growth%";

```
INFO  : Compiling command(queryId=hive_20211222205548_e72ab10e-4a33-4dcc-8db6-3dc8f7c6cfdd): 

```hive
SELECT indicator_value AS GDP_growth_value, year, country_name
FROM wdi_opencsv_text_partitions
WHERE year = 2015 AND country_name = "Canada" AND indicator_name LIKE "%GDP growth%"
```
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:gdp_growth_value, type:float, comment:null), FieldSchema(name:year, type:int, comment:null), FieldSchema(name:country_name, type:string, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20211222205548_e72ab10e-4a33-4dcc-8db6-3dc8f7c6cfdd); Time taken: 0.47 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20211222205548_e72ab10e-4a33-4dcc-8db6-3dc8f7c6cfdd): 

```hive
SELECT indicator_value AS GDP_growth_value, year, country_name
FROM wdi_opencsv_text_partitions
WHERE year = 2015 AND country_name = "Canada" AND indicator_name LIKE "%GDP growth%"
```
INFO  : Query ID = hive_20211222205548_e72ab10e-4a33-4dcc-8db6-3dc8f7c6cfdd
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20211222205548_e72ab10e-4a33-4dcc-8db6-3dc8f7c6cfdd
INFO  : Tez session hasn't been created yet. Opening session
INFO  : Dag name: SELECT indicator_value AS GDP_gro...growth%" (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1640191836849_0026)

```hive
--Columnar file format

DROP TABLE IF EXISTS wdi_csv_parquet;

CREATE EXTERNAL TABLE wdi_csv_parquet(year INT, country_name STRING, country_code STRING, indicator_name STRING, indicator_code STRING, indicator_value FLOAT)
STORED AS PARQUET
LOCATION 'hdfs:///user/tomatoes907/hive/wdi/wdi_csv_parquet';

INSERT OVERWRITE TABLE wdi_csv_parquet
SELECT * FROM wdi_opencsv_gs;
```
Query executed successfully. Affected rows : -1


```sh
#Compare disk usage between parquet storage format and opencsv

hdfs dfs -du -h /user/tomatoes907/hive/wdi
```
137.2 M  274.5 M  /user/tomatoes907/hive/wdi/wdi_csv_parquet
1.7 G    3.4 G    /user/tomatoes907/hive/wdi/wdi_csv_text
2.3 G    4.5 G    /user/tomatoes907/hive/wdi/wdi_opencsv_text
1.9 G    3.8 G    /user/tomatoes907/hive/wdi/wdi_opencsv_text_partitions

```hive
--Compare runtime speed between parquet table and openCSV table

--11 seconds
SELECT COUNT(*)
FROM wdi_csv_parquet;

--69 seconds
SELECT COUNT(*)
FROM wdi_opencsv_text;

--16 seconds
SELECT indicator_value AS GDP_growth_value, year, country_name
FROM wdi_csv_parquet
WHERE year = 2015 AND country_name = "Canada" AND indicator_name LIKE "%GDP growth%";

--72 seconds
SELECT indicator_value AS GDP_growth_value, year, country_name
FROM wdi_opencsv_text
WHERE year = 2015 AND country_name = "Canada" AND indicator_name LIKE "%GDP growth%";
```
INFO  : Compiling command(queryId=hive_20211223014230_52c1d521-c0d9-4b37-8bc6-330482c84952): 

```hive
SELECT COUNT(*)
FROM wdi_csv_parquet
```
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:_c0, type:bigint, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20211223014230_52c1d521-c0d9-4b37-8bc6-330482c84952); Time taken: 0.182 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20211223014230_52c1d521-c0d9-4b37-8bc6-330482c84952): 

```hive
SELECT COUNT(*)
FROM wdi_csv_parquet
```
INFO  : Query ID = hive_20211223014230_52c1d521-c0d9-4b37-8bc6-330482c84952
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20211223014230_52c1d521-c0d9-4b37-8bc6-330482c84952
INFO  : Tez session hasn't been created yet. Opening session
INFO  : Dag name: SELECT COUNT(*)
FROM wdi_csv_parquet (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1640191836849_0060)


```hive
--Highest GDP growth year for every country using Hive

--19 seconds
SELECT wdi_csv.indicator_value AS GDP_growth_value, wdi_csv.country_name, MAX(wdi_csv.year)
FROM (
    SELECT MAX(indicator_value) AS maximum, country_name
    FROM wdi_csv_parquet
    WHERE indicator_code='NY.GDP.MKTP.KD.ZG'
    GROUP BY country_name
    ) country_max INNER JOIN wdi_csv_parquet wdi_csv ON 
    wdi_csv.indicator_value >= country_max.maximum
    AND wdi_csv.country_name = country_max.country_name
WHERE wdi_csv.indicator_code='NY.GDP.MKTP.KD.ZG'
GROUP BY wdi_csv.indicator_value, wdi_csv.country_name;
```
INFO  : Compiling command(queryId=hive_20211223005448_a5b33864-9060-439b-8760-da228d022041): 

```
SELECT wdi_csv.indicator_value AS GDP_growth_value, wdi_csv.country_name, MAX(wdi_csv.year)
FROM (
    SELECT MAX(indicator_value) AS maximum, country_name
    FROM wdi_csv_parquet
    WHERE indicator_code='NY.GDP.MKTP.KD.ZG'
    GROUP BY country_name
    ) country_max INNER JOIN wdi_csv_parquet wdi_csv ON 
    wdi_csv.indicator_value >= country_max.maximum
    AND wdi_csv.country_name = country_max.country_name
WHERE wdi_csv.indicator_code='NY.GDP.MKTP.KD.ZG'
GROUP BY wdi_csv.indicator_value, wdi_csv.country_name
```
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:gdp_growth_value, type:float, comment:null), FieldSchema(name:wdi_csv.country_name, type:string, comment:null), FieldSchema(name:_c2, type:int, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20211223005448_a5b33864-9060-439b-8760-da228d022041); Time taken: 0.393 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20211223005448_a5b33864-9060-439b-8760-da228d022041): 

```
SELECT wdi_csv.indicator_value AS GDP_growth_value, wdi_csv.country_name, MAX(wdi_csv.year)
FROM (
    SELECT MAX(indicator_value) AS maximum, country_name
    FROM wdi_csv_parquet
    WHERE indicator_code='NY.GDP.MKTP.KD.ZG'
    GROUP BY country_name
    ) country_max INNER JOIN wdi_csv_parquet wdi_csv ON 
    wdi_csv.indicator_value >= country_max.maximum
    AND wdi_csv.country_name = country_max.country_name
WHERE wdi_csv.indicator_code='NY.GDP.MKTP.KD.ZG'
GROUP BY wdi_csv.indicator_value, wdi_csv.country_name
```
INFO  : Query ID = hive_20211223005448_a5b33864-9060-439b-8760-da228d022041
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20211223005448_a5b33864-9060-439b-8760-da228d022041
INFO  : Tez session hasn't been created yet. Opening session
INFO  : Dag name: SELECT wdi_csv.indica...wdi_csv.country_name (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1640191836849_0041)

```spark.sql
--Highest GDP growth year for every country using spqrk.sql

--5 seconds
SELECT wdi_csv.indicator_value AS GDP_growth_value, wdi_csv.country_name, MAX(wdi_csv.year)
FROM (
    SELECT MAX(indicator_value) AS maximum, country_name
    FROM wdi_csv_parquet
    WHERE indicator_code='NY.GDP.MKTP.KD.ZG'
    GROUP BY country_name
    ) country_max INNER JOIN wdi_csv_parquet wdi_csv ON 
    wdi_csv.indicator_value >= country_max.maximum
    AND wdi_csv.country_name = country_max.country_name
WHERE wdi_csv.indicator_code='NY.GDP.MKTP.KD.ZG'
GROUP BY wdi_csv.indicator_value, wdi_csv.country_name;
```
|GDP_growth_value|country_name|max(year)|
|-|-|-|
|33.62937|Chad|2004|
|14.036278|Paraguay|2013|
|6.8927703|Heavily indebted poor countries (HIPC)|1962|
|6.645997|World|1964|
|8.920504|Senegal|1976|
|6.821457|Sweden|1964|
|19.182642|Cabo Verde|1994|
|13.160728|East Asia & Pacific (IDA & IBRD countries)|1970|
|45.302753|Kiribati|1974|
|9.378625|Least developed countries: UN classification|2007|
|0.0|Sint Maarten (Dutch part)|2016|
|57.81783|Iraq|1990|
|0.0|Northern Mariana Islands|2016|
|5.2550063|Germany|1990|
|21.020649|Afghanistan|2009|
|24.309572|Jordan|1976|
|16.66522|Sudan|1976|
|10.9000435|Palau|1995|
|0.0|Turks and Caicos Islands|2016|
|6.9905305|France|1969|
|8.645224|Middle income|2007|
|10.016272|Late-demographic dividend|1970|
|0.0|British Virgin Islands|2016|
|14.260685|Dominica|1980|
|34.31373|Algeria|1963|
|15.459713|Togo|1965|
|0.0|Not classified|2016|
|6.9566846|Belgium|1964|
|23.171246|Iran, Islamic Rep.|1982|
|9.292035|San Marino|1995|
|13.950682|Ecuador|1973|
|26.40488|Lesotho|1973|
|14.046002|Ghana|2011|
|14.192425|Nicaragua|1974|
|22.562428|Brunei Darussalam|1979|
|6.9591|IDA only|2007|
|9.954231|Benin|1981|
|23.544891|St. Lucia|1990|
|0.0|Curacao|2016|
|11.588021|Sub-Saharan Africa|2004|
|0.0|American Samoa|2016|
|17.505634|Timor-Leste|2000|
|9.115646|Sao Tome and Principe|2006|
|19.105537|Isle of Man|1986|
|6.645474|Croatia|1997|
|21.32567|Burundi|1970|
|12.02392|Andorra|2003|
|11.018513|Suriname|1989|
|0.0|St. Martin (French part)|2016|
|27.690886|Mauritania|1964|
|7.1167274|North America|1984|
|23.5977|Congo, Rep.|1982|
|9.269938|Denmark|1964|
|11.1779995|Ireland|1997|
|10.410924|Liechtenstein|1999|
|12.372876|Morocco|1996|
|9.08357|South Asia|2010|
|8.394322|Lower middle income|2004|
|21.200697|Congo, Dem. Rep.|1962|
|6.473487|Macedonia, FYR|2007|
|11.360282|Guyana|1964|
|21.22141|Eritrea|1994|
|6.6795015|Pacific island small states|1984|
|8.920647|Philippines|1973|
|6.5584636|Tonga|1985|
|6.0001945|Djibouti|2014|
|11.714203|Malaysia|1973|
|15.240377|Singapore|2010|
|11.212815|Turkey|1966|
|10.848075|Comoros|2000|
|13.250086|Cambodia|2005|
|35.22408|Rwanda|1995|
|20.654325|Maldives|2006|
|11.149537|Greece|1961|
|26.973917|Kosovo|2001|
|9.048841|Caribbean small states|1972|
|26.754988|Macao SAR, China|2004|
|12.66971|Argentina|1991|
|26.170246|Qatar|2006|
|9.8546505|Madagascar|1979|
|9.593287|Finland|1969|
|34.600002|New Caledonia|1988|
|13.843995|Myanmar|2003|
|12.308128|Peru|1994|
|26.268576|Sierra Leone|2002|
|9.227175|Latin America & Caribbean (excluding high income)|1973|
|7.259087|United States|1984|
|12.896334|Arab World|1976|
|19.4|China|1970|
|10.259963|India|2010|
|11.449743|Belarus|2004|
|10.583587|St. Kitts and Nevis|1986|
|33.990467|Kuwait|1993|
|19.560095|Malta|1975|
|14.190637|Lao PDR|1989|
|24.542517|Marshall Islands|1986|
|15.50122|Tuvalu|1998|
|30.073698|Somalia|1975|
|12.27793|Chile|1992|
|9.849452|Puerto Rico|2001|
|18.286606|Venezuela, RB|2004|
|5.305203|Cayman Islands|1994|
|6.008331|Europe & Central Asia|1973|
|7.9674854|Bolivia|1972|
|39.487095|Gabon|1974|
|8.207246|Italy|1961|
|6.5564966|OECD members|1964|
|11.086954|Lithuania|2007|
|35.38456|Turkmenistan|1990|
|11.838675|Spain|1961|
|9.481555|Central African Republic|1984|
|10.952788|Bangladesh|1964|
|5.6764426|Barbados|2006|
|10.0|Russian Federation|2000|
|13.288115|Thailand|1988|
|12.393429|Gambia, The|1975|
|21.018|Swaziland|1990|
|28.696266|Bhutan|1987|
|14.582442|Monaco|2007|
|8.618244|Low & middle income|2007|
|13.351832|Fiji|1967|
|16.728817|Malawi|1995|
|9.144579|Sri Lanka|2012|
|149.97296|Equatorial Guinea|1997|
|22.593054|Angola|2007|
|13.501173|Albania|1999|
|21.244144|Pre-demographic dividend|2004|
|13.9|Tajikistan|1988|
|33.735775|Nigeria|2004|
|6.2733355|Norway|1961|
|19.688335|Cuba|1981|
|13.472612|Niger|1978|
|6.100627|European Union|1973|
|9.08357|South Asia (IDA & IBRD)|2010|
|0.0|Nauru|2016|
|12.1|Ukraine|2004|
|7.7826047|Europe & Central Asia (IDA & IBRD countries)|2006|
|13.060838|Iceland|1971|
|5.8983297|Channel Islands|2007|
|13.160728|East Asia & Pacific (excluding high income)|1970|
|0.0|Korea, Dem. People�s Rep.|2016|
|0.0|Gibraltar|2016|
|6.4625196|High income|1964|
|8.809809|Uruguay|1986|
|11.90548|Mexico|1964|
|9.048979|Upper middle income|2007|
|10.657902|Montenegro|2007|
|0.0|Guam|2016|
|9.541044|Guatemala|1963|
|14.627248|Egypt, Arab Rep.|1976|
|14.040795|Armenia|2003|
|10.500693|Honduras|1976|
|22.672523|Saudi Arabia|1973|
|15.916227|Middle East & North Africa (excluding high income)|1976|
|11.523244|Uganda|1995|
|12.269548|Namibia|2004|
|4.330794|Switzerland|1989|
|8.755049|Latin America & the Caribbean (IDA & IBRD countries)|1973|
|6.7345076|Low income|2010|
|8.312726|Early-demographic dividend|1964|
|11.902191|Latvia|2006|
|23.874775|United Arab Emirates|1980|
|16.164154|Hong Kong SAR, China|1976|
|21.151693|Seychelles|1978|
|11.645651|East Asia & Pacific|1969|
|0.0|Faroe Islands|2016|
|7.178966|Samoa|1996|
|13.20003|Kyrgyz Republic|1988|
|6.8765445|Czech Republic|2006|
|15.166518|Belize|1980|
|22.173891|Kenya|1971|
|18.005667|IDA blend|1970|
|38.20071|Lebanon|1991|
|8.207598|Yemen, Rep.|1992|
|13.3764|Antigua and Barbuda|2006|
|8.464381|Tanzania|2007|
|12.882468|Japan|1968|
|26.36194|Botswana|1972|
|8.195589|Europe & Central Asia (excluding high income)|2004|
|26.139296|Bahamas, The|1979|
|5.523738|Haiti|2011|
|12.612603|Portugal|1970|
|22.003004|Cameroon|1978|
|7.1668916|Australia|1970|
|9.8296175|Costa Rica|1965|
|11.014744|Burkina Faso|1996|
|8.46345|Colombia|1978|
|4.9377146|Hungary|2004|
|8.533192|IDA & IBRD total|2007|
|11.353461|Pakistan|1970|
|11.730911|Moldova|1982|
|9.540481|Vietnam|1995|
|10.834435|Slovak Republic|2007|
|6.466701|Post-demographic dividend|1964|
|189.82993|Oman|1965|
|18.001049|French Polynesia|1974|
|20.266293|Cyprus|1976|
|17.613089|Cote d'Ivoire|1964|
|12.344|Georgia|2007|
|22.565151|Zimbabwe|1970|
|6.5672135|Central Europe and the Baltics|2006|
|12.034179|Indonesia|1968|
|17.290777|Mongolia|2011|
|15.916227|Middle East & North Africa (IDA & IBRD countries)|1976|
|14.787517|Korea, Rep.|1973|
|104.486786|Libya|2012|
|34.5|Azerbaijan|2006|
|13.273064|Grenada|2005|
|14.441417|Trinidad and Tobago|2003|
|16.241243|Fragile and conflict affected situations|1990|
|14.667397|Virgin Islands (U.S.)|1972|
|13.85933|Ethiopia|1987|
|18.008554|Jamaica|1972|
|8.1406555|Micronesia, Fed. Sts.|1993|
|7.1167793|Canada|1962|
|26.845322|Mozambique|1996|
|13.978692|Brazil|1973|
|25.947592|West Bank and Gaza|1998|
|25.83149|St. Vincent and the Grenadines|1972|
|18.226683|Dominican Republic|1970|
|15.106996|Middle East & North Africa|1972|
|9.983934|Luxembourg|1986|
|88.957664|Bosnia and Herzegovina|1996|
|11.588021|Sub-Saharan Africa (IDA & IBRD countries)|2004|
|18.202286|Papua New Guinea|1993|
|18.167528|Guinea-Bissau|1981|
|8.458984|Romania|2008|
|10.583844|Other small states|2006|
|12.928475|Solomon Islands|2011|
|9.668261|Small states|2006|
|10.944692|Bulgaria|1988|
|6.275867|Austria|1969|
|9.68113|Nepal|1984|
|7.5433373|El Salvador|1992|
|13.5|Kazakhstan|2001|
|9.046513|Serbia|2004|
|8.89603|South Africa|1965|
|12.870007|Bahrain|1993|
|9.742033|Mauritius|1986|
|25.027607|Syrian Arab Republic|1972|
|6.5399766|United Kingdom|1973|
|15.315924|Panama|2007|
|16.241957|Israel|1968|
|7.8144317|Aruba|1996|
|11.798595|Estonia|1997|
|106.27981|Liberia|1997|
|17.742718|Tunisia|1972|
|16.647455|Zambia|1965|
|13.129731|South Sudan|2013|
|6.308117|Guinea|1988|
|9.921154|Uzbekistan|2007|
|6.941572|Slovenia|2007|
|6.416388|New Zealand|1993|
|13.060854|Greenland|1971|
|7.2016835|Poland|2007|
|6.9535418|Euro area|1969|
|8.692858|IBRD only|2007|
|14.363636|Bermuda|1966|
|11.229955|IDA total|2004|
|8.6221485|Latin America & Caribbean|1973|
|13.833095|Vanuatu|1983|
|8.643095|Netherlands|1965|
|20.286634|Mali|1985|
|11.60034|Sub-Saharan Africa (excluding high income)|2004|
```hive
--GDP Growth for all countries sorted by country and year

SELECT country_name, year, indicator_code, indicator_value
FROM wdi_csv_parquet
WHERE indicator_code='NY.GDP.MKTP.KD.ZG'
ORDER BY country_name, year;

```
INFO  : Compiling command(queryId=hive_20211223013331_e02b188b-a354-4370-bbc4-872fbc0a3eb4): 

```hive
SELECT country_name, year, indicator_code, indicator_value
FROM wdi_csv_parquet
WHERE indicator_code='NY.GDP.MKTP.KD.ZG'
ORDER BY country_name, year
```
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Semantic Analysis Completed (retrial = false)
INFO  : Returning Hive schema: Schema(fieldSchemas:[FieldSchema(name:country_name, type:string, comment:null), FieldSchema(name:year, type:int, comment:null), FieldSchema(name:indicator_code, type:string, comment:null), FieldSchema(name:indicator_value, type:float, comment:null)], properties:null)
INFO  : Completed compiling command(queryId=hive_20211223013331_e02b188b-a354-4370-bbc4-872fbc0a3eb4); Time taken: 0.167 seconds
INFO  : Concurrency mode is disabled, not creating a lock manager
INFO  : Executing command(queryId=hive_20211223013331_e02b188b-a354-4370-bbc4-872fbc0a3eb4): 

```hive
SELECT country_name, year, indicator_code, indicator_value
FROM wdi_csv_parquet
WHERE indicator_code='NY.GDP.MKTP.KD.ZG'
ORDER BY country_name, year
```
INFO  : Query ID = hive_20211223013331_e02b188b-a354-4370-bbc4-872fbc0a3eb4
INFO  : Total jobs = 1
INFO  : Launching Job 1 out of 1
INFO  : Starting task [Stage-1:MAPRED] in serial mode
INFO  : Subscribed to counters: [] for queryId: hive_20211223013331_e02b188b-a354-4370-bbc4-872fbc0a3eb4
INFO  : Tez session hasn't been created yet. Opening session
INFO  : Dag name: SELECT country_name, year, indicator_...year (Stage-1)
INFO  : Status: Running (Executing on YARN cluster with App id application_1640191836849_0053)
