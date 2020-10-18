package concurrency.practice;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

class Chopstick {
    final int id;
    public Chopstick(int id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "Chopstick: " +
                 + id;
    }
}

class ChopstickBin extends LinkedBlockingDeque<Chopstick>{}

class Philosopher implements Runnable{
    ChopstickBin cb;
    private Chopstick left;
    private Chopstick right;
    private final int id;
    private final int ponderFactor;
    private Random rand = new Random(47);
    private void pause() throws InterruptedException {
        if(ponderFactor == 0) return;
        TimeUnit.MILLISECONDS.sleep(
                rand.nextInt(ponderFactor * 250));
    }
    public Philosopher(ChopstickBin cb, int ident, int ponder) {
        this.cb = cb;
        id = ident;
        ponderFactor = ponder;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                System.out.println(this + "Thinking");
                pause();
                left = cb.take();
                System.out.println(this + "has " + left + " waiting for another one");
                right = cb.take();
                System.out.println(this + "has " + right);
                System.out.println(this + "Eating");
                pause();
                cb.put(left);
                cb.put(right);
            }
        } catch (InterruptedException e) {
            System.out.println(this + " Exiting via interrupt");
        }
    }
    public String toString() { return "Philosopher " + id + " "; }
}

public class practice_31_Deadlock {
    public static void main(String[] args) throws Exception {
        int ponder = 1;
        if(args.length > 0)
            ponder = Integer.parseInt(args[0]);
        int size = 5;
        if(args.length > 1)
            size = Integer.parseInt(args[1]);
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[size];
        ChopstickBin chopstickBin = new ChopstickBin();
        for(int i = 0; i < size; i++)
            sticks[i] = new Chopstick(i);
        for(int i = 0; i < size; i++){
            chopstickBin.put(sticks[i]);
        }

        // 注释下面一行则会造成死锁
        chopstickBin.put(new Chopstick(size));

        for(int i = 0; i < size; i++)
            exec.execute(new Philosopher(chopstickBin,i,ponder));
        if(args.length == 3 && args[2].equals("timeout"))
            TimeUnit.SECONDS.sleep(5);
        else {
            System.out.println("Press 'Enter' to quit");
            System.in.read();
        }
        exec.shutdownNow();
    }
}
