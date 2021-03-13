import java.io.*;

import java.util.*;

public class CountComparisons {

    private static long comparisonCount;

    public static void main(String[] arg) {
      if(arg.length != 1) {
        System.out.println("please give file name");
        return;
      }
      String fileName = arg[0];

      int[] arr = arrFromFile(fileName);

      System.out.println(arr.length + " elements");
      sort(arr);
      System.out.println(comparisonCount + " comparisons");
    }

    private static int[] arrFromFile(String fileName) {
      List<Integer> numList = new ArrayList<>();

      try {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String currentLine = reader.readLine();

        while(currentLine !=  null) {
          numList.add(Integer.valueOf(currentLine));
          currentLine = reader.readLine();
        }

      } catch (Exception e) {
        e.printStackTrace();
      }

      return numList.stream().mapToInt(Integer::intValue).toArray();
    }

    private static int[] sort(int[] arr) {
      return quicksort(arr, 0, arr.length - 1);
    }

    private static int[] quicksort(int[] arr, int start, int end) {
      comparisonCount = comparisonCount + end - start;
      if(end - start < 2) {
        if(arr[start] > arr[end]) {
          swap(arr, start, end);
        }
        return arr;
      }

      int pivot = getPivot(arr, start, end);
      System.out.println("pivot is " + pivot);

      swap(arr, start, pivot);

      int i = start + 1;
      int j = start + 1;

      while(j <= end) {
        if(arr[j] <= arr[start]) {
          swap(arr, i, j);
          i++;
        }
        j++;
      }

      //swap pivot to its place
      swap(arr, start, i - 1);
      
      //left side
      if(start < i - 1) {
        quicksort(arr, start, i - 2);
      }

      //right side
      if(i < end) {
        quicksort(arr, i, end);
      }

      return arr;
    }

    private static int[] swap(int[] arr, int a, int b) {
      int c = arr[a];
      arr[a] = arr[b];
      arr[b] = c;
      return arr;
    }

    private static int getPivot(int min, int max) {
      return max;
    }

    private static int getPivot(int[] arr, int min, int max) {
      int pivot0 = min;
      int pivot1 = (max + min) / 2;
      int pivot2 = max;

      int[] pivotArr = new int[3];
      pivotArr[0] = arr[pivot0];
      pivotArr[1] = arr[pivot1];
      pivotArr[2] = arr[pivot2];

      for(int i = 0; i < pivotArr.length; i++) {
        for(int j = i ; j < pivotArr.length; j++) {
          if(pivotArr[i] > pivotArr[j]) {
            swap(pivotArr, i, j); 
          }
        } 
      }
     
      if(pivotArr[1] == arr[pivot0]) {
        return pivot0;
      } else if(pivotArr[1] == arr[pivot1]) {
        return pivot1;
      }
      return pivot2;
    }
}

