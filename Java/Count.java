package Java;

/**
 * 累加字符串里元音字母 A、E、I、O、U 的数量
 * Examples:
 * Count.vowels("Hello World") => 3
 * 基于 jinyaoMa 的代码修改
 * 
 * @author SuiMu
 */

public class Count {

  public static int vowels(String s) {
    int count = 0;
    String ns = s.toUpperCase();
    for (int i = 0; i < ns.length(); i++) {
      char c = ns.charAt(i);
      if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
        count += 1;
      }
    }
    return count;
  }
}