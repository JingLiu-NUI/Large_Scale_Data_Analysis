import java.util.ArrayList;
import java.util.*;
import java.lang.*;
import java.lang.Runnable;

 public class Q3{
	@SuppressWarnings("unchecked") 
	public static int[] bucketSort(int[] numbers, int bucketCount, MyFnInterface sort) {
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
			// call sort function (sortFunc)and let all numbers in buckets get a correct order  
			sort.sortFunc(buckets[i]); 
			for (int j = 0; j < buckets[i].size(); j++) { // update array with the bucket content
				numbers[k] = buckets[i].get(j);
				k++;
			}
		 }

		return numbers;
	 }
	 
	public static void main(String []args){
		int[] numbers={2,33,12,43,12,51,32,54,6};
		int[]numbers2;
		//lambda expression
		//pass the lambda function to bucketsort method as the third argument 
		numbers2=bucketSort3.bucketSort(numbers,9,(ArrayList<Integer> buckets)->Collections.sort(buckets));
		for (int v=0;v<numbers.length;v++){
			System.out.println(numbers[v]);
		}
	}
	
}
// create a interface for sorting method(the type of the function is MyFnInterface)
@FunctionalInterface
interface MyFnInterface{
	public void sortFunc(ArrayList<Integer> buckets);
}
