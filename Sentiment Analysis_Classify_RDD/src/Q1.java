import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vectors;
import com.google.common.base.Function;
import scala.Tuple2;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.*;
//create Weather Station class 
public class Q1  {
	//test
		public static void main(String[]args){
			//first station's measurements
			List<Measurement> measurements1=new ArrayList<>() ;
			Measurement ms1=new Measurement(1,20.0);
			Measurement ms2=new Measurement(2,11.7);
			Measurement ms3=new Measurement(5,-5.4);
		    Measurement ms4=new Measurement(2,18.7);
			Measurement ms5=new Measurement(2,3.1);
			measurements1.add(ms1);
			measurements1.add(ms2);
			measurements1.add(ms3);
			measurements1.add(ms4);
			measurements1.add(ms5);
			//create first station
			WeatherStation ws1=new WeatherStation("Galway",measurements1);
			WeatherStation.stations.add(ws1);
			//second station's measurements
			List<Measurement> measurements2=new ArrayList<>();
			Measurement ms6=new Measurement(1,8.4);
			Measurement ms7=new Measurement(2,3.2);
			Measurement ms8=new Measurement(5,2.1);	
			measurements2.add(ms6);
			measurements2.add(ms7);
			measurements2.add(ms8);
			//create second station
			WeatherStation ws2=new WeatherStation("Cork",measurements2);
			WeatherStation.stations.add(ws2);
			System.out.println("Total count tempterature  is : "+WeatherStation.countTemperature(3));
			
		}
	
}

class WeatherStation {
	//original filed 
		public String cityName;
		public static List<WeatherStation> stations=new ArrayList<>();
		public List<Measurement> measurements;
		public WeatherStation(String cityName,List<Measurement> measurements){
			this.cityName=cityName;
			this.measurements=measurements;
		} 
		//countTemperature function
		public static int countTemperature(int t) {
			 // use this to avoid (harmless) exception in case you couldn't set system variable HADOOP_HOME
			System.setProperty("hadoop.home.dir", "C:/winutils");
			//configuration
			SparkConf sparkConf = new SparkConf().setAppName("countTemperature").setMaster("local[4]").set("spark.executor.memory","1g");
			//create object for JavaSparkContext
			JavaSparkContext ctx = new JavaSparkContext(sparkConf);
			//get all temperatures using stream and save as a list. 
			List<Double> inputData=stations.parallelStream()
					.flatMap(item->item.getMeasurements().parallelStream())
					.map(item->item.getTemp())
					.collect(Collectors.toList());
			//convert list to JavaRDD
			JavaRDD<Double> wstations = ctx.parallelize(inputData);
			//filter all temperatures that between [t+1,t-1] and convert RDD to PairRDD, the key is temperatures and the value is 1
			JavaPairRDD<Double,Integer> ss =wstations.filter(f->f<=(t+1)&&f>=(t-1))
					.mapToPair((s) -> new Tuple2<Double,Integer>(s,1));
			//get the counts of each value which have same key
			JavaPairRDD<Double, Integer> counts = ss.reduceByKey((Integer i1, Integer i2) -> i1 + i2);
			//convert to List<Tuple2<Double, Integer>>
			List<Tuple2<Double, Integer>> output = counts.collect();
			int s=0;
			//calculate the sum counts of all temperatures 
			for (Tuple2<Double, Integer> tuple : output) {
					s=s + tuple._2$mcI$sp();
			}
			//stop and close the JavaSparkContext.
			ctx.stop();
			ctx.close();
			return s;
			
		}
		// get Method for fields
		public String getCityName(){
			return cityName;
		}
		public List<Measurement> getMeasurements(){
			return measurements;
		}
	
}

//Creating Entity Class for Measurement
class Measurement {
	int time;
	double temp;
	public Measurement(int time,double temp){
		this.time=time;
		this.temp=temp;
	}
	public double getTemp(){
		return temp;
	}
	public void setTemp(double temp){
		this.temp=temp;
	} 
	public int getTime(){
		return time;
	}
	public void setTemp(int time){
		this.time=time;
	}
	
}
