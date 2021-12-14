import java.io.*;
import java.util.*;
import java.util.stream.*;


public class BigClustering {

  private static int nodes;

  public static void main(String[] args) {
    if(args.length < 2) {
      System.out.println("not enough args are given");
      return;
    }
    String fileName = args[0];
    Integer clusterSize = Integer.valueOf(args[1]);
    System.out.println(clusterSize + " clustering");

    Map<Integer, List<BitSet>> distanceMap = new BigClustering().readFile(fileName);
    //printBitSetMap(distanceMap);
    System.out.println(distanceMap.size());
    distanceMap.entrySet().stream().forEach(n -> System.out.println("key: " + n.getKey() + ", size: " + n.getValue().size()));

    Map<Integer, List<BitSet>> cluster = cluster(distanceMap, clusterSize);
    cluster.entrySet().stream().forEach(n -> System.out.println("key: " + n.getKey() + ", size: " + n.getValue().size()));
    //Integer maxSpacing = cluster(distanceSet, clusterSize);
    //System.out.println("max spacing: " + maxSpacing);
  }


  private static Map<Integer, List<BitSet>> cluster(Map<Integer, List<BitSet>> bitMap, int clusterSize) {
    Map<Integer, List<BitSet>> clusterMap = new TreeMap<>();
    for(int i = 0; i <= clusterSize; i++) {
      System.out.print("distance: " + i + " ");
      Set<Integer> bfsSet = new TreeSet<>();
      if(!bitMap.isEmpty()) {
        //System.out.println(bfs(bfsSet, bitMap, bitMap.entrySet().iterator().next().getKey(), i));
        System.out.println(bfs(bfsSet, bitMap, 1, 2));
      }
      break;
    }

    return clusterMap;
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

  private Map<Integer, List<BitSet>> readFile(String fileName) {
    Map<Integer, List<BitSet>> distanceMap = new TreeMap<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;
      currentLine = reader.readLine();
      //System.out.println(currentLine);

      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;

        String s = currentLine.replace(" ", "");
        //System.out.println(s);

        BitSet t = stringToBitSet(s);
        //printBitSet(t);
        //System.out.println("");

        int hammingWeight = getHammingWeight(t);
        //System.out.println("weight is " + hammingWeight);
        if(distanceMap.containsKey(hammingWeight)) {
          distanceMap.get(hammingWeight).add(t);
        } else {
          List<BitSet> weightList = new ArrayList<>();
          weightList.add(t);
          distanceMap.put(hammingWeight, weightList);
        }
      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return distanceMap;
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

  private static void printBitSetMap(Map<Integer, List<BitSet>> bitSetMap) {
    for(Map.Entry<Integer, List<BitSet>> bitSet : bitSetMap.entrySet()) {
      System.out.print(bitSet.getKey());
      System.out.print("= {");
      bitSet.getValue().forEach(n -> { 
        printBitSet(n);
        System.out.print(", ");
      });
      System.out.print("} ");
    }
    System.out.println("");
  }

  private static void printBitSet(BitSet bs) {
    StringBuilder sb = new StringBuilder(bs.length());
    for (int i = bs.length() - 1; i >= 0; i--)
      sb.append(bs.get(i) ? 1 : 0);
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
