import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Clustering {

  private static int nodes;

  public static void main(String[] args) {
    if(args.length < 2) {
      System.out.println("not enough args are given");
      return;
    }
    String fileName = args[0];
    Integer clusterSize = Integer.valueOf(args[1]);
    System.out.println(clusterSize + " clustering");
    
    TreeSet<Distance> distanceSet = new Clustering().readFile(fileName);
    System.out.println(distanceSet.size());

    Integer maxSpacing = cluster(distanceSet, clusterSize);
    System.out.println("max spacing: " + maxSpacing);
  }

  private static Integer cluster(TreeSet<Distance> distanceSet, Integer clusterSize) {

    List<Set<Integer>> clusterList = new ArrayList<>();
    for(int i = 1; i <= nodes; i++) {
      Set<Integer> node = new TreeSet<>();
      node.add(i);
      clusterList.add(node);
    } 

    while(clusterList.size() > clusterSize) {
      Distance nextEntry = distanceSet.pollFirst();
      System.out.println(nextEntry);
      for (int i = 0; i < clusterList.size(); i++) {
        Set<Integer> clusterNode = clusterList.get(i);
        if (clusterNode.contains(nextEntry.node1)) {
          //System.out.println("collection to merge");
          Set<Integer> mergedCollection = clusterList.stream().filter(n -> n.contains(nextEntry.node1)).flatMap(p -> p.stream()).collect(Collectors.toSet());
          Set<Integer> mergedCollection1 = clusterList.stream().filter(n -> n.contains(nextEntry.node2)).flatMap(p -> p.stream()).collect(Collectors.toSet());
          //System.out.print(mergedCollection);
          //clusterList.stream().filter(n -> n.contains(nextEntry.node1)).forEach(p -> System.out.print(p));
          //System.out.println(" and " + mergedCollection1);
          //System.out.println("---");
          //clusterNode.add(nextEntry.node2);
          mergedCollection.addAll(mergedCollection1);
          clusterList.removeIf(n -> n.contains(nextEntry.node1) || n.contains(nextEntry.node2));
          clusterList.add(mergedCollection);
        } 
      }
        System.out.println(clusterList.size());
        //System.out.println(distanceSet);

      if(clusterList.size() == clusterSize) {
        //System.out.println("before");
        //System.out.println(distanceSet);
        distanceSet.removeIf(n -> clusterList.stream().filter(p -> p.contains(n.node1) && p.contains(n.node2)).findAny().isPresent());
        //System.out.println("after");
        //System.out.println(distanceSet);
      }
    }

    return distanceSet.first().distance;
  }

  private TreeSet<Distance> readFile(String fileName) {
    TreeSet<Distance> distanceSet = new TreeSet<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;
      currentLine = reader.readLine();
      nodes = Integer.valueOf(currentLine);
      System.out.println(currentLine + " nodes in the file");

      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;

        String[] job = currentLine.split(" ");
        Integer edge1 = Integer.valueOf(job[0]);
        Integer edge2 = Integer.valueOf(job[1]);
        Integer cost = Integer.valueOf(job[2]);
        
        Distance distance = new Distance(edge1, edge2, cost);
        distanceSet.add(distance);
        
      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return distanceSet;
  }

  private class Distance implements Comparable<Distance> {
    int node1;
    int node2;
    int distance;

    Distance(int node1, int node2, int distance) {
      this.node1 = node1;
      this.node2 = node2;
      this.distance = distance;
    }

    @Override
    public int compareTo(Distance other) {
      if(this.node1 == other.node1 && this.node2 == other.node2 ||
          this.node1 == other.node2 && this.node2 == other.node1) {
        return 0;
      }
      if(this.distance == other.distance) {
        if(this.node1 == other.node1) {
          return this.node2 - other.node2;
        }
        return this.node1 - other.node1;
      }
      return this.distance - other.distance;
    }

    @Override
    public int hashCode() { 
      return node1*node2*distance;
    }

    @Override
    public boolean equals(Object that) {
      if(((Distance)that).node1 == this.node1 && ((Distance)that).node2 == this.node2 || 
         ((Distance)that).node2 == this.node1 && ((Distance)that).node1 == this.node2) {
        return true;
      }
      
      return false;
    }

    @Override
    public String toString() {
      return distance + ": " + node1 + "," + node2;
    }
  }
}
