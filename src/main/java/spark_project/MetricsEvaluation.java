package spark_project;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.regression.LabeledPoint;
import scala.Tuple2;

import java.util.Arrays;

public class MetricsEvaluation {
    public static void metrics(JavaPairRDD<Object, Object> predictionAndLabel, JavaRDD <LabeledPoint> testingData){

        BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(predictionAndLabel.rdd());

        // Confusion matrix
        MulticlassMetrics metrics2 = new MulticlassMetrics(predictionAndLabel.rdd());
        Matrix confusion = metrics2.confusionMatrix();
        System.out.println("\n1. Confusion matrix: \n" + confusion);

        //Accuracy
        double accuracy =
                predictionAndLabel.filter(pl -> pl._1().equals(pl._2())).count() / (double) testingData.count();
        System.out.printf("2. Accuracy: %.2f\n", accuracy*100);

        double TN = confusion.apply(0, 0);
        double TP = confusion.apply(1, 1);
        double FN = confusion.apply(1,0);
        double FP = confusion.apply(0, 1);
        double precision = TP/(double)(TP+FP);
        double recall = (double)TP/(TP+FN);
        double Fscore = 2*precision*recall/(double)(precision+recall);
        System.out.printf("3. Precison: %.2f\n", precision*100);
        System.out.printf("4. Recall: %.2f\n", recall*100);
        System.out.printf("5. F-Measure: %.2f\n", Fscore*100);
    }
}



