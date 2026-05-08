package Java;

/**
 * 温度单位转换工具
 * 支持开尔文、摄氏度、华氏度、兰氏度、列氏度之间的相互转换
 * 基于 jinyaoMa 的代码修改
 * 
 * @author SuiMu
 */

public class Temperature {

  public class Unit {
    public static final String Kelvin = " K";
    public static final String Celsius = "°C";
    public static final String Fahrenheit = "°F";
    public static final String Rankine = "°Ra";
    public static final String Reaumur = "°R";
  }

  public class Value {
    private double value;
    private String unit = "";

    public Value(double value) {
      setValue(value);
    }

    public double getValue() {
      return value;
    }

    public void setValue(double value) {
      this.value = value;
    }

    public String getUnit() {
      return unit;
    }

    public void setUnit(String unit) {
      this.unit = unit;
    }

    @Override
    public String toString() {
      return String.format("%.2f%s", value, unit);
    }
  }

  public class Kelvin extends Value {
    public Kelvin(double value) {
      super(value);
      setUnit(Unit.Kelvin);
    }

    public Celsius toCelsius() {
      return new Celsius(getValue() - 273.15d);
    }

    public Fahrenheit toFahrenheit() {
      return new Fahrenheit(getValue() * 1.8d - 459.67d);
    }

    public Rankine toRankine() {
      return new Rankine(getValue() * 1.8d);
    }

    public Reaumur toReaumur() {
      return new Reaumur((getValue() - 273.15d) * 0.8d);
    }
  }

  public class Celsius extends Value {
    public Celsius(double value) {
      super(value);
      setUnit(Unit.Celsius);
    }

    public Kelvin toKelvin() {
      return new Kelvin(getValue() + 273.15d);
    }

    // 修复：正确的摄氏度转华氏度公式
    public Fahrenheit toFahrenheit() {
      return new Fahrenheit(getValue() * 1.8d + 32d);
    }

    public Rankine toRankine() {
      return new Rankine(getValue() * 1.8d + 32d + 459.67d);
    }

    public Reaumur toReaumur() {
      return new Reaumur(getValue() * 0.8d);
    }
  }

  public class Fahrenheit extends Value {
    public Fahrenheit(double value) {
      super(value);
      setUnit(Unit.Fahrenheit);
    }

    public Kelvin toKelvin() {
      return new Kelvin((getValue() + 459.67d) / 1.8d);
    }

    public Celsius toCelsius() {
      return new Celsius((getValue() - 32d) / 1.8d);
    }

    public Rankine toRankine() {
      return new Rankine(getValue() + 459.67d);
    }

    public Reaumur toReaumur() {
      return new Reaumur((getValue() - 32d) / 2.25d);
    }
  }

  public class Rankine extends Value {
    public Rankine(double value) {
      super(value);
      setUnit(Unit.Rankine);
    }

    // 修复：正确的兰氏度转开尔文公式
    public Kelvin toKelvin() {
      return new Kelvin(getValue() / 1.8d);
    }

    public Celsius toCelsius() {
      return new Celsius((getValue() - 32d - 459.67d) / 1.8d);
    }

    public Fahrenheit toFahrenheit() {
      return new Fahrenheit(getValue() - 459.67d);
    }

    public Reaumur toReaumur() {
      return new Reaumur((getValue() - 459.67d - 32d) / 2.25d);
    }
  }

  public class Reaumur extends Value {
    public Reaumur(double value) {
      super(value);
      setUnit(Unit.Reaumur);
    }

    public Kelvin toKelvin() {
      return new Kelvin(getValue() * 1.25d + 273.15d);
    }

    public Celsius toCelsius() {
      return new Celsius(getValue() * 1.25d);
    }

    public Fahrenheit toFahrenheit() {
      return new Fahrenheit(getValue() * 2.25d + 32d);
    }

    public Rankine toRankine() {
      return new Rankine(getValue() * 2.25d + 32d + 459.67d);
    }
  }
}