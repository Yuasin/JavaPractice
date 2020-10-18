package concurrency.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class PriorityThreadFactory implements ThreadFactory{
    int priority;
    public PriorityThreadFactory(int priority){
        this.priority = priority;
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setPriority(priority);
        return thread;
    }
}

public class practice_09_ThreadFactory_Priority implements Runnable{
    private int countDown = 5;
    private volatile double d; // 防止编译器优化、乱序执行

    @Override
    public String toString() {
        return Thread.currentThread() + ": " +countDown;
    }

    @Override
    public void run() {
        while(true){
            for(int i=1; i<100000000; i++){
                d += (Math.PI + Math.E) / (double)i;
                if(i % 1000 == 0)
                    Thread.yield();
            }
            System.out.println(this);
            if(--countDown==0)return;
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool(new PriorityThreadFactory(Thread.MIN_PRIORITY));
        for(int i=0; i<5; i++){
            exec.execute(new practice_09_ThreadFactory_Priority());
        }
        exec.shutdown();
        exec = Executors.newCachedThreadPool(new PriorityThreadFactory(Thread.MAX_PRIORITY));
        exec.execute(new practice_09_ThreadFactory_Priority());
        exec.shutdown();


    }
}
