package Z_TEST.Java;
import Java.Earth;

public class test_Java_Earth {
    public static void main(String[] args) {
        Earth earth = new Earth();
        
        // 北京 (39.9042°N, 116.4074°E) 到 上海 (31.2304°N, 121.4737°E)
        Earth.Location beijing = earth.new Location(39.9042, 116.4074);
        Earth.Location shanghai = earth.new Location(31.2304, 121.4737);
        
        double distance = Earth.getDistance(beijing, shanghai);
        System.out.printf("北京到上海的距离约为: %.2f km%n", distance);
        
        // 测试 toString（使用度分秒构造）
        Earth.Location sampleLoc = earth.new Location(
            earth.new Location(0,0).new Value(49, 0, 0.72),
            earth.new Location(0,0).new Value(51, 6, 0)
        );
        System.out.println("示例坐标: " + sampleLoc.getLatitude() + ", " + sampleLoc.getLongitude());
    }
}