public class Job {
  private long weight;
  private long length;

  public Job(){};
  
  public Job(long weight, long length) {
    this.weight = weight;
    this.length = length;
  }

  public long getWeight() {
    return weight;
  }

  public long getLength() {
    return length;
  }

  public void setWeight(long weight) {
    this.weight = weight;
  }

  public void setLength(long length) {
    this.length = length;
  }

  public String toString() {
    return weight + " " + length;
  }
}
