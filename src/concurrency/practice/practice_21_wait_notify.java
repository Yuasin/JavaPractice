package concurrency.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class practice_21_wait_notify {

    class waitTask implements Runnable{
        @Override
        public void run() {
            synchronized (this){
                try {
                    System.out.println("waitTask begin wait");
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("End of wait");
            }
        }
    }

    class notifyTask implements Runnable{
        public Runnable ref;
        public notifyTask(Runnable ref){
            this.ref = ref;
        }
        @Override
        public void run() {
            synchronized (ref){
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("notify");
                    ref.notifyAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        Runnable ref = new practice_21_wait_notify().new waitTask();
        exec.execute(ref);
        exec.execute(new practice_21_wait_notify().new notifyTask(ref));
        exec.shutdown();
    }
}
