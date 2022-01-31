import java.io.*;
import java.util.*;
import java.util.stream.*;


public class BigClustering {

  private static int bitNumber;

  public static void main(String[] args) {
    if(args.length < 1) {
      System.out.println("not enough args are given");
      return;
    }
    String fileName = args[0];

    List<BitSet> distanceList = BigClustering.readFile(fileName);

    System.out.println(cluster(distanceList).size());
  }


  private static Set<Set<BitSet>> cluster(List<BitSet> bitList) {
    Set<Set<BitSet>> clusterSet = new LinkedHashSet<>();
    Set<BitSet> bitSet = new LinkedHashSet<>(bitList);
    System.out.println(bitList.size() + " values found");
    System.out.println((bitSet.size()) + " 0-distance clustering");

    for(BitSet bit : new LinkedHashSet<>(bitList)) {
      if(bitSet.contains(bit)) {
        clusterSet.add(findOneChange(bitSet, bit));
      }
    }
    System.out.println(clusterSet.size() + " 1-distance and 2-distance clustering");
    clusterSet.forEach(n -> {n.forEach(p -> System.out.print(bitSetToString(p))); System.out.print("| ");});
    System.out.println("");

    return clusterSet;
  }

  private static Set<BitSet> findOneChange(Set<BitSet> bitSet, BitSet bit) {
    Set<BitSet> clusterSet = new LinkedHashSet<>();

    if(bitSet.contains(bit)) {
      clusterSet.add(bit);
      bitSet.remove(bit);
    }

    for(int i = 0; i <= bitNumber; i++) {
      BitSet sb = (BitSet) bit.clone();
      sb.flip(i);

      if(bitSet.contains(sb)) {
        clusterSet.add(sb);
        bitSet.remove(sb);
        System.out.println("1 distance found");
      }
      for(int n = 0; n <= bitNumber; n++) {
        if(n != i) {
          BitSet sb1 = (BitSet) sb.clone(); 
          sb1.flip(n);
          if(bitSet.contains(sb1)) {
            clusterSet.add(sb1);
            bitSet.remove(sb1);
            System.out.println("2 distance found");
          }
        }
      }

    }

    return clusterSet;
  }

  private static Set<Set<BitSet>> findTwoChanges(Set<Set<BitSet>> bitSet, BitSet bit) {
    Set<Set<BitSet>> newBitSet = new LinkedHashSet<>();
    for(int i = 0; i < bitNumber; i++) {
      BitSet sb = (BitSet) bit.clone();
      sb.flip(i);
      for(int n = 0; n < bitNumber; n++) {
        if(n != i) {
          sb.flip(n);
          for(Set<BitSet> bs : bitSet) {
            if(bitSet.contains(sb)) {
              //clusterSet.add(sb);
              bs.remove(sb);
            }
          }
        }
      }
    }
    return new HashSet<>();
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

  private static String bitSetToString(BitSet bs) {
    StringBuilder sb = new StringBuilder(bs.length());
    for (int i = bitNumber - 1; i >= 0; i--)
      sb.append(bs.get(i) ? 1 : 0);
    sb.append(" ");
    return sb.toString();
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
