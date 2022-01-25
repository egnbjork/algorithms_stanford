import java.io.*;
import java.util.*;

public class Huffman {
  public static void main(String[] args) {
    if(args.length < 1) {
      System.out.println("not enough args are given");
      return;
    }
    String fileName = args[0];

    PriorityQueue<HuffmanObject> pq = readFile(fileName);
    System.out.println(pq);
  }

  private static PriorityQueue<HuffmanObject> readFile(String fileName) {
    PriorityQueue<HuffmanObject> pq = new PriorityQueue<>();
    Huffman h = new Huffman();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;
      currentLine = reader.readLine();
      System.out.println(currentLine + " jobs in the file");

      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;

        pq.add(h.new HuffmanObject(Integer.valueOf(currentLine)));
      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return pq;
  }

  private class HuffmanObject implements Comparable<HuffmanObject> {
    private Integer weight;
    private List<HuffmanObject> children;

    HuffmanObject(Integer weight) {
      this.weight = weight;
    }

    @Override
    public int compareTo(HuffmanObject other) {
      return Integer.compare(getWeight(), other.getWeight());
    }

    @Override
    public String toString() {
      return weight.toString();
    }

    Integer getWeight() {
      return weight;
    }
  }
}
