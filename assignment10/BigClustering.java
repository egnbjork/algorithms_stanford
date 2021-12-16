import java.io.*;
import java.util.*;
import java.util.stream.*;


public class BigClustering {

  private static int bitNumber;

  public static void main(String[] args) {
    if(args.length < 2) {
      System.out.println("not enough args are given");
      return;
    }
    String fileName = args[0];
    Integer clusterSize = Integer.valueOf(args[1]);
    System.out.println(clusterSize + " clustering");

    List<BitSet> distanceList = BigClustering.readFile(fileName);
    //distanceList.forEach(n -> printBitSet(n));

    cluster(distanceList, clusterSize);
  }


  private static Set<Set<BitSet>> cluster(List<BitSet> bitList, int clusterSize) {
    Set<Set<BitSet>> clusterSet = new HashSet<>();
    Set<BitSet> bitSet = new HashSet<>(bitList);

    for(int i = 0; i < bitList.size(); i++) {
      if(bitSet.contains(bitList.get(i))) {
        //printBitSet(bitList.get(i));
        clusterSet.add(findChange(bitSet, bitList.get(i)));
      }
    }

    System.out.println("=======");
    System.out.println("clusters found: " + clusterSet.size());
    //clusterSet.forEach(n -> {System.out.println(""); n.forEach(p -> printBitSet(p));});
    clusterSet.forEach(n -> System.out.println(n.size()));

    return clusterSet;
  }

  private static Set<BitSet> findChange(Set<BitSet> bitSet, BitSet bit) {
    Set<BitSet> clusterSet = new HashSet<>();

    if(bitSet.contains(bit)) {
      clusterSet.add(bit);
      bitSet.remove(bit);
    }

    for(int i = 0; i < bitNumber; i++) {
      BitSet sb = (BitSet) bit.clone();
      sb.flip(i);
      if(bitSet.contains(sb)) {
        clusterSet.add(sb);
        bitSet.remove(sb);
      }

      for(int n = 1; n < bitNumber; n++) {
        BitSet sb1 = (BitSet) sb.clone();
        sb1.flip(n);
        if(bitSet.contains(sb1)) {
          clusterSet.add(sb1);
          bitSet.remove(sb1);
        }
      }
    }
    System.out.println(clusterSet.size() + " elements found");
    //clusterSet.forEach(n -> printBitSet(n));

    return clusterSet;
  }

  private static Set<Integer> bfs(Set<Integer> bfsSet, Map<Integer, List<BitSet>> bitMap, int element, int distance) {
    System.out.println("bitmap: " + bitMap.keySet());
    System.out.println("key: " + element + " distance: " + distance);
    if(bitMap.containsKey(element) && !bfsSet.contains(element)) {
      bfsSet.add(element);
    }

     if(bitMap.containsKey(element + distance) && !bfsSet.contains(element + distance)) {
       bfsSet.add(element + distance);
       bfs(bfsSet, bitMap, element + distance, distance);
     } 
    System.out.println("bfsset: " + bfsSet);
    return bfsSet;
  }

  private static List<BitSet> readFile(String fileName) {
    List<BitSet> distanceList = new ArrayList<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;
      currentLine = reader.readLine();
      bitNumber = Integer.valueOf(currentLine.split(" ")[1]);

      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;

        String s = currentLine.replace(" ", "");

        BitSet t = stringToBitSet(s);
        distanceList.add(t);
      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return distanceList;
  }

  private static BitSet stringToBitSet(String s) {
    BitSet t = new BitSet(s.length());

    int lastBitIndex = s.length() - 1;

    for (int i = lastBitIndex; i >= 0; i--) {
      if ( s.charAt(i) == '1'){
        t.set(lastBitIndex - i);                            
      }               
    }
    return t;
  }

  private static void printBitSet(BitSet bs) {
    StringBuilder sb = new StringBuilder(bs.length());
    for (int i = bs.length() - 1; i >= 0; i--)
      sb.append(bs.get(i) ? 1 : 0);
    sb.append("\n");
    System.out.print(sb.toString());
  }

  private static int getHammingWeight(BitSet bitSet) {
    int counter = 0;
    for (byte aByte : bitSet.toByteArray()) {
      for (int i = 0; i < 8; i++) {
        counter += ((aByte & (0x01 << i)) >>> i);
      }
    }
    return counter;
  }
}
