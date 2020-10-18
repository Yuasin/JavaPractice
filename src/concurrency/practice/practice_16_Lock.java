package concurrency.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class practice_16_Lock {

    static class Shared{
        private Lock lock = new ReentrantLock();
        // 打印与上个任务执行的时间间隔
        long before = System.currentTimeMillis();

        public void methodX() throws InterruptedException {
            lock.lock();
            System.out.println("methodX：" + (System.currentTimeMillis() - before));
            before = System.currentTimeMillis();
            TimeUnit.SECONDS.sleep(1);
            lock.unlock();
        }
        public void methodY() throws InterruptedException {
            lock.lock();
            System.out.println("methodY：" + (System.currentTimeMillis() - before));
            before = System.currentTimeMillis();
            TimeUnit.SECONDS.sleep(1);
            lock.unlock();
        }
        public void methodZ() throws InterruptedException {
            lock.lock();
            System.out.println("methodZ：" + (System.currentTimeMillis() - before));
            before = System.currentTimeMillis();
            TimeUnit.SECONDS.sleep(1);
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Shared shared = new Shared();
        ExecutorService exec = Executors.newCachedThreadPool();

        // 开启三个线程，执行同一个对象中的不同方法
        exec.execute(new Thread(() -> {
            while (true) {
                try {
                    shared.methodX();
                    Thread.yield();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
        exec.execute(new Thread(() -> {
            while (true) {
                try {
                    shared.methodY();
                    Thread.yield();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
        exec.execute(new Thread(() -> {
            while (true) {
                try {
                    shared.methodZ();
                    Thread.yield();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
        exec.shutdown();

    }
}
