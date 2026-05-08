package Java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * 纯 Java 实现的 HTTP 异步请求工具（Ajax）
 *
 * 功能：封装了 java.net.HttpURLConnection，用于发送 HTTP 请求
 * 支持 GET/POST 方法、自定义请求头、超时控制、成功/失败回调
 *
 * 使用方式：
 * 1. 实现 Ajax.Options 接口，指定 url、method、headers、data、timeout、success、error
 * 2. 调用 Ajax.run(options) 发起请求
 * 3. 请求完成后自动调用 success 或 error 回调
 * 基于 jinyaoMa 的代码修改
 * 
 * @author SuiMu
 */

public class Ajax {

  public static final String METHOD_GET = "GET";

  public static final String METHOD_POST = "POST";

  public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";

  public static final int READ_TIMEOUT_MULTIPLE = 2;

  public interface Options {

    String url();

    default String method() {
      return METHOD_GET;
    };

    default HashMap<String, String> headers(HashMap<String, String> headers) {
      return headers;
    };

    default HashMap<String, String> data(HashMap<String, String> data) {
      return data;
    };

    default int timeout() {
      return 15000;
    };

    void success(String string);

    void error(int code);
  }

  public static void run(Options options) {
    try {
      // Set URL
      URL url = new URL(options.url());
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // Set Method
      String method = options.method();
      connection.setRequestMethod(method);
      if (method.toUpperCase().equals(METHOD_POST)) {
        connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
      }

      // Set User Agent
      connection.setRequestProperty("User-Agent", USER_AGENT);

      // Set Headers
      HashMap<String, String> headers = options.headers(new HashMap<String, String>());
      if (headers != null && headers.size() > 0) {
        for (String key : headers.keySet()) {
          connection.setRequestProperty(key, headers.get(key));
        }
      }

      // Set data
      HashMap<String, String> data = options.data(new HashMap<String, String>());
      if (data != null && data.size() > 0) {
        StringBuffer databBuffer = new StringBuffer();
        for (String key : data.keySet()) {
          databBuffer.append(key).append("=").append(data.get(key)).append("&");
        }
        int extraAndSymbolIndex = databBuffer.lastIndexOf("&");
        if (extraAndSymbolIndex > 0) {
          databBuffer.deleteCharAt(extraAndSymbolIndex);
        }
        connection.setDoOutput(true);
        connection.getOutputStream().write(databBuffer.toString().getBytes());
      }

      // Set timeout
      connection.setConnectTimeout(options.timeout());
      connection.setReadTimeout(options.timeout() * READ_TIMEOUT_MULTIPLE);

      // Request
      connection.connect();
      int responseCode = connection.getResponseCode();
      if (responseCode < 400) {
        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuffer resultBuffer = new StringBuffer();
        String temp = null;
        while ((temp = bufferedReader.readLine()) != null) {
          resultBuffer.append(temp);
          resultBuffer.append("\r\n");
        }
        options.success(resultBuffer.toString());
        bufferedReader.close();
      } else {
        options.error(responseCode);
      }
      connection.disconnect();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}