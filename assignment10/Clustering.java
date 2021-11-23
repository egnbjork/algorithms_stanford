import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Clustering {

  private static int nodes;;

  public static void main(String[] args) {
    if(args.length < 2) {
      System.out.println("not enough args are given");
      return;
    }
    String fileName = args[0];
    Integer clusterSize = Integer.valueOf(args[1]);
    System.out.println(clusterSize + " clustering");
    
    TreeSet<Distance> distanceSet = new Clustering().readFile(fileName);

    Integer maxSpacing = cluster(distanceSet, clusterSize);
    System.out.println("max spacing: " + maxSpacing);
  }

  private static Integer cluster(TreeSet<Distance> distanceSet, Integer clusterSize) {
    while(distanceSet.size() > clusterSize) {
      System.out.println(distanceSet.size());
      Distance firstEntry = distanceSet.pollFirst();
      //System.out.println(firstEntry + " polled");
      Iterator<Distance> it = distanceSet.iterator();
      TreeSet<Distance> tempSet = new TreeSet<>();
      while(it.hasNext()) {
        Distance d = it.next();
        //System.out.println(d);
        //System.out.println(distanceSet);
        if(d.node1 == firstEntry.node2) {
          d.node1 = firstEntry.node1;
        } else if(d.node2 == firstEntry.node2) {
          d.node2 = firstEntry.node1;
        }
        tempSet.add(d);
      }
      distanceSet = tempSet;
    }
    System.out.println(distanceSet.size());
    System.out.println(distanceSet);

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
