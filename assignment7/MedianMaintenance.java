import java.io.*; 
import java.util.*;

public class MedianMaintenance {

  public static void main(String[] args) {
    if(args.length != 1) {
      System.out.println("please give file name");
      return;
    }
    String fileName = args[0];

    List<Integer> fileData = readFile(fileName);

    System.out.println(calculateMedian(fileData));
  }

  private static Integer calculateMedian(List<Integer> numbers) {
    TreeSet<Integer> minHeap = new TreeSet<>();
    TreeSet<Integer> maxHeap = new TreeSet<>();
    Integer medianSum = 0;

    for(Integer number : numbers) {
      System.out.println(number);
      fillHeaps(minHeap, maxHeap, number);

      System.out.println("median is " + minHeap.last());
      medianSum += minHeap.last();
      //calc median
    }

    return medianSum;
  }

  private static void fillHeaps(TreeSet<Integer> minHeap, TreeSet<Integer> maxHeap, Integer number) {
    System.out.println("fill heaps");
    if(minHeap.isEmpty()) {
      minHeap.add(number);
    } else if(maxHeap.isEmpty()) {
      if(number > minHeap.last()) {
        maxHeap.add(number);
      } else {
        minHeap.add(number);
      }
    }else if(number < maxHeap.first()) {
      minHeap.add(number);
    } else {
      maxHeap.add(number);
    }

    //balance heaps
    if(!isHeapsBalanced(minHeap, maxHeap)) {
      balanceHeaps(minHeap, maxHeap);
    }

    System.out.println(isHeapsBalanced(minHeap, maxHeap));

    System.out.println("min heap size: " + minHeap.size());
    System.out.println("max heap size: " + maxHeap.size());
  }

  private static void balanceHeaps(TreeSet<Integer> minHeap, TreeSet<Integer> maxHeap) {
    if(minHeap.isEmpty() || maxHeap.size() > minHeap.size()) {
      minHeap.add(maxHeap.pollFirst());
    } else if(minHeap.size() > maxHeap.size() + 1) {
      maxHeap.add(minHeap.pollLast());
    }
  }

  private static boolean isHeapsBalanced(TreeSet<Integer> minHeap, TreeSet<Integer> maxHeap) {
    return minHeap.size() == maxHeap.size() || minHeap.size() == maxHeap.size() + 1;
  }

  private static List<Integer> readFile(String fileName) {
    List<Integer> file = new ArrayList<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;

      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;

        file.add(Integer.valueOf(currentLine));
      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return file;
  }
}
