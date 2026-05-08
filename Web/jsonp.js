/**
 * Jsonp - JSONP 跨域请求工具
 * 通过动态创建 script 标签实现跨域请求，支持超时处理。
 * 
 * @author SuiMu
 */

const jsonp = (url, params, callback, flush = false, timeout = 30000) => {
  let script = document.createElement('script');
  let data = '';
  let cb = null;
  let result = null;
  let isFinished = false;  // 【优化1】：标志位，请求是否已完成
  let timerId = null;      // 【优化2】：保存定时器 ID，以便清除

  // 拼接请求参数
  if (params) {
    for (const key in params) {
      const value = params[key];
      if (data === '') {
        data += '?' + key + '=' + value;
      } else {
        data += '&' + key + '=' + value;
      }
      if (/(jsonp|callback)/.test(key.toLowerCase())) {
        cb = value;
      }
    }
  }

  // 定义全局回调函数
  if (cb) {
    window[cb] = function (obj) {
      result = obj;
    };
  }

  // 请求成功
  script.onload = function () {
    if (isFinished) return; // 已完成则不再处理
    isFinished = true;
    timerId && clearTimeout(timerId); // 【优化3】：清除超时定时器
    typeof callback === 'function' && callback(result || 'No result');
    flush && script.remove();
  };

  // 请求失败
  script.onerror = function (e) {
    if (isFinished) return;
    isFinished = true;
    timerId && clearTimeout(timerId); // 【优化3】：清除超时定时器
    typeof callback === 'function' && callback(e || new Error('Network error'));
    flush && script.remove();
  };

  // 设置超时处理
  timerId = setTimeout(() => {
    if (isFinished) return; // 如果已经完成，不再触发超时
    isFinished = true;
    typeof callback === 'function' && callback(new Error('Request timeout'));
    flush && script.remove();
  }, timeout);

  // 发起请求
  script.src = url + data;
  document.head.appendChild(script);
};