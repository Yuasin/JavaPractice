package concurrency.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class practice_15_synchronizedDiffObject {

    static class Shared{
        Object X = new Object();
        Object Y = new Object();
        Object Z = new Object();
        public void methodX() throws InterruptedException {
            synchronized (X){
                System.out.println("methodX");
                TimeUnit.SECONDS.sleep(1);
            }
        }
        public void methodY() throws InterruptedException {
            synchronized (Y){
                System.out.println("methodY");
                TimeUnit.SECONDS.sleep(1);
            }
        }
        public void methodZ() throws InterruptedException {
            synchronized (Z){
                System.out.println("methodZ");
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    public static void main(String[] args) {
        Shared shared = new Shared();
        ExecutorService exec = Executors.newCachedThreadPool();

        exec.execute(new Thread(() -> {
            while (true) {
                try {
                    shared.methodX();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
        exec.execute(new Thread(() -> {
            while (true) {
                try {
                    shared.methodY();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
        exec.execute(new Thread(() -> {
            while (true) {
                try {
                    shared.methodZ();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
        exec.shutdown();

    }
}
