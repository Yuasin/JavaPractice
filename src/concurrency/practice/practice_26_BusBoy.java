package concurrency.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.mindview.util.Print.print;
import static net.mindview.util.Print.printnb;

class Meal {
    private final int orderNum;
    public Meal(int orderNum) { this.orderNum = orderNum; }
    public String toString() { return "Meal " + orderNum; }
}

class WaitPerson implements Runnable {
    private practice_26_BusBoy restaurant;
    public WaitPerson(practice_26_BusBoy r) { restaurant = r; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized(this) {
                    while(restaurant.meal == null)
                        wait(); // ... for the chef to produce a meal
                }
                print("Waitperson got " + restaurant.meal);
                synchronized(restaurant.chef) {
                    restaurant.meal = null;
                    restaurant.chef.notifyAll(); // Ready for another
                }
                synchronized (restaurant.busBoy){
                    restaurant.cleanFlag = true;
                    restaurant.busBoy.notifyAll();
                }
            }
        } catch(InterruptedException e) {
            print("WaitPerson interrupted");
        }
    }
}

class Chef implements Runnable {
    private practice_26_BusBoy restaurant;
    private int count = 0;
    public Chef(practice_26_BusBoy r) { restaurant = r; }
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized(this) {
                    while(restaurant.meal != null)
                        wait(); // ... for the meal to be taken
                }
                if(++count == 10) {
                    print("Out of food, closing");
                    restaurant.exec.shutdownNow();
                }
                printnb("Order up! ");
                synchronized(restaurant.waitPerson) {
                    restaurant.meal = new Meal(count);
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch(InterruptedException e) {
            print("Chef interrupted");
        }
    }
}

class BusBoy implements Runnable{
    private practice_26_BusBoy restaurant;
    public BusBoy(practice_26_BusBoy restaurant){
        this.restaurant = restaurant;
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                synchronized (this){
                    while (!restaurant.cleanFlag){
                        wait();
                    }
                    System.out.println("BusBoy Clean!");
                    restaurant.cleanFlag = false;
                }
            }
        }catch (InterruptedException e){

        }
    }
}

public class practice_26_BusBoy {
    Meal meal;
    boolean cleanFlag = false;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson waitPerson = new WaitPerson(this);
    Chef chef = new Chef(this);
    BusBoy busBoy = new BusBoy(this);
    public practice_26_BusBoy() {
        exec.execute(chef);
        exec.execute(waitPerson);
        exec.execute(busBoy);
    }
    public static void main(String[] args) {
        new practice_26_BusBoy();
    }
}
