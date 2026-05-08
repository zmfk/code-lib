package Z_TEST.Java;
import Java.Temperature;

public class test_Java_Temperature {
    public static void main(String[] args) {
        // 创建不同温标的实例
        Temperature temp = new Temperature();
        Temperature.Celsius celsius = temp.new Celsius(100);
        Temperature.Fahrenheit fahrenheit = temp.new Fahrenheit(212);
        Temperature.Kelvin kelvin = temp.new Kelvin(373.15);
        Temperature.Rankine rankine = temp.new Rankine(671.67);
        Temperature.Reaumur reaumur = temp.new Reaumur(80);

        // 打印转换结果
        System.out.println("摄氏度 100 -> 华氏度: " + celsius.toFahrenheit());
        System.out.println("摄氏度 100 -> 开尔文: " + celsius.toKelvin());

        System.out.println("华氏度 212 -> 摄氏度: " + fahrenheit.toCelsius());
        System.out.println("华氏度 212 -> 开尔文: " + fahrenheit.toKelvin());

        System.out.println("开尔文 373.15 -> 摄氏度: " + kelvin.toCelsius());
        System.out.println("开尔文 373.15 -> 华氏度: " + kelvin.toFahrenheit());

        System.out.println("兰氏度 671.67 -> 开尔文: " + rankine.toKelvin());
        System.out.println("兰氏度 671.67 -> 华氏度: " + rankine.toFahrenheit());

        System.out.println("列氏度 80 -> 摄氏度: " + reaumur.toCelsius());
        System.out.println("列氏度 80 -> 华氏度: " + reaumur.toFahrenheit());
    }
}