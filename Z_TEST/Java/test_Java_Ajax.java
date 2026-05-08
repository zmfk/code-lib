package Z_TEST.Java;
import java.util.HashMap;

import Java.Ajax;
import Java.Ajax.Options;

public class test_Java_Ajax {
  public static void main(String[] args) {
    Ajax.run(new Ajax.Options() {

      @Override
      public String url() {
        return "https://zmfk.github.io/";//用户输入的URL,会返回网站的HTML代码
      }

      @Override
      public String method() {
        return Options.super.method();
      }

      @Override
      public HashMap<String, String> headers(HashMap<String, String> headers) {
        return Options.super.headers(headers);
      }

      @Override
      public HashMap<String, String> data(HashMap<String, String> data) {
        return Options.super.data(data);
      }

      @Override
      public int timeout() {
        return Options.super.timeout();
      }

      @Override
      public void success(String string) {
        System.out.print(string);
      }

      @Override
      public void error(int code) {
        System.out.print(code);
      }
    });
  }
}