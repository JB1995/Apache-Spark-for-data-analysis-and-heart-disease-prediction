package spark_project;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.min;

public class DataAnalysisSparkSQL {
    public static void analysis(String path){
        Logger.getLogger("org").setLevel(Level.ERROR);
        SparkSession session = SparkSession.builder().appName("HeartDiseaseAnalysis").master("local[*]").getOrCreate();

        DataFrameReader dfReader = session.read();

        //Defining Columns Names for Dataset i.e. SchemaDetails
        String schemaString = "Age Sex Chestpain BloodPressure Cholestoral FastingBloodSugar ECG MaxHeartRate ExerciseAngina OldPeak Slope MajorVesselsNo. HeartRate HeartDisease";
        List<StructField> fields = new ArrayList<>();
        for (String fieldName : schemaString.split(" ")) {
            fields.add(DataTypes.createStructField(fieldName, DataTypes.StringType, true));
        }
        StructType schema = DataTypes.createStructType(fields);

        //load cleaned data of cleaveland csv file
        Dataset<Row> responses =
                dfReader.option("header", "false").schema(schema).csv(path);

        responses.printSchema();
        responses.show();
        boolean contd=true;
        Scanner scan = new Scanner(System.in);
        while(contd){
            Options.analyse();
            int choose = scan.nextInt();
            switch(choose) {
                case 1:
                    System.out.print("HeartDisease in less than 45 years old is: ");
                    Dataset<Row> lessthan45 = responses.filter(col("Age").$less(45)).
                            filter(col("HeartDisease").$greater(0));
                    System.out.println(lessthan45.count() + " and they are as shown below:-");
                    lessthan45.show();
                    break;

                case 2:
                    System.out.println("Minimum Age Attacked by Heart Disease");
                    responses.filter(col("HeartDisease").$greater(0)).select(min(col("Age"))).show();
                    break;

                default:
                    break;
            }
            System.out.println("\nPress '1' to continue and '0' to exit");
            if(scan.nextInt()==1){
                contd=true;
            }else{
                contd=false;
            }
        }
        //session.stop();
    }
}
