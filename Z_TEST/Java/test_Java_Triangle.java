package Z_TEST.Java;
import Java.Triangle;

public class test_Java_Triangle {
    public static void main(String[] args) {
        // 合法的直角三角形
        Triangle t1 = new Triangle(3, 4, 5);
        System.out.println("(3,4,5) 边长合法? " + t1.isValid());
        System.out.println("能构成三角形? " + t1.isTriangle());
        System.out.println("是直角三角形? " + t1.isRight());
        System.out.println("边长: A=" + t1.getA() + " B=" + t1.getB() + " C=" + t1.getC());

        System.out.println("---");

        // 包含非法边长
        Triangle t2 = new Triangle(-1, 0, 5);
        System.out.println("(-1,0,5) 边长合法? " + t2.isValid());
        System.out.println("能构成三角形? " + t2.isTriangle());
        System.out.println("是直角三角形? " + t2.isRight());

        System.out.println("---");

        // 合法但不能构成三角形
        Triangle t3 = new Triangle(1, 2, 4);
        System.out.println("(1,2,4) 边长合法? " + t3.isValid());
        System.out.println("能构成三角形? " + t3.isTriangle());

        System.out.println("---");

        // 等边三角形
        Triangle t4 = new Triangle(7, 7, 7);
        System.out.println("(7,7,7) 边长合法? " + t4.isValid());
        System.out.println("能构成三角形? " + t4.isTriangle());
        System.out.println("是直角三角形? " + t4.isRight());
    }
}