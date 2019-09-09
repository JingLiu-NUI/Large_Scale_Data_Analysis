import java.util.ArrayList;
import java.util.*;
import java.lang.*;
import java.lang.Runnable;
//class added called bucket
 public class Q1{
	public static int[] bucketSort(int[] numbers, int bucketCount) {
		 if (numbers.length <= 1) return numbers;
		 int maxVal = numbers[0];
		 int minVal = numbers[0];
		 for (int i = 1; i < numbers.length; i++) {
		 if (numbers[i] > maxVal) maxVal = numbers[i];
		 if (numbers[i] < minVal) minVal = numbers[i];
		 }

		 double interval = ((double)(maxVal - minVal + 1)) / bucketCount; // range of bucket
		 ArrayList<Integer> buckets[] = new ArrayList[bucketCount];

		 for (int i = 0; i < bucketCount; i++) // initialize buckets (initially empty)
		 buckets[i] = new ArrayList<Integer>();

		 for (int i = 0; i < numbers.length; i++) // distribute numbers to buckets
		 buckets[(int)((numbers[i] - minVal)/interval)].add(numbers[i]);

		 int k = 0;

		 for (int i = 0; i < buckets.length; i++) {

		 Collections.sort(buckets[i]); 

		 for (int j = 0; j < buckets[i].size(); j++) { // update array with the bucket content
		 numbers[k] = buckets[i].get(j);
		 k++;
		 }
		 }

		return numbers;
	 }
	// main method and create a array called numbers and the bucket size is 9
	// and then output the array of numbers,and array is reference pass, so the original array is
	// changed when call bucketSort method.	
	public static void main(String []args){
		int[] numbers={2,33,12,43,12,51,32,54,6};
		int bucketCount=8;
		bucketSort(numbers,bucketCount);
		for (int v=0;v<numbers.length;v++){
			System.out.println(numbers[v]);
		}
	}
}