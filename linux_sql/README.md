# Linux Cluster Monitoring Agent
# Introduction
In this project, I developed a minimum viable product (MVP) for 
demonstrating how hardware usage data could automatically be collected 
from Linux machines within a cluster and stored into a database. The 
applications of this project are numerous, and it may particularly prove 
useful to network administrators who may wish to understand how well a 
particular distributed system suits their specific processing and memory 
demands. The design works primarily with Linux bash scripts, which collect
all the necessary hardware usage data. These scripts are executed 
periodically by Linux's `crontab` command, and the data gets stored into 
a database managed with PostgreSQL. Another very important technology 
that was used is Docker, which allows the containerization of PostgreSQL.
This means that the database management system can run without actual 
installation on a host, making the design portable to any Linux machine.
This is especially useful for clusters, where more than one database may be 
desired. The project itself was developed
with Git version control, and stored in this GitHub repository. The 
Gitflow branching model was applied to facilitate continuous software 
development practices.

# Quick Start
Starting a PSQL instance using _psql_docker.sh_:<br />
`bash scripts/psql_docker.sh [start | stop | create] [db_username] [db_pasword]`

Creating the database tables in PSQL with _ddl.sql_:<br />
`psql -h psql_host -U psql_user -d db_name -f sql/ddl.sql`

Insert host hardware information into the "host_info" database table with _host_info.sh_:<br />
`bash scripts/host_info.sh psql_host psql_port db_name psql_user psql_password`

Insert host hardware usage information into the "host_usage" database table with _host_usage.sh_:<br />
`bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password`

Crontab setup script for usage data collection every minute:
`* * * * * bash /path/to/linux_sql/scripts/host_usage.sh psql_host port db_name psql_user psql_password &> /tmp/host_usage.log`

# Implementation 
This section describes the details pertaining to the implementation and design 
process of the system developed.
## Architecture
The schematic below illustrates the fundamental structure of a Linux cluster
of 3 machines. Also shown is the relative position of the database as well
as the monitoring agents. The purpose of this schematic is to demonstrate 
the feasibility of implementing the MVP on a cluster, since this project
focused on a per-unit implementation.

![alt text](https://raw.githubusercontent.com/jarviscanada/jarvis_data_eng_TomasRotbauer/feature/readme/linux_sql/assets/architecture.png "Architecture Schematic")

## Scripts
### Creating, starting & stopping a PSQL Docker container
1. To create a new container and volume for a PSQL instance:<br />
   `bash scripts/psql_docker.sh create db_username db_pasword` <br /> <br />
2. To start an existing instance:<br />
   `bash scripts/psql_docker.sh start` <br /> <br />
3. To stop a running instance:<br />
   `bash scripts/psql_docker.sh stop`
   
### Adding a new host:
This command will execute the bash script responsible for adding
a new host to the "host_info" database table. This is required in order to
collect usage data for the host. See "Database Modelling" below for 
details about what specific hardware info is collected.<br />
`scripts/host_info.sh psql_host psql_port db_name psql_user psql_password`
### Collecting usage data:
Upon execution of the _host_usage.sh_ script using the command below,
the host's current usage data is retrieved with `top` and saved to
the "host_usage" database table. When the host is not present in the 
"host_info" table, it is first added automatically via execution of the
_host_info.sh_ script above. See "Database Modelling" below for
details about what specific hardware usage data is collected.<br />
`bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password`
### Crontab setup:
The following `crontab` setup is used such that hardware usage data is
collected and stored every minute. It only needs to execute the
_host_usage.sh_ bash script, and the syntax is as follows:
```
# First, launch the crontab editor:
crontab -e
# Now simply insert the following line:
* * * * * bash /path/to/linux_sql/scripts/host_usage.sh psql_host port db_name psql_user psql_password &> /tmp/host_usage.log
```
### Useful queries (_sql/queries.sql_):
The file _queries.sql_ contains three useful queries that answer relevant business questions:
1. The first query in the file lists all the currently monitored
hosts, including the number of logical cores and total memory.
   Additionally, the hosts are ordered by these amounts in descending
   and ascending orders respectively. This is useful for getting a 
   sense of the amount of available resources.
   <br /><br />
2. The second query displays the average used memory per every 5-minute
interval for each host. This information can be very useful for
   monitoring purposes, as it can uncover patterns in usage 
   with respect to time. It also allows a network administrator to 
   decide whether the accessible memory of the cluster is enough to
   accommodate the local demand.
   <br /><br />
3. The third query detects host failure. The way it works is that 
it reports the host id, timestamp, and number of usage entries for 
   any 5-minute interval during which the host entered usage data 
   less than three times into the "host_usage" database table. Of
   course, a healthy host will report all 5 times during any given
   5-minute interval. This of course is useful for uncovering and
   troubleshooting an unresponsive host.
## Database Modelling
### Schema for the "host_info" table
| Attribute Name  | Description                             | Type
| ----------------|:----------------------------------------| :---
| id              | Unique number to identify each host by. It is the primary key| serial
| hostname        | The hostname, unique for every host     | varchar
| cpu_number      | Number of logical cores (total number of threads)   | smallint
| cpu_architecture| The processor architecture         | varchar
| cpu_model       | The processor model      | varchar
| cpu_mhz         | The processor clock frequency in MHz      | real
| l2_cache        | The size of the L2 cache in bytes    | varchar
| total_mem       | The total memory      | integer
| timestamp       | The timestamp from when the info was collected  | timestamp
### Schema for the "host_usage" table
| Attribute Name  | Description                             | Type
| ----------------|:----------------------------------------| :---
| timestamp       | The timestamp from when the info was collected  | timestamp
| host_id         | The same "id" number from the "host_info" table to identify each host by. It is not a superkey in this table| serial
| memory_free     | The amount of available memory      | integer
| cpu_idle        | Percentage of processor in idle mode   | smallint
| cpu_kernel      | Percentage of processor in kernel mode   | smallint
| disk_io         | Number of disk I/O devices   | smallint
| disk_available  | Available disk space   | varchar
# Test
Here are some of the testing methods that were used for functionality
validation purposes as well as debugging:
## Bash scripts
* The `bash -x` option was used for debugging. It prints out the 
details about where program control does what.
* _psql_docker.sh_ functionality was verified/tested
  using Docker CLI. E.g., after running the create command, check
  if volume was created using `docker volume ls` command.
* _host_info.sh_ & _host_usage.sh_ were tested on the local
host, and manually verified by checking the database contents
  after execution to make sure the entries match the actual specifications.
## SQL queries
Mock data was generated consisting of several entries. The output of each
query was tested against the expected result. For example, in order
to test the query that computes the average memory consumption, three
insertions were made with a synthetic timestamp mapping to one 
five-minute interval. The average used memory was calculated manually,
and made sure to match the output. The other two queries were
even simpler to manually verify, hence similar approaches were taken.
# Improvements
The following is a list of features that could be improved or added:
1. Add support for other operating systems. Currently, the bash
scripts and PSQL Docker image only work with Linux.
2. Integrate a backup storage system to account for potential data
loss. Currently, the database exists only on one of the machines.
3. Add an automatic alert mechanism for the event when host failure
occurs, as explained in the queries section. This would ensure that
   such events do not happen without anyone noticing.