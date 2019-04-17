package spark_project;

public class Options {
    public static void choices(){
        System.out.println("\n1. Perform Data Analysis using SparkSQL");
        System.out.println("2. TrainModel using Naive Bayesian Classification");
        System.out.println("3. TrainModel using DecisionTree");
        System.out.println("4. TrainModel using Random Forest Classification");
        System.out.println("5. Predict the HeartDisease");
        System.out.println("6. Exit");
    }
    public static void analyse(){
        System.out.println("\n1. Heart Disease found in less than 45 years old");
        System.out.println("2. Minimum age attacked by Heart Disease");
    }
}
