package Java;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 多线程任务分发器
 * 自动将任务分配给当前任务最少的 Worker 执行。
 * 与 JavaScript 版 Factory 功能对应。
 * 基于 jinyaoMa 的代码修改而来，增加了 Worker 的概念，使得任务分发更加合理。
 * 
 * @author SuiMu
 */

public class Factory<T> {

  public interface Listener<T> {

    void onComplete(Factory<T> factory);

  }

  public static class Task<T> {

    private static long nextId = 0;

    public interface Process<T> {

      void run(Task<T> task, Next<T> next);

      void onResolve(T data);

      void onReject(Exception e);

    }

    public interface Next<T> {

      void resolve(T data);

      void reject(Exception e);
    }

    private long id;

    private Process<T> process;

    private Next<T> next = new Next<T>() {

      @Override
      public void resolve(T data) {
        process.onResolve(data);
      }

      @Override
      public void reject(Exception e) {
        process.onReject(e);
      }
    };

    public Task(Process<T> process) {
      id = nextId++;
      setProcess(process);
    }

    public long getId() {
      return id;
    }

    public void setProcess(Process<T> process) {
      this.process = process;
    }

    public void run() {
      try {
        process.run(this, next);
      } catch (Exception e) {
        process.onReject(e);
      }
    }
  }

  public static class Worker<T> {

    private static long nextId = 0;

    private long id;
    private Queue<Task<T>> tasks;
    private Thread thread = null;
    private Runnable runnable = new Runnable() {

      @Override
      public void run() {
        while (!tasks.isEmpty()) {
          Task<T> task = tasks.poll();
          task.run();
        }
      }
    };

    public Worker() {
      id = nextId++;
      tasks = new LinkedList<Task<T>>();
    }

    public long getId() {
      return id;
    }

    public int getTasksCount() {
      return tasks.size();
    }

    public boolean isWorking() {
      return thread != null && thread.isAlive();
    }

    public void add(Task<T> task) {
      tasks.add(task);
      if (!isWorking()) {
        thread = new Thread(runnable);
        thread.start();
      }
    }
  }

  private final int MIN_WORKER_SIZE = 1; // minimum size - 1 worker
  private ArrayList<Worker<T>> workers;

  public Factory(int size) throws IllegalArgumentException {
    if (size < MIN_WORKER_SIZE) {
      throw new IllegalArgumentException("Minimum size is 1");
    }
    workers = new ArrayList<Worker<T>>(size);
    for (int i = 0; i < size; i++) {
      workers.add(new Worker<T>());
    }
  }

  public Worker<T> getWorkerWithTheLeastTasks() {
    int size = workers.size();
    if (size > 0) {
      int min = Integer.MAX_VALUE;
      int index = 0;
      for (int i = 0; i < size; i++) {
        int count = workers.get(i).getTasksCount();
        if (count < min) {
          min = count;
          index = i;
        }
      }
      return workers.get(index);
    }
    return null;
  }

  public void add(Task<T> task) {
    Worker<T> workerWithTheLeastTasks = getWorkerWithTheLeastTasks();
    workerWithTheLeastTasks.add(task);
  }

  public void add(ArrayList<Task<T>> tasks) {
    for (Task<T> task : tasks) {
      add(task);
    }
  }

  public boolean isEmpty() {
    boolean result = true;
    for (Worker<T> worker : workers) {
      if (worker.isWorking() || worker.getTasksCount() > 0) {
        result = false;
        break;
      }
    }
    return result;
  }
}