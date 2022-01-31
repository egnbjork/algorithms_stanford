import java.util.*;
import java.io.*;

public class WIS {
  public static void main(String[] args) {
    if(args.length < 1) {
      System.out.println("not enough args are given");
      return;
    }
    String fileName = args[0];

    List<Integer> weightList = readFile(fileName);
    System.out.println(weightList.size());

    Set<Integer> indexSet = calc(weightList);
    System.out.println(indexSet);

    //checkIfExists(indexSet, Arrays.asList(1, 2, 3, 4, 17, 117, 517, 997));
    checkIfExists(indexSet, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
  }

  private static void checkIfExists(Set<Integer> indexSet, List<Integer> checkList) {
    checkList.forEach(n -> {
      if(indexSet.contains(n)) {
        System.out.print("1");
      } else {
        System.out.print("0");
      }
    });
    System.out.println("");
  }

  private static Set<Integer> calc(List<Integer> weightList) {
    int sum = 0;
    Set<Integer> indexSet = new TreeSet<>();

    int index = weightList.size() - 1;
    while(index >= 0) {
      System.out.println("index: " + index);
      int a1 = index - 1 >= 0 ? weightList.get(index - 1) : 0;
      int a2 = index - 2 >= 0 ? weightList.get(index - 2) + weightList.get(index) : weightList.get(index);
      if(a1 >= a2) {
        index -= 1;
      } else {
        sum += weightList.get(index);
        indexSet.add(index);
        index -= 2;
      }
      System.out.println("sum: " + sum);
      System.out.println("indexSet: " + indexSet);
    }
     
    System.out.println("sum is " + sum);
    return indexSet;
  }

  private static List<Integer> readFile(String fileName) {
    List<Integer> list = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;
      currentLine = reader.readLine();
      System.out.println(currentLine + " weights in the file");

      list.add(0);
      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;

        list.add(Integer.valueOf(currentLine));
      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return list;
  }
}
