package concurrency.practice;

import java.util.Random;
import java.util.concurrent.*;

class Sender implements Runnable{
    private BlockingQueue<Character> queue;
    private Random random = new Random(47);
    public Sender(BlockingQueue<Character> queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                for(char c = 'A'; c<= 'z'; c++){
                    queue.put(c);
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(500));
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e + " Sender sleep interrupt");
        }
    }
}

class Receiver implements Runnable{
    private BlockingQueue<Character> queue;
    private Random random = new Random(47);
    public Receiver(BlockingQueue<Character> queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                System.out.println("Read: " + queue.take()+ ",");
            }
        } catch (InterruptedException e) {
            System.out.println(e + " Receiver sleep interrupt");
        }
    }
}

public class practice_30_PipedIO_BlockingQueue {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Character> queue = new LinkedBlockingQueue<>();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Sender(queue));
        exec.execute(new Receiver(queue));
        TimeUnit.SECONDS.sleep(4);
        exec.shutdownNow();
    }
}
