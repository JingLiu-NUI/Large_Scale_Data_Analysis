import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.spark.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.feature.HashingTF;
import org.apache.spark.mllib.util.MLUtils;
import scala.Tuple2;
import org.apache.spark.ml.feature.StopWordsRemover;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.SparkSession;
public class Q2 {
	public static void movieRe() {
		//Q2*1
		System.setProperty("hadoop.home.dir", "C:/winutils"); // use this to avoid (harmless) exception in case you couldn't set system variable HADOOP_HOME
		//configuration
		SparkConf sparkConf = new SparkConf().setAppName("countTemperature").setMaster("local[4]").set("spark.executor.memory","1g");
		//create object for JavaSparkContext
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		//read data and save as RDD
		JavaRDD<String> Data = ctx.textFile("imdb_labelled.txt", 1);
		// create object for HashingTF and 100 is the feature number, use HashingTF to transform and get the LabelPoint of label and training set 
		final HashingTF tf = new HashingTF(100);
		double[] weights = {0.6, 0.4};
	    Long seed = 50L;
	    //use randomSplit to split the data to train set and test set and get the train data.
		JavaRDD<String> traingData=Data.randomSplit(weights,seed)[0];
		traingData.cache();
		//use randomSplit to split the data to train set and test set and get the test data.
		JavaRDD<String> testData=Data.randomSplit(weights,seed)[1];
		//convert JDD<string> train data to JDD<LabelPoint>
		JavaRDD<LabeledPoint> traingDatatoLP = traingData.map(f -> new LabeledPoint(Double.parseDouble(f.split("	")[1]), tf.transform(Arrays.asList(f.split("	")[0].split(" ")))));
		//SVM model, train the training data and train 500 times
		SVMModel model = SVMWithSGD.train(traingDatatoLP.rdd(), 500);
		//model.clearThreshold();
		//get a pair RDD, the key is predict value and the value is real value for test dataset.u
		JavaPairRDD<Object,Object>predictionAndLabels =testData.mapToPair(f->new Tuple2<>(model.predict(tf.transform(Arrays.asList(f.split("	")[0].split(" ")))),Double.parseDouble(f.split("	")[1])));
		List<Tuple2<Object,Object>> prediction = predictionAndLabels.collect();
		// print predict label values
		for (Tuple2<Object,Object> tuple : prediction) {
			System.out.println(tuple._1);
		}
		
		//Q2*2
		//create object of BinaryClassificationMetrics for prediction and real data
		BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(predictionAndLabels.rdd());
		//output the metrics.areaUnderROC.
		System.out.println("Area under ROC = " + metrics.areaUnderROC());
		ctx.stop();
		ctx.close();
		}
	public static void main(String[]args){
		movieRe();
	}
}
