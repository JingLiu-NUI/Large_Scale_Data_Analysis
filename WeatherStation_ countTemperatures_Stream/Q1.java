import java.util.*;

//Main Class to test 
class Q1{
		public static void main(String[]args){
		int startTime=1;
		int endTime=3;
		int maxTemp;
		//Create Measurement object
		Measurement ms1=new Measurement(1,20.1);
		Measurement ms2=new Measurement(2,30.1);
		Measurement ms3=new Measurement(5,40.1);
	    WeatherStation ws=new WeatherStation();
		//Add to List of Measurement
		ws.measurement.add(ms1);
		ws.measurement.add(ms2);
		ws.measurement.add(ms3);
		ws.maxTemperature(startTime,endTime);
	}
} 

class WeatherStation{	
	public String cityName;
	public static String[] station;	
	public List<Measurement> measurement=new ArrayList<>(); 
	//Method for get max temperature
	public void maxTemperature(int startTime,int endTime){
		 OptionalDouble maxTemp=measurement.stream()
					.filter(p -> p.getTime() >= startTime && p.getTime() <= endTime)
					.mapToDouble(Measurement::getTemp)
					.max();
				// if there is some available temperature exsting then return the max one
				//else output that no temperature exsting
				if (maxTemp.isPresent()) {
					System.out.println("The max temperature: "+ maxTemp.getAsDouble());
				}else{
					System.out.println("No available temperature");
			}	
		
	}
}

// Measurement Entity Class
class Measurement{
	int time;
	double temp;
	public Measurement(int time,double temp){
		this.time=time;
		this.temp=temp;
	}
// Get method and set method for all fields
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