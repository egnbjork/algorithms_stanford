public class Job implements Comparable<Job> {
  private Long weight;
  private Long length;
  private boolean compareByRatio;

  public Job(){};
  
  public Job(long weight, long length, boolean compareByRatio) {
    this.weight = weight;
    this.length = length;
    this.compareByRatio = compareByRatio;
  }

  public Long getWeight() {
    return weight;
  }

  public Long getLength() {
    return length;
  }

  public void setWeight(long weight) {
    this.weight = weight;
  }

  public void setLength(long length) {
    this.length = length;
  }

  public void setCompareByRatio(boolean compareByRatio) {
    this.compareByRatio = compareByRatio;
  }

  @Override
  public int compareTo(Job job) {
    Double thisScore;
    Double jobScore;

    if(compareByRatio) {
      thisScore = weight.doubleValue() / length.doubleValue();
      jobScore = job.getWeight().doubleValue() / job.getLength().doubleValue();
    } else {
      thisScore = weight.doubleValue() - length.doubleValue();
      jobScore = job.getWeight().doubleValue() - job.getLength().doubleValue();
    }
      return thisScore.compareTo(jobScore);
  }

  public String toString() {
    return weight + " " + length;
  }
}
