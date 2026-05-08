package Z_TEST.Java;
import java.util.ArrayList;

import Java.Factory;
import Java.Factory.Task;
import Java.Factory.Task.Next;

public class test_Java_Factory {
  public static void main(String[] args) throws InterruptedException {
    ArrayList<Task<String>> tasks = new ArrayList<Task<String>>(10);
    for (int i = 0; i < 10; i++) {
      tasks.add(new Task<String>(new Task.Process<String>() {

        @Override
        public void run(Task<String> task, Next<String> next) {
          next.resolve(task.getId() + "");
        }

        @Override
        public void onResolve(String data) {
          System.out.println(data);

        }

        @Override
        public void onReject(Exception e) {
          System.out.println(e.getMessage());
        }
      }));
    }
    Factory<String> factory = new Factory<String>(3);
    for (Task<String> task : tasks) {
      factory.add(task);
    }
    System.out.printf("Factory is empty? %b\n", factory.isEmpty());
    Thread.sleep(5000);
    System.out.printf("Factory is empty? %b\n", factory.isEmpty());
  }
}