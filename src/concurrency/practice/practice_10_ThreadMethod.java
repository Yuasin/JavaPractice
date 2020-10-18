package concurrency.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class practice_10_ThreadMethod {


    public Future<List<Integer>> runTask(int fibNum){
        ExecutorService exec = Executors.newSingleThreadExecutor();

        Future<List<Integer>> result = exec.submit(new Callable<List<Integer>>(){
                @Override
                public List<Integer> call() throws Exception {
                    GenFibonacci gen = new GenFibonacci();
                    ArrayList<Integer> fibArray = new ArrayList<>();
                    for(int i=0; i<fibNum;i++){
                        fibArray.add(gen.next());
                    }
                    return fibArray;
                }
            }
        );
        exec.shutdown();
        return result;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 并行创建任务，总执行时间为任务中耗时最长的那个
        long time = System.currentTimeMillis();
        ArrayList<Future<List<Integer>>> results = new ArrayList<>();
        for(int i=0; i<40; i++)
            results.add(new practice_10_ThreadMethod().runTask(i+5));
        for(Future<List<Integer>> result:results){
            System.out.println(result.get());
        }
        System.out.println("并行计算时间为：" + (System.currentTimeMillis() - time));


        // get()会阻塞，直到任务完成，所以后续任务无法创建
        time = System.currentTimeMillis();
        for(int i=0; i<40; i++)
            System.out.println(new practice_10_ThreadMethod().runTask(i+5).get());
        System.out.println("非并行计算时间为：" + (System.currentTimeMillis() - time));

    }
}
