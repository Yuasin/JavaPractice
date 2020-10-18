package concurrency.practice;


import java.util.ArrayList;

public class practice_02_Fibonacci implements Runnable{
    private int n;
    private static int taskCount = 0;
    private final  int Id = taskCount++;

    public practice_02_Fibonacci(int n){
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
        for(int i=0; i<10; i++){
            new Thread(new practice_02_Fibonacci(i)).start();
        }
    }
}
