package Java;

/**
 * 三角形判定工具
 * 根据三边长度判断能否构成三角形、是否为直角三角形。
 * 基于 jinyaoMa 的代码修改,增加了边长合法性检查，使得功能更加健壮。
 * 
 * @author SuiMu
 */

public class Triangle {

  private int sideA, sideB, sideC;

  /**
   * 输入三条边
   *
   * @param a 第一条边
   * @param b 第二条边
   * @param c 第三条边
   */
  public Triangle(int a, int b, int c) {
    sideA = a;
    sideB = b;
    sideC = c;
  }

  /**
   * 检查三条边是否都大于0
   *
   * @return 合法返回 true，否则 false
   */
  public boolean isValid() {
    return sideA > 0 && sideB > 0 && sideC > 0;
  }

  /**
   * 判断能否构成三角形
   * 先检查边长合法性，若不合法返回 false
   *
   * @return 能构成三角形返回 true，否则 false
   */
  public boolean isTriangle() {
    if (!isValid()) {
      return false;
    }
    return longestSide() < (shortestSide() + mediumSide());
  }

  /**
   * 判断是否为直角三角形
   * 先检查边长合法性，若不合法返回 false
   *
   * @return 是直角三角形返回 true，否则 false
   */
  public boolean isRight() {
    if (!isValid()) {
      return false;
    }
    return longestSide() * longestSide() == shortestSide() * shortestSide() + mediumSide() * mediumSide();
  }

  /**
   * @return 第一条边
   */
  public int getA() {
    return sideA;
  }

  /**
   * @return 第二条边
   */
  public int getB() {
    return sideB;
  }

  /**
   * @return 第三条边
   */
  public int getC() {
    return sideC;
  }

  /**
   * @return 最短边
   */
  private int shortestSide() {
    return Math.min(Math.min(sideA, sideB), sideC);
  }

  /**
   * @return 最长边
   */
  private int longestSide() {
    return Math.max(Math.max(sideA, sideB), sideC);
  }

  /**
   * @return 中等长度边
   */
  private int mediumSide() {
    return (sideA + sideB + sideC) - shortestSide() - longestSide();
  }
}