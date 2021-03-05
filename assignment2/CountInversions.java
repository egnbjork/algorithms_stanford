import java.io.*;

import java.util.*;

public class CountInversions {

    private static long inversionCount;

    public static void main(String[] arg) {
      if(arg.length != 1) {
        System.out.println("please give file name");
        return;
      }
      String fileName = arg[0];

      int[] arr = arrFromFile(fileName);

      System.out.println(arr.length + " elements");
      sort(arr);
      System.out.println(inversionCount + " inversions");
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
      if(arr.length < 2) {
        return arr;
      } 

      int newArrSize = arr.length / 2;
      
      int[] arr1 = new int[newArrSize];
      int[] arr2 = new int[arr.length - newArrSize]; 

      System.arraycopy(arr, 0, arr1, 0, arr1.length);
      System.arraycopy(arr, newArrSize, arr2, 0, arr2.length);

      arr1 = sort(arr1);
      arr2 = sort(arr2);

      int[] result = new int[arr.length];

      int ri = 0;
      int a1i = 0;
      int a2i = 0;

      while(ri < result.length) {
        if(a1i > arr1.length - 1) {
          result[ri] = arr2[a2i++];
        } else if(a2i > arr2.length - 1) {
          result[ri] = arr1[a1i++];
        } else if(arr1[a1i] <= arr2[a2i]) {
          result[ri] = arr1[a1i++];
        } else {
          result[ri] = arr2[a2i++];
          inversionCount += arr1.length - a1i;
        }
        
        ri++;
      }

      return result;
    }

}

