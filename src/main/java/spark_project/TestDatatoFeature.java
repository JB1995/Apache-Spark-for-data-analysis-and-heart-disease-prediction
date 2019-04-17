package spark_project;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;

public class TestDatatoFeature implements Function<String, Vector> {
    int ct=0;
    public Vector call(String data) throws Exception{
        ++ct;
        String newLine = data.replaceAll("\\?", "99.0");
        String[] fields = newLine.split(",");
        if(Integer.valueOf(fields[13])>0){
            fields[13]="1";
        }
        System.out.println(ct+ ". " + "Splits Count: " + fields[13]);

        double fieldValues[] = new double[13];
        for(int i=0; i<13; i++){
            fieldValues[i]=Double.parseDouble(fields[i]);
        }
        Vector features = new DenseVector(fieldValues);
        return features;
    }
}
