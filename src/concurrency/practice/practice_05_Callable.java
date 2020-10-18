package concurrency.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class practice_05_Callable implements Callable<List<Integer>> {
    private int n;
    private static int taskCount = 0;
    private final  int Id = taskCount++;

    public practice_05_Callable(int fibPar){
        this.n = fibPar;
    }
    @Override
    public List<Integer> call() throws Exception {
        GenFibonacci gen = new GenFibonacci();
        ArrayList<Integer> fibArray = new ArrayList<>();
        for(int i=0; i<n;i++){
            fibArray.add(gen.next());
        }
        return fibArray;
    }

    // Callable 的返回结果需要使用Future类来进行接收
    // 使用执行器 Executor 的submit运行一个Callable会返回一个Future
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<List<Integer>>> callBacks = new ArrayList<>();

        for(int i=0; i<10; i++){
            callBacks.add(exec.submit(new practice_05_Callable(i)));
        }

        //get()方法为阻塞方法，任务完成之后才能获得结果，可以设置等待时间，超时则不再进行等待
        for(Future<List<Integer>> callBack:callBacks){
            System.out.println(callBack.get());
        }

        exec.shutdown();
    }
}
