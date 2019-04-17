package spark_project;

import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.DenseVector;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;

public class DatatoModelMapper implements Function<String, LabeledPoint> {
    int ct=0;
    public LabeledPoint call(String datarow) throws Exception{
        ++ct;
        String newLine = datarow.replaceAll("\\?", "99.0");
        String[] splits = newLine.split(",");

        if(Integer.valueOf(splits[13])>0){
            splits[13]="1";
        }

        //System.out.println(ct+". "+ "Splits Count: " + splits.length + " " + splits[13]);

        double[] features = new double[13];
        for(int i=0; i<13; i++){
            features[i]=Double.parseDouble(splits[i]);
        }
        Vector featuresVector = new DenseVector(features);

        LabeledPoint lp = new LabeledPoint(Double.parseDouble(splits[13]), featuresVector);
        return lp;
    }
}
