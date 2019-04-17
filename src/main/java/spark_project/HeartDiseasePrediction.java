package spark_project;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.regression.LabeledPoint;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.exit;

public class HeartDiseasePrediction {
    public static void main(String [] args) throws IOException {

        System.out.println("Setting up SparkConfiguration and SparkContext");
        SparkConfAndSparkContext contextBuilder = new SparkConfAndSparkContext();
        JavaSparkContext sc = contextBuilder.loadSparkContext("HeartDiseasePrediction", "local[*]");

        String data = "src/main/resources/full_data_cleaned_2.csv";
        //String data = "hdfs://localhost:9000/user/bikki/input/full_data_cleaned_2.csv";

        JavaRDD<String> inputFile = sc.textFile(data);
        JavaRDD<LabeledPoint> cleanedData = inputFile.map(new DatatoModelMapper());

        JavaRDD<LabeledPoint>[] splits = cleanedData.randomSplit(new double[]{0.7, 0.3}, 1234L);
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testingData = splits[1];

        Scanner scan = new Scanner(System.in);
        while(true) {
            //Perform Analysis according to the requirement
            System.out.println("\nSelect options to perform analysis");
            Options.choices();
            int choose = scan.nextInt();
            switch (choose) {
                case 1:
                    DataAnalysisSparkSQL obj = new DataAnalysisSparkSQL();
                    obj.analysis(data);
                    break;

                case 2:
                    HeartDiseasePrediction_TestModel
                            .testModel("NaiveBayesian/", sc, 2, trainingData, testingData);
                    break;

                case 3:
                    HeartDiseasePrediction_TestModel
                            .testModel("DecisionTree/", sc, 3, trainingData, testingData);
                    break;

                case 4:
                    HeartDiseasePrediction_TestModel
                            .testModel("RandomForest/", sc, 4, trainingData, testingData);
                    break;

                case 5:
                    ManualDataPrediction.predictHeartDisease(sc, "RandomForest/");
                    break;

                default:
                    contextBuilder.closesc();
                    exit(0);
            }
        }
    }
}
