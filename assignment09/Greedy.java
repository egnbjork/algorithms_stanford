import java.io.*;
import java.util.*;

public class Greedy {
  public static void main(String[] args) {
    if(args.length < 2) {
      System.out.println("not enough args are given");
      return;
    }

    boolean compareByRatio = Boolean.parseBoolean(args[1]);

    if(compareByRatio) {
      System.out.println("Compare by ratio");
    } else {
      System.out.println("Compare by difference");
    }

    List<Job> jobList = readFile(args[0], compareByRatio);
    Collections.sort(jobList);
    System.out.println(jobList);
    System.out.println(calculateSum(jobList));
  }

  public static long calculateSum(List<Job> jobList) {
    long sum = 0;
    long completionTime  = 0;

    for(Job job : jobList) {
      long weight = job.getWeight(); 
      completionTime += job.getLength();
      System.out.println("job weight: " + weight);
      System.out.println("job length: " + job.getLength());
      System.out.println("job completion time: " + completionTime);
      long score = weight * completionTime;
      sum += score;
      System.out.println("score is " + score);
      System.out.println("sum is " + sum);
    }

    return sum;
  }

  private static List<Job> readFile(String fileName, boolean compareByRatio) {
    List<Job> jobList = new ArrayList<>();

    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      String currentLine;
      currentLine = reader.readLine();
      System.out.println(currentLine + " jobs in the file");

      do {
        currentLine = reader.readLine();
        if(currentLine == null) break;

        String[] job = currentLine.split(" ");
        long weight = Long.valueOf(job[0]);
        long length = Long.valueOf(job[1]);
        jobList.add(new Job(weight, length, compareByRatio));
      } while(currentLine !=  null);
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println(jobList);
    return jobList;
  }
}
