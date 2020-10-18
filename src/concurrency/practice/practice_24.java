package concurrency.practice;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

// 一个生产者，一个消费者
// 队列存放生产对象
//

class FlowQueue<T>{
    private Queue<T> queue = new LinkedList<>();
    private int maxSize;

    public FlowQueue(int maxSize){
        this.maxSize = maxSize;
    }

    public synchronized void put(T element) throws InterruptedException {
        while (queue.size() == maxSize){
            wait();
        }
        queue.offer(element);
        notifyAll();
    }

    public synchronized T get() throws InterruptedException {
        while(queue.isEmpty()){
            wait();
        }
        T returnVal = queue.poll();
        notifyAll();
        return returnVal;
    }

    @Override
    public String toString() {
        return "FlowQueue{" +
                "queue=" + queue +
                '}';
    }
}

class Item{
    private static int count = 0;
    final int id = count++;

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                '}';
    }
}

// 队列未满->生产、入队、notifyAll(),  队列满->wait(),
class Producer implements Runnable{
    FlowQueue<Item> queue;
    int sleepTime;
    public Producer(FlowQueue<Item> queue, int sleepTime){
        this.queue = queue;
        this.sleepTime = sleepTime;
    }
    @Override
    public void run() {
        try {
            while(true){
                queue.put(new Item());
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            }
        }catch (InterruptedException e){
            return;
        }
    }
}

class Consumer implements Runnable{
    FlowQueue<Item> queue;
    int sleepTime;
    public Consumer(FlowQueue<Item> queue, int sleepTime){
        this.queue = queue;
        this.sleepTime = sleepTime;
    }
    @Override
    public void run() {
        try {
            while(true){
                System.out.println(queue.get());
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            }
        }catch (InterruptedException e){
            return;
        }
    }
}

public class practice_24 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        FlowQueue<Item> queue = new FlowQueue<>(20);
        exec.execute(new Producer(queue,10));
        exec.execute(new Consumer(queue, 1000));
        TimeUnit.SECONDS.sleep(2);
        System.out.println(queue);
        exec.shutdownNow();
    }
}
