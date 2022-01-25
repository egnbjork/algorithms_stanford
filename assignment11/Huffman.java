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

    PriorityQueue<HuffmanObject> list = calc(pq);
    System.out.println(list);
  }

  private static PriorityQueue<HuffmanObject> calc(PriorityQueue<HuffmanObject> pq) {
    while(pq.size() > 2) {
      System.out.println(pq);

      HuffmanObject o1 = pq.poll();
      HuffmanObject o2 = pq.poll();
      System.out.println("merge " + o1.getWeight() + " and " + o2.getWeight());
      o2.merge(o1);
      pq.add(o2);
    }
    return pq;
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
      children = new ArrayList<>();
    }

    void merge(HuffmanObject o1) {
      HuffmanObject o2 = new HuffmanObject(weight);
      o2.children = new ArrayList<>(children);;
      List<HuffmanObject> mergedChildren = Arrays.asList(o1, o2);
      this.children = mergedChildren;

      if(children.size() > 2) {
        throw new IllegalArgumentException("children number exceeded");
      }

      this.weight = o1.getWeight() + o2.getWeight();
    }

    @Override
    public int compareTo(HuffmanObject other) {
      return Integer.compare(getWeight(), other.getWeight());
    }

    @Override
    public String toString() {
      return weight.toString() + (children.isEmpty() ? "" : children.toString());
    }

    Integer getWeight() {
      return weight;
    }
  }
}
