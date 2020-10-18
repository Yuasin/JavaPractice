package concurrency.practice;

import concurrency.Restaurant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static net.mindview.util.Print.print;
import static net.mindview.util.Print.printnb;

class Meal2 {
    private final int orderNum;
    public Meal2(int orderNum) { this.orderNum = orderNum; }
    public String toString() { return "Meal " + orderNum; }
}

class WaitPerson2 implements Runnable {
    private practice_27_Lock_Condition restaurant;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    public WaitPerson2(practice_27_Lock_Condition r) { restaurant = r; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                lock.lock();
                try {
                    while(restaurant.meal == null)
                        condition.await(); // ... for the chef to produce a meal
                }finally {
                    lock.unlock();
                }

                print("Waitperson got " + restaurant.meal);
                restaurant.chef.lock.lock();
                try {
                    restaurant.meal = null;
                    restaurant.chef.condition.signalAll(); // Ready for another
                } finally {
                    restaurant.chef.lock.unlock();
                }
            }
        } catch(InterruptedException e) {
            print("WaitPerson interrupted");
        }
    }
}

class Chef2 implements Runnable {
    private practice_27_Lock_Condition restaurant;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    private int count = 0;
    public Chef2(practice_27_Lock_Condition r) { restaurant = r; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                lock.lock();
                try{
                    while(restaurant.meal != null)
                        condition.await(); // ... for the meal to be taken
                }finally {
                    lock.unlock();
                }

                if(++count == 10) {
                    print("Out of food, closing");
                    restaurant.exec.shutdownNow();
                }

                printnb("Order up! ");
                restaurant.waitPerson.lock.lock();
                try {
                    restaurant.meal = new Meal2(count);
                    restaurant.waitPerson.condition.signalAll();
                }finally {
                    restaurant.waitPerson.lock.unlock();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch(InterruptedException e) {
            print("Chef interrupted");
        } finally {

        }
    }
}

public class practice_27_Lock_Condition {
    Meal2 meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson2 waitPerson = new WaitPerson2(this);
    Chef2 chef = new Chef2(this);
    public practice_27_Lock_Condition() {
        exec.execute(chef);
        exec.execute(waitPerson);
    }
    public static void main(String[] args) {
        new practice_27_Lock_Condition();
    }
}
