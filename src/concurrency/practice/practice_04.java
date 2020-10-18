package concurrency.practice;

import net.mindview.util.Generator;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class practice_04 implements Runnable{

    private int n;
    private static int taskCount = 0;
    private final  int Id = taskCount++;

    public practice_04(int n){
        this.n = n;
        System.out.println("Task "+ Id+ " begin");
    }

    @Override
    public void run() {
        GenFibonacci gen = new GenFibonacci();
        ArrayList<Integer> fibArray = new ArrayList<>();
        for(int i=0; i<n;i++){
            fibArray.add(gen.next());
        }
        System.out.println("Task "+ Id+ " fibonacci: " + fibArray);
    }

    public static void main(String[] args) throws InterruptedException {
        //可缓存线程池
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0; i<10; i++){
            exec.execute(new practice_02_Fibonacci(i));
        }
        //不进行shutdown则main线程无法自动结束
        exec.shutdown();

        exec = Executors.newFixedThreadPool(1);
        for(int i=0; i<10; i++){
            exec.execute(new practice_02_Fibonacci(i));
        }
        exec.shutdown();

        exec = Executors.newSingleThreadExecutor();
        for(int i=0; i<10; i++){
            exec.execute(new practice_02_Fibonacci(i));
        }
        exec.shutdown();

        // 定时线程池
        exec = Executors.newScheduledThreadPool(2);
        for(int i=0; i<10; i++){
            exec.execute(new practice_02_Fibonacci(i));
        }
        exec.shutdown();
    }
}
