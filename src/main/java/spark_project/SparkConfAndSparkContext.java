package spark_project;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkConfAndSparkContext {

    private JavaSparkContext sc;

    public JavaSparkContext loadSparkContext(String appName, String appType){
        Logger.getLogger("org").setLevel(Level.ERROR);
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(appType);
        sc = new JavaSparkContext(conf);
        System.out.println("Spark Context Created");
        return sc;
    }

    public void closesc(){
        sc.close();
    }
}
