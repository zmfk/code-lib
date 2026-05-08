package Java;

/**
 * 地球距离计算工具
 * 根据两地的经纬度坐标，计算球面距离（单位：公里）
 * 
 * Examples:
 * A => new Location(49.0002, 51.1000)
 *   => new Location(new Location.Value(49, 0, 0.72), new Location.Value(51, 6, 0))
 * B => new Location(50.0002, 50.1000)
 * Earth.getDistance(A, B)
 * 
 * 基于 jinyaoMa 的代码修改
 * 
 * @author SuiMu
 */

public class Earth {

  public class Location {

    public class Value {
      private long degree;
      private long minute;
      private double second;

      public Value(long degree, long minute, double second) {
        setTo(degree, minute, second);
      }

      public Value(long degree, double minute) {
        setTo(degree, minute);
      }

      public Value(double degree) {
        setTo(degree);
      }

      public void setTo(long degree, long minute, double second) {
        this.degree = degree;
        this.minute = minute;
        this.second = second;
      }

      public void setTo(long degree, double minute) {
        this.degree = degree;
        this.minute = (long) minute;
        this.second = (minute - this.minute) * 60;
      }

      public void setTo(double degree) {
        this.degree = (long) degree;
        double tempMinute = (degree - this.degree) * 60;
        this.minute = (long) tempMinute;
        this.second = (tempMinute - this.minute) * 60;
      }

      public long getDegree() {
        return degree;
      }

      public long getMinute() {
        return minute;
      }

      public double getSecond() {
        return second;
      }

      public double toRadian() {
        return (degree + (minute / 60d) + (second / 3600d)) * Math.PI / 180;
      }

      @Override
      public String toString() {
        return String.format("%d°%d'%.2f\"", degree, minute, second);
      }
    }

    private Value latitude;
    private Value longitude;

    public Location(Value latitude, Value longitude) {
      setLatitude(latitude);
      setLongitude(longitude);
    }

    public Location(double latitude, double longitude) {
      setLatitude(latitude);
      setLongitude(longitude);
    }

    public void setLatitude(double latitude) {
      this.latitude = new Value(latitude);
    }

    public void setLongitude(double longitude) {
      this.longitude = new Value(longitude);
    }

    public void setLatitude(Value latitude) {
      this.latitude = latitude;
    }

    public void setLongitude(Value longitude) {
      this.longitude = longitude;
    }

    public Value getLatitude() {
      return latitude;
    }

    public Value getLongitude() {
      return longitude;
    }
  }

  public static final double Radius = 6371; // in km

  public static double getDistance(Location from, Location to) {
    double lat1 = from.getLatitude().toRadian();
    double long1 = from.getLongitude().toRadian();
    double lat2 = to.getLatitude().toRadian();
    double long2 = to.getLongitude().toRadian();
    return Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(long2 - long1))
        * Radius;
  }
}