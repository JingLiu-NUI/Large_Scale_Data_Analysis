import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import scala.Tuple2;

public class Kmeans {
	public static void movieRe() {
		//Q3
		System.setProperty("hadoop.home.dir", "C:/winutils"); // use this to avoid (harmless) exception in case you couldn't set system variable HADOOP_HOME
		//configuration
		SparkConf sparkConf = new SparkConf().setAppName("movieRe").setMaster("local[4]").set("spark.executor.memory","1g");
		//create object for JavaSparkContext
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		//read data and save as RDD
		JavaRDD<String> Data = ctx.textFile("twitter2D.txt", 1);
		//get coordinate for training
		JavaRDD<Vector> parsedData = Data.map(
				(String s) -> {
				// split by , and get a list
				String[] sarray = s.split(",");
				//only use the first two columns data as the train data to get the clusters
				double[] values = new double[2];
				for (int i = 0; i < 2; i++)
				values[i] = Double.parseDouble(sarray[i]);
				// convert to vector and each vertor like[Xcoordinate,Ycoordinate]
				return Vectors.dense(values);
				}
				);
		parsedData.cache();
		int numClusters = 4;
		int numIterations = 20;
		//create KMean clusters model
		KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);
		// get the pair RDD the key is the cluster and the value is each sentence, then sort by key
		JavaPairRDD<Integer,String>predictionAndLabels =Data.mapToPair(s->{
				String[] sarray = s.split(",");
				String ss=sarray[sarray.length-1];
				double[] values = new double[2];
				for (int i = 0; i < 2; i++)
				values[i] = Double.parseDouble(sarray[i]);
				return new Tuple2<>(clusters.predict(Vectors.dense(values)),ss);
		}).sortByKey();
		//convert to list and output each one
		List<Tuple2<Integer, String>> prediction = predictionAndLabels.collect();
		for (Tuple2<?, ?> tuple : prediction)
			System.out.println("Tweet "+tuple._2()+ " is in cluster "+tuple._1());
		ctx.stop();
		ctx.close();	
	}
	public static void main(String[]args) {
		movieRe();
	}
}