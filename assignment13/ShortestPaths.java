import java.io.*;
import java.util.*;

public class ShortestPaths {

    private static int edgeCount;
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide file name");
            return;
        }
        Map<Integer, List<Vertice>> map = readFile(args[0]);
        System.out.println(map);

        List<List<Long>> allShortestPaths = calcAll(map);
        System.out.println(findMin(allShortestPaths));
    }

    private static Long findMin(List<List<Long>> list) {
        PriorityQueue<Long> pq = new PriorityQueue<>();
        for (List<Long> a : list) {
            for (Long b : a) {
                pq.add(b);
            }
        }
        return pq.peek();
    }

    private static List<List<Long>> calcAll(Map<Integer, List<Vertice>> map) {
        List<List<Long>> allShortestPaths = new ArrayList<>();
        for (int i = 1; i <= edgeCount; i++) {
            List<Long> list = calc(map, i);
            if (!list.isEmpty()) {
                allShortestPaths.add(list);
            }
        }
        return allShortestPaths;
    }

    private static List<Long> calc(Map<Integer, List<Vertice>> map, int startingPoint) {
        List<Long> list = new ArrayList<>();
        //init
        for (int i = 0; i <= edgeCount; i++) {
            if (startingPoint == i) {
                list.add(Long.valueOf(0));
            } else {
                list.add(Long.MAX_VALUE);
            }

        }

        list.set(0, Long.MIN_VALUE);
        for (int p = 0; p <= edgeCount; p++) {
            bellmanFord(map, list, startingPoint);
            if (p == edgeCount && doesHaveNegativeCycle(map, list)) {
                System.out.println("Negative Cycle found!!! starting point " + startingPoint + " of " + edgeCount);
                return new ArrayList<>();
            } else if (p == 0) {
                System.out.println(startingPoint + " of " + edgeCount);
            }
        }

        list.remove(0);
        return list;
    }

    public static boolean doesHaveNegativeCycle(Map<Integer, List<Vertice>> map, List<Long> list) {
        List<Long> list1 = new ArrayList<>(list);
        bellmanFord(map, list1, 1);
        return !list1.equals(list);
    }

    public static void bellmanFord(Map<Integer, List<Vertice>> map, List<Long> list, int startingPoint) {
           for (int i = 1; i <= edgeCount; i++) {
                if (i != startingPoint) {
                    for (Vertice v : map.get(i)) {
                        if (list.get(v.head) != Long.MAX_VALUE) {
                            Long path = list.get(v.head) == Long.MAX_VALUE ? v.length : list.get(v.head) + v.length;
                            if (list.get(i) > path) {
                                list.set(i, path);
                            }
                        }
                    }
                }
            }
    }

    private static Map<Integer, List<Vertice>> readFile(String filename) {
        Map<Integer, List<Vertice>> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            edgeCount = Integer.valueOf(line.split(" ")[0]);
            System.out.println("total " + edgeCount + " edges");
            while ((line = br.readLine()) != null) {
                String[] parsedLine = line.split(" ");
                Vertice v = new ShortestPaths().new Vertice(Integer.valueOf(parsedLine[0]),
                        Integer.valueOf(parsedLine[1]), Long.valueOf(parsedLine[2]));
                if(map.containsKey(v.tail)) {
                    map.get(v.tail).add(v);
                } else {
                    List<Vertice> list = new ArrayList<>();
                    list.add(v);
                    map.put(v.tail, list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private class Vertice {
        private Integer head;
        private Integer tail;
        private Long length;

        Vertice(int head, int tail, long length) {
            this.head = head;
            this.tail = tail;
            this.length = length;
        }

        @Override
        public int hashCode() {
            return this.head.hashCode() + this.tail.hashCode() + this.length.hashCode();
        }
    
        @Override
        public boolean equals(Object that) {
            if (that instanceof Vertice) {
                Vertice vertice = (Vertice) that;
                return this.head.equals(vertice.head) && this.tail.equals(vertice.tail)
                        && this.length.equals(vertice.length);
            }
            return false;
        }
        
        @Override
        public String toString() {
            return head + " " + tail + " " + length;
        }
    }
}
