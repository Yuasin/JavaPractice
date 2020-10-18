package concurrency.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class practice_18_interrupt {
    class NoTask{
        public void sleepLongTime() throws InterruptedException {
            while (true){
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Sleep Zzzz...");
            }
        }
    }

    class Task implements Runnable{
        @Override
        public void run(){
            try {
                new NoTask().sleepLongTime();
            } catch (InterruptedException e) {
                System.out.println("Task Interrupted");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        ExecutorService exec = Executors.newCachedThreadPool();
//        Future<?> task = exec.submit(new practice_18_interrupt().new Task());
//        exec.shutdown();
//        System.out.println("Executor shutdown");
//        TimeUnit.SECONDS.sleep(5);
//        task.cancel(true);

        Thread thread = new Thread(new practice_18_interrupt().new Task());
        thread.start();
        TimeUnit.SECONDS.sleep(3);
        thread.interrupt();
    }
}
