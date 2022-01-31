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

    HuffmanObject o1 = list.poll();
    HuffmanObject o2 = list.poll();
    System.out.println("max weights are " + o1.getMaxMergeCount() + " and " + o2.getMaxMergeCount());
    System.out.println("min weights are " + o1.getMinMergeCount() + " and " + o2.getMinMergeCount());
  }

  private static PriorityQueue<HuffmanObject> calc(PriorityQueue<HuffmanObject> pq) {
    while(pq.size() > 2) {
      System.out.println(pq);

      HuffmanObject o1 = pq.poll();
      HuffmanObject o2 = pq.poll();
      System.out.println("merge " + o1.getWeight() + " and " + o2.getWeight() + " = " + Integer.sum(o1.getWeight(), o2.getWeight()));
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
    private Integer maxMergeCount;
    private Integer minMergeCount;
    private Integer weight;
    private List<HuffmanObject> children;

    HuffmanObject(Integer weight) {
      this.weight = weight;
      children = new ArrayList<>();
      maxMergeCount = 1;
      minMergeCount = 1;
    }

    HuffmanObject(HuffmanObject o) {
      this.maxMergeCount = o.getMaxMergeCount();
      this.minMergeCount = o.getMinMergeCount();
      this.children = o.getChildren();  
      this.weight = o.getWeight();
    }

    void merge(HuffmanObject o1) {
      HuffmanObject o2 = new HuffmanObject(this);
      List<HuffmanObject> mergedChildren = Arrays.asList(o1, o2);
      this.children = mergedChildren;

      if(children.size() > 2) {
        throw new IllegalArgumentException("children number exceeded");
      }

      this.weight = o1.getWeight() + o2.getWeight();
      this.maxMergeCount = o1.getMaxMergeCount() > o2.getMaxMergeCount() ? Integer.sum(o1.getMaxMergeCount(), 1) : Integer.sum(o2.getMaxMergeCount(), 1);
      this.minMergeCount = Integer.sum(this.minMergeCount, 1);
    }

    @Override
    public int compareTo(HuffmanObject other) {
      return Integer.compare(getWeight(), other.getWeight());
    }

    @Override
    public String toString() {
      return weight.toString() + "c:" + maxMergeCount + (children.isEmpty() ? "" : children.toString());
    }

    Integer getWeight() {
      return weight;
    }

    List<HuffmanObject> getChildren() {
      return children;
    }

    Integer getMaxMergeCount() {
      return maxMergeCount;
    }

    Integer getMinMergeCount() {
      return minMergeCount;
    }
  }
}
