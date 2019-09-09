import java.util.ArrayList;
import java.util.*;
import java.lang.*;
import java.lang.Runnable;

public class Q2 {
	public static int thread_id=0;
	@SuppressWarnings("unchecked")
	public static int[] bucketSort(int[] numbers,int bucketCount) {
		if (numbers.length <= 1) return numbers;
		
		int maxVal = numbers[0];
		int minVal = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > maxVal) maxVal = numbers[i];
			if (numbers[i] < minVal) minVal = numbers[i];
		}
		double interval = ((double)(maxVal - minVal + 1)) / bucketCount; // range of bucket
		ArrayList<Integer> buckets[]=new ArrayList[bucketCount];
		for (int i = 0; i < bucketCount; i++) // initialize buckets (initially empty)
		buckets[i] = new ArrayList<Integer>();
		for (int i = 0; i < numbers.length; i++) // distribute numbers to buckets
		buckets[(int)((numbers[i] - minVal)/interval)].add(numbers[i]);
		//create Thread for each bucket, one thread deal with one bucket to sort
		for(int i=0;i<bucketCount;i++){
			Thread t=new Thread(new Runnable(){
				@Override
				public void run(){ 
				    thread_id++;
					Collections.sort(buckets[thread_id-1]);
					//System.out.println("thread_id"+thread_id);
				}
			});
		//each thread have to wait unitl last thread finished,each time only one thread
		//is allowed to execute.
			t.start();
			try{
				t.join();
			}
			catch(Exception e){}
		}
		int k=0;
		for (int i=0;i<buckets.length;i++){
			for (int j = 0; j < buckets[i].size(); j++) { // update array with the bucket content
				numbers[k] = buckets[i].get(j);
				k++;
			} 
		}
			return numbers;
	}
	//main method and create a array called numbers and the bucket size is 9
	//and then output the array of numbers
	public static void main(String []args){
		int[] numbers={2,33,12,43,12,51,32,-54,-6,45,0,0,5};
		int bucketCount=8;
		bucketSort(numbers,bucketCount);
		for (int i=0;i<numbers.length;i++ ){
			System.out.println(numbers[i]);
		}
	}
}
