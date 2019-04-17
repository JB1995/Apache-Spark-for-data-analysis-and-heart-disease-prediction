package spark_project;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.*;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import scala.Tuple2;

import java.util.HashMap;
import java.util.Map;

public class HeartDiseasePrediction_TestModel {
    public static void testModel(String path, JavaSparkContext sc, int state,
                                 JavaRDD<LabeledPoint> trainingData, JavaRDD<LabeledPoint> testingData){

        //2. NaiveBayesian Classification
        if(state==2) {

            NaiveBayesModel model = NaiveBayes.train(trainingData.rdd(), 1.0);

            JavaPairRDD<Object, Object> predictionAndLabel =
                    testingData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));

            System.out.println("Number of records for testing split: " + testingData.count());
            System.out.println("PredictedLabel\t"+"HeartDiseaseLabel\t"+"ResultStatus");
            for(Tuple2<Object, Object> it: predictionAndLabel.collect()){
                System.out.println(it._1()+"\t\t\t\t" + it._2() + "\t\t\t\t\t" + (it._1().equals(it._2())));
            }

            System.out.println("\nNaiveBayesClassification Metrics Evaluation");
            MetricsEvaluation.metrics(predictionAndLabel, testingData);

            // Save and load model
            // model.save(sc.sc(), "out/"+path);
            // NaiveBayesModel sameModel = NaiveBayesModel.load(sc.sc(), "out/"+path);
        }

        //4. Decision Tree
        if(state==3){

            int numClasses = 2;
            Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
            String impurity = "gini";
            int maxDepth = 20;
            int maxBins = 40;

            DecisionTreeModel model = DecisionTree.trainClassifier(trainingData, numClasses,
                    categoricalFeaturesInfo, impurity, maxDepth, maxBins);

            JavaPairRDD<Object, Object> predictionAndLabel =
                    testingData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));

            System.out.println("Number of records for testing split: " + testingData.count());
            System.out.println("PredictedLabel\t"+"HeartDiseaseLabel\t"+"ResultStatus");
            for(Tuple2<Object, Object> it: predictionAndLabel.collect()){
                System.out.println(it._1()+"\t\t\t\t" + it._2() + "\t\t\t\t\t" + (it._1().equals(it._2())));
            }

            System.out.println("\nDecisionTree Metrics Evaluation");
            MetricsEvaluation.metrics(predictionAndLabel, testingData);

            // Save and load model
            // model.save(sc.sc(), "out/"+path);
            // DecisionTreeModel sameModel = DecisionTreeModel.load(sc.sc(), "out/"+path);

        }

        //4. RandomForest
        if(state==4){

            Integer numClasses = 2;
            Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
            Integer numTrees = 80; // Use more in practice.
            String featureSubsetStrategy = "auto"; // Let the algorithm choose.
            String impurity = "gini";
            Integer maxDepth = 20;
            Integer maxBins = 40;
            Integer seed = 12345;

            RandomForestModel model = RandomForest.trainClassifier(trainingData, numClasses,
                    categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepth, maxBins,
                    seed);

            JavaPairRDD<Object, Object> predictionAndLabel =
                    testingData.mapToPair(p -> new Tuple2<>(model.predict(p.features()), p.label()));

            System.out.println("\nNumber of records for testing split: " + testingData.count());
            System.out.println("\nPredictedLabel\t"+"HeartDiseaseLabel\t"+"ResultStatus");
            for(Tuple2<Object, Object> it: predictionAndLabel.collect()){
                System.out.println(it._1()+"\t\t\t\t" + it._2() + "\t\t\t\t\t" + (it._1().equals(it._2())));
            }

            System.out.println("\nRandomForest Metrics Evaluation");
            MetricsEvaluation.metrics(predictionAndLabel, testingData);

            // Save and load model
            System.out.println("\nSaving Model........");
            model.save(sc.sc(), "out/"+path);
            //model.save(sc.sc(), "hdfs://localhost:9000/user/bikki/model/"+path);
//            RandomForestModel sameModel = RandomForestModel.load(sc.sc(), "out/"+path);
        }
    }
}
