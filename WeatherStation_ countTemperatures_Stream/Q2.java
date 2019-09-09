import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.Arrays;
import java.lang.String;
import java.util.List;
//create test class and create test data
class Q2{	
	public static void main(String[]args){
		//first station's measurements
		List<Measurement> measurements1=new ArrayList<>() ;
		Measurement ms1=new Measurement(1,20.0);
		Measurement ms2=new Measurement(2,11.7);
		Measurement ms3=new Measurement(5,-5.4);
	    Measurement ms4=new Measurement(2,18.7);
		Measurement ms5=new Measurement(2,20.9);
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
		Measurement ms7=new Measurement(2,19.2);
		Measurement ms8=new Measurement(5,7.2);	
		measurements2.add(ms6);
		measurements2.add(ms7);
		measurements2.add(ms8);
		//create second station
		WeatherStation ws2=new WeatherStation("Cork",measurements2);
		WeatherStation.stations.add(ws2);
		WeatherStation.countTemperatures(19.0,10.8,2.1);	
	}
}
//define a generic class for "map"
class Pair<K,V>{
	K first;
    V second;
	public Pair(K f,V s){
        first = f;
        second = s;
	}
	// get method
	public  K getKey(){
		return first;
	}
	public  V getValue(){
		return second;
	}
}
// WeatherStation Class
class WeatherStation{
	public String cityName;
	public static List<WeatherStation> stations=new ArrayList<>();
	public List<Measurement> measurements;
	public WeatherStation(String cityName,List<Measurement> measurements){
		this.cityName=cityName;
		this.measurements=measurements;
	} 
	// method for generate a intermediate List like((19.0,1),(19.0,1),(19.0,1),(19.0,1))
	public static List<Pair<Double,Integer>> getMap(List<Pair<WeatherStation,List<Measurement>>> inputData, double t,double r){
		List<Pair<Double,Integer>> InterMediate=inputData.parallelStream()	
											   .flatMap(w->w.getValue().parallelStream())
											   .filter(item->item.getTemp()<=(t+r)&&item.getTemp()>=(t-r))
											   .map(item -> {return new Pair<>(t,1);})
											   .collect(Collectors.toList()); 
		return InterMediate;
	}
	//CountTemperatures Method
	public static void countTemperatures(double t1,double t2,double r){
		//generate a List like((ObjectOfWeatherStation,ListOfMeasurement),(ObjectOfWeatherStation,ListOfMeasurement))
		List<Pair<WeatherStation,List<Measurement>>> inputData=stations.parallelStream()
																		.map(item->{return new Pair<>(item,item.getMeasurements());})
																		.collect(Collectors.toList());
		//call the getMap function to generate List1 and List2 for t1 and t2 respectively. 
		//this phase similar to map  and each List key is t1 or t2, value is 1		
		List<Pair<Double,Integer>> list1=WeatherStation.getMap(inputData,t1,r);
		List<Pair<Double,Integer>> list2=WeatherStation.getMap(inputData,t2,r);
		//to counting the number of pairs for key=t1 and key=t2 respectively, get map fistly and the covert to List
		List<Pair<Double,Long>> result = Stream.of(list1.parallelStream(),list2.parallelStream())
											   .flatMap(Function.identity())
											   .collect(Collectors.groupingBy(Pair::getKey,Collectors.counting()))
											   .entrySet()
											   .parallelStream()
											   .map(item->{return new Pair<>(item.getKey(),item.getValue());})
											   .collect(Collectors.toList());
		//Output for the result List<Pair<Double,Long>>
		System.out.println("The list of Result is:"+result);
		// Output for each key and value
		for(int i=0;i<result.size();i++){
			System.out.println("The Key is:"+result.get(i).getKey());
			System.out.println("The Value is:"+result.get(i).getValue());
		}
	}
	// get Method for fields
	public String getCityName(){
		return cityName;
	}
	public List<Measurement> getMeasurements(){
		return measurements;
	}
	
}	
// Creating Entity Class for Measurement
class Measurement{
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