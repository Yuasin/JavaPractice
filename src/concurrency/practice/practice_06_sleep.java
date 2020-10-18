package concurrency.practice;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class practice_06_sleep implements Runnable{
    private static int taskCount = 0;
    private final  int Id = taskCount++;

    @Override
    public void run() {
        try {
            int sleepTime = new Random().nextInt(10)+1;
            TimeUnit.SECONDS.sleep(sleepTime);
            System.out.println("Task " + Id+ " sleepTime: " + sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0; i<10; i++){
            exec.execute(new practice_06_sleep());
        }
        exec.shutdown();
    }
}
