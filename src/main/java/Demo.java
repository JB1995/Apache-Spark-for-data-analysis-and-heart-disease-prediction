import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import scala.Tuple2;
import java.util.Arrays;

public class Demo {

    public static void main(String[] args){

        //Create a SparkContext to initialize
        Logger.getLogger("org").setLevel(Level.ERROR);
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Word Count");

        // Create a Java version of the Spark Context
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Load the text into a Spark RDD, which is a distributed representation of each line of text
        JavaRDD<String> textFile = sc.textFile("src/main/resources/shakespeare.txt");
        JavaPairRDD<String, Integer> counts = textFile
                .flatMap(s -> Arrays.asList(s.split("[ ,]")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a + b);
        counts.foreach(p -> System.out.println(p));
        System.out.println("Total words: " + counts.count());
        counts.saveAsTextFile("/tmp/shakespeareWordCount");
    }
}