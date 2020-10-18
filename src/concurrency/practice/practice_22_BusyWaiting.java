package concurrency.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class practice_22_BusyWaiting {
    // 必须加volatile，因为stopWait域在两个不同的线程（任务）中被使用，
    // 一个线程修改stopWait时修改值只存在了缓存中，需要使用volatile将修改值刷到主存中
    volatile boolean stopWait = false;

    class SleepTask implements Runnable{
        @Override
        public void run(){
            try {
                TimeUnit.SECONDS.sleep(2);
                stopWait = true;
                System.out.println("stopWait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class BusyWaiting implements Runnable{
        @Override
        public void run() {
            long time = System.currentTimeMillis();
            while (!stopWait){

            }
            stopWait = false;
            System.out.println("BusyWaiting Time: " + (System.currentTimeMillis()-time));
        }
    }

    class WaitTask implements Runnable{
        @Override
        public void run() {

        }
    }

    public static void main(String[] args) {
        practice_22_BusyWaiting t = new practice_22_BusyWaiting();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(t.new SleepTask());
        exec.execute(t.new BusyWaiting());
        exec.shutdown();
    }
}
