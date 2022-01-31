import java.util.*;
import java.io.*;

public class WIS {
  public static void main(String[] args) {
    if(args.length < 1) {
      System.out.println("not enough args are given");
      return;
    }
    String fileName = args[0];

    List<Integer> list = readFile(fileName);
    System.out.println(list);
  }

  private static List<Integer> readFile(String fileName) {
    List<Integer> list = new ArrayList<>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;
      currentLine = reader.readLine();
      System.out.println(currentLine + " weights in the file");

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
