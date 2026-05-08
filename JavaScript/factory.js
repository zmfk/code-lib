/**
 * Factory - 异步任务队列管理器
 * 按顺序执行任务，支持停止和重启。
 * 基于 jinyaoMa 的代码修改
 * 
 * @author SuiMu
 */

class Factory {
  constructor() {
    this.queue = [];
    this.isWorking = false;
    this.forceStop = false;
  }
  add(task) {
    if (task instanceof Factory.Task) {
      this.queue.push(task);
      this.run();
    } else {
      throw new Error('Factory.add: invalid arguments');
    }
  }
  run() {
    this.forceStop = false;
    if (!this.isWorking) {
      this.next();
    }
  }
  next() {
    if (this.queue.length) {
      let task = this.queue.shift();
      task.on(Factory.Task.START, o => {
        this.isWorking = true;
      }).on(Factory.Task.COMPLETE, result => {
        if (result !== undefined && result !== null && !this.forceStop) {
          task.listeners && typeof task.listeners.oncomplete === 'function' && task.listeners.oncomplete(result);
          this.next();
        } else {
          this.isWorking = false;
          this.forceStop = false;
        }
      }).on(Factory.Task.ERROR, error => {
        this.isWorking = false;
        this.forceStop = false;
        task.listeners && typeof task.listeners.onerror === 'function' && task.listeners.onerror(error);
        console.error(error);
      }).do();
    }
  }
  stop() {
    this.forceStop = true;
  }
}
Factory.Task = class {
  constructor(callback, listeners = null) {
    if (typeof callback === 'function') {
      this.callback = callback;
    } else {
      throw new Error('Factory.Tack.constructor: invalid arguments');
    }
    if (listeners != null) {
      this.listeners = listeners;
    }
  }
  on(action, callback) {
    if (typeof action === 'string' && typeof callback === 'function') {
      switch (action.toLowerCase().trim()) {
        case Factory.Task.START:
          this.onstart = callback;
          break;
        case Factory.Task.COMPLETE:
          this.oncomplete = callback;
          break;
        case Factory.Task.ERROR:
          this.onerror = callback;
          break;
      }
    } else {
      throw new Error('Factory.Tack.on: invalid arguments');
    }
    return this;
  }
  do() {
    this.onstart && this.onstart();
    this.callback(this.oncomplete, this.onerror);
  }
}
Factory.Task.START = 'start';
Factory.Task.COMPLETE = 'complete';
Factory.Task.ERROR = 'error';