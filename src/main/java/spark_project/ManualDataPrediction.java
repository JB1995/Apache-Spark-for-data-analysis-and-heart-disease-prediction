package spark_project;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.tree.model.RandomForestModel;

import java.util.Scanner;

import static java.lang.System.exit;

public class ManualDataPrediction {
    public static void predictHeartDisease (JavaSparkContext sc, String path){
        //RandomForestModel sameModel = RandomForestModel.load(sc.sc(), "hdfs://localhost:9000/user/bikki/model/"+path);
        RandomForestModel sameModel = RandomForestModel.load(sc.sc(), "out/"+path);
        Scanner scan = new Scanner(System.in);
        String str[] = {"Age", "Gender", "Chestpain(1-4)",
                "BloodPressure", "Cholesterol", "FastingBloodSugar", "ECG(0-2)", "MaxHeartRate",
                "ExerciseAngina(0-1)", "OldPeak", "Slope(1-3)", "MajorVesselsNo.(0-3)", "Thal(3,6,7)", "HeartDisease"};
        boolean contd=true;
        while(contd){
            System.out.println("\nPlease Provide the data to be tested");
            double fieldValues[] = new double[13];
            for (int i = 0; i < 13; i++) {
                System.out.print(str[i]+": ");
                fieldValues[i] = scan.nextDouble();
            }
            Vector features = new DenseVector(fieldValues);
            Double result = sameModel.predict(features);
            System.out.println("\nPredicted HeartDisease: "+result);
            System.out.println("\nPress '1' to continue and '0' to exit");
            if(scan.nextInt()==1){
                contd=true;
            }else{
                contd=false;
                exit(0);
            }
        }
    }
}