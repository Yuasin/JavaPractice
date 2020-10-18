package concurrency.practice;

import concurrency.SimpleDaemons;

import java.util.concurrent.TimeUnit;

import static net.mindview.util.Print.print;

public class practice_08 implements Runnable{

    public void run() {
        try {
            while(true) {
                TimeUnit.MILLISECONDS.sleep(100);
                print(Thread.currentThread() + " " + this);
            }
        } catch(InterruptedException e) {
            print("sleep() interrupted");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 10; i++) {
            Thread daemon = new Thread(new practice_08());
            daemon.setDaemon(true); // Must call before start()
            daemon.start();
        }
        print("All daemons started");
        TimeUnit.MILLISECONDS.sleep(100);
    }
}
