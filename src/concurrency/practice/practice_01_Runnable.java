package concurrency.practice;

public class practice_01_Runnable implements Runnable{

    private static int taskCount = 0;
    private final int Id = taskCount++;
    public practice_01_Runnable(){
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

    public static void main(String[] args) {
        for(int i=0; i<10; i++){
            new Thread(new practice_01_Runnable()).start();
        }
    }
}
