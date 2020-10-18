package concurrency.practice;

// Toast
// Butterer
// Jammer
// Eater
// Alternator
// Merge

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static net.mindview.util.Print.print;

class Toast {
    public enum Status{
        DRY,
        BUTTERED,
        JAMMED,
        READY{
            @Override
            public String toString() {
                return BUTTERED.toString() + "&" +JAMMED.toString();
            }
        }
    }
    private Status status = Status.DRY;
    private final int id;
    public Toast(int id){
        this.id = id;
    }
    public void butter(){
        status = (status==Status.DRY)?Status.BUTTERED:Status.READY;
    }
    public void jam(){
        status = (status==Status.DRY)?Status.JAMMED:Status.READY;
    }
    public Status getStatu(){
        return status;
    }
    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return "Toast{" +
                "status=" + status +
                ", id=" + id +
                '}';
    }
}

class ToastQueue extends LinkedBlockingDeque<Toast>{}

class Toaster implements Runnable{
    ToastQueue dryToastQueue;
    private int count;
    Random random = new Random(47);
    public Toaster(ToastQueue dq){
        dryToastQueue = dq;
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(100 + random.nextInt(500));
                dryToastQueue.put(new Toast(count++));
            }
        }catch (InterruptedException e){
            print("Toaster interrupted");
        }
        print("Toaster off");
    }
}


class Butterer implements Runnable{
    ToastQueue toBeButtered, output;
    public Butterer(ToastQueue toBeButter, ToastQueue output){
        this.toBeButtered = toBeButter;
        this.output = output;
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                Toast toast = toBeButtered.take();
                toast.butter();
                print(toast);
                output.put(toast);
            }
        }catch (InterruptedException e){
            print("Butterer interrupted");
        }
        print("Butterer off");
    }
}

class Jammer implements Runnable{
    ToastQueue toBeJammed, output;
    public Jammer(ToastQueue toBeJammed, ToastQueue output){
        this.toBeJammed = toBeJammed;
        this.output = output;
    }
    @Override
    public void run() {
        try{
            while(!Thread.interrupted()){
                Toast toast = toBeJammed.take();
                toast.jam();
                print(toast);
                output.put(toast);
            }
        }catch (InterruptedException e){
            print("Jammer interrupted");
        }
        print("Jammer off");
    }
}

class Eater implements Runnable{
    ToastQueue finishQueue;
    public Eater(ToastQueue fq){
        finishQueue = fq;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                Toast toast = finishQueue.take();
                if(toast.getStatu()!= Toast.Status.READY){
                    print(">>>>Error" + toast);
                    System.exit(1);
                }else{
                    print("Chomp!" + toast);
                }
            }
        } catch (InterruptedException e) {
            print("Eater interrupted");
        }
        print("Eater off");
    }
}

class Alternator implements Runnable{
    ToastQueue input, output1, output2;
    boolean change;
    public Alternator(ToastQueue i, ToastQueue o1, ToastQueue o2){
        input = i;
        output1 = o1;
        output2 = o2;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                Toast toast = input.take();
                if(change){
                    output1.put(toast);
                }else{
                    output2.put(toast);
                }
                change = !change;
            }
        } catch (InterruptedException e) {
            print("Alternator interrupted");
        }
        print("Alternator off");
    }
}

class Merger implements Runnable{
    ToastQueue toBeButteredQueue, butteredQueue, toBeJammedQueue,jammedQueue,finishQueue;

    public Merger(ToastQueue toBeButteredQueue,ToastQueue butteredQueue,
                  ToastQueue toBeJammedQueue,ToastQueue jammedQueue,ToastQueue finishQueue){
        this.toBeButteredQueue = toBeButteredQueue;
        this.butteredQueue = butteredQueue;
        this.toBeJammedQueue = toBeJammedQueue;
        this.jammedQueue = jammedQueue;
        this.finishQueue = finishQueue;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                Toast toast = null;
                while(toast==null){
                    toast = butteredQueue.poll(50,TimeUnit.MILLISECONDS);
                    if(toast!=null)break;
                    toast = jammedQueue.poll(50,TimeUnit.MILLISECONDS);
                }
                switch (toast.getStatu()){
                    case BUTTERED:
                        toBeJammedQueue.put(toast);
                        break;
                    case JAMMED:
                        toBeButteredQueue.put(toast);
                        break;
                    default:
                        finishQueue.put(toast);
                }
            }
        } catch (InterruptedException e) {
            print("Merger interrupted");
        }
        print("Merger off");
    }
}


public class practice_29_BlockingQueue_parallel {
    public static void main(String[] args) throws InterruptedException {
        ToastQueue dryQueue = new ToastQueue(),
                toBeButteredQueue = new ToastQueue(),
                butteredQueue = new ToastQueue(),
                toBeJammedQueue = new ToastQueue(),
                jammedQueue = new ToastQueue(),
                finishedQueue = new ToastQueue();

        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(dryQueue));
        exec.execute(new Alternator(dryQueue, toBeButteredQueue, toBeJammedQueue));
        exec.execute(new Butterer(toBeButteredQueue, butteredQueue));
        exec.execute(new Jammer(toBeJammedQueue, jammedQueue));
        exec.execute(new Merger(toBeButteredQueue,butteredQueue,toBeJammedQueue,jammedQueue,finishedQueue));
        exec.execute(new Eater(finishedQueue));
        TimeUnit.SECONDS.sleep(3);

        exec.shutdownNow();
    }
}
