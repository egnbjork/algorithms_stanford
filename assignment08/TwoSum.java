import java.io.*;
import java.util.*;

public class TwoSum {
  public static void main(String[] args) {
    if(args.length < 3) {
      System.out.println("not enough args are given");
      return;
    }
    String fileName = args[0];
    Long min = Long.valueOf(args[1]);
    Long max = Long.valueOf(args[2]);

    List<Long> numbers = readFile(fileName);

    System.out.println(numbers.size());
    System.out.println("\n\n\n SIZE IS " + getTwoSums(numbers, min, max));
  }

  private static int getTwoSums(List<Long> numbers, Long min, Long max) {
    Set<Long> sums = new HashSet<>();
    TreeSet<Long> treeSet = new TreeSet<>(numbers);
    System.out.println("size " + treeSet.size());

    Iterator<Long> it = treeSet.iterator();

    while(it.hasNext()) {
      Long next = it.next();
      for(int i = min.intValue(); i <= max.intValue(); i++) {
        if(treeSet.contains(i - next)) {
          sums.add((i - next) + next);
        }
      }
    }

    return sums.size();
  }

  private static List<Long> readFile(String fileName) {
    List<Long> file = new ArrayList<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;

      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;

        file.add(Long.valueOf(currentLine));
      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return file;
  }
}
