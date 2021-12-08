import org.apache.spark.sql.SparkSession

object Main {
    def main(args: Array[String]) : Unit = {
        require(args.length == 2, "Expected 2 table names as program parameters");
    
        var spark = SparkSession.builder().appName("CompareSparkTables").getOrCreate();
        
        var df_src = spark.read.option("header", true).load(args(0));
        var df_backup = spark.read.option("header", true).load(args(1));
        
        
    }
}
