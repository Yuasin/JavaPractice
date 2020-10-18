package concurrency.practice;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class practice_03 implements Runnable{
    private static int taskCount = 0;
    private final int Id = taskCount++;
    public practice_03(){
        System.out.println("Thread "+ Id+ " begin");
    }

    @Override
    public void run(){
        for(int i=0; i<3; i++){
            System.out.println("Task"+ Id +" message: "+i);
            Thread.yield();
        }
        System.out.println("Task "+ Id +" end");
    }

    // 使用四种线程池执行任务
    public static void main(String[] args) {
        //可缓存线程池
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0; i<5; i++){
            exec.execute(new practice_03());
        }
        //不进行shutdown则main线程无法自动结束
        exec.shutdown();

        exec = Executors.newFixedThreadPool(1);
        for(int i=0; i<5; i++){
            exec.execute(new practice_03());
        }
        exec.shutdown();

        exec = Executors.newSingleThreadExecutor();
        for(int i=0; i<5; i++){
            exec.execute(new practice_03());
        }
        exec.shutdown();

        // 定时线程池
        exec = Executors.newScheduledThreadPool(2);
        for(int i=0; i<5; i++){
            exec.execute(new practice_03());
        }
        exec.shutdown();
    }
}
