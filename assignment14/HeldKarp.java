import java.util.*;

public class HeldKarp {

  private static String string = "ABC";

  public static void main(String[] args) {
    System.out.println(permute(string));
  }

  public static Set<String> permute(String string) {
    Set<String> result = new HashSet<String>();

    for (int i = 0; i < string.length(); i++) {
      char ch = string.charAt(i);
      System.out.println(ch);
      String variationString = string.substring(0, i) + string.substring(i + 1) + ch;
      result.add(variationString);
      for (int n = 0; n < (string.length() - i); n++) {
        char secChar = variationString.charAt(n);
        String permutedString = variationString.substring(0, n) + variationString.substring(n + 1) + secChar;
        System.out.println(permutedString);
        result.add(permutedString);
      }
    }

    return result;
  }

}
