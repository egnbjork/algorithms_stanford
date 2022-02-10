import java.io.*;
import java.util.*;

public class Knapsack {
  private static int size = 0;

  public static void main(String[] args) {
    System.out.println("Hello, World!");
    if(args.length < 1) {
      System.out.println("not enough args are given");
      return;
    }
    String fileName = args[0];

    Queue<Item> items = readFile(fileName);
    //System.out.println(items);
    System.out.println("=========");
    System.out.println(calc(items));
  }

  private static int calc(Queue<Item> items) {
    int calc = 0;
    List<Integer> prevList = new ArrayList<>();

    while(!items.isEmpty()) {
      System.out.println(items.size());
      Item item = items.poll();
      List<Integer> valueList = new ArrayList<>();
      valueList.add(0);
      //System.out.println(item);
      //System.out.println(items);

      for(int i = 1; i < size; i++) {
        if(prevList.isEmpty()) {
          if(item.getSize() <= i) {
            valueList.add(item.getValue());
          } else {
            valueList.add(0);
          }
        } else if(item.getSize() <= i &&
            (item.getValue() + prevList.get(i - item.getSize())) > prevList.get(i)) {
          //System.out.println("value " + item.getValue() + " size " + item.getSize());
          //System.out.println(item.getValue() + prevList.get(i - item.getSize()));
          valueList.add(prevList.get(i - item.getSize()) + item.getValue());
        } else {
          valueList.add(prevList.get(i));
        }
      }
      prevList = valueList;
    }

    return prevList.get(prevList.size() - 1);
  }

  private static Queue<Item> readFile(String fileName) {
    Queue<Item> itemList = new LinkedList<Item>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;
      currentLine = reader.readLine();
      System.out.println(currentLine.split(" ")[1] + " elements");
      size = Integer.valueOf(currentLine.split(" ")[0]) + 1;
      System.out.println("knapsack size is " + size);

      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;

        String[] item = currentLine.split(" ");
        itemList.offer(new Knapsack().new Item(Integer.valueOf(item[0]), Integer.valueOf(item[1])));

      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return itemList;
  }

    private class Item {
      Integer value;
      Integer size;
      Item(Integer value, Integer size) {
        this.value = value;
        this.size = size;
      }

      @Override
      public String toString() {
        return value + " " + size;
      }

      @Override
      public int hashCode() {
        return this.value.hashCode() + this.size.hashCode();
      }

      @Override
      public boolean equals(Object item) {
        if(item instanceof Item) {
          return this.value.equals(((Item) item).getValue()) &&
            this.size.equals(((Item) item).getSize());
        } else {
          return false;
        }
      }

      Integer getValue() {
        return value;
      }

      Integer getSize() {
        return size;
      } 
    }
}
