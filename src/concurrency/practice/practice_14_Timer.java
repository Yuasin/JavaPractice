package concurrency.practice;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class practice_14_Timer {

    public static void startTimer(int i){
        long before = System.currentTimeMillis();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer " +i+ " :" + (System.currentTimeMillis()-before));
                //this.cancel()只能获取到TimerTask,无法使Timer停止
                //this.cancel();
                timer.cancel();
            }
        }, new Random().nextInt(3000));
    }

    public static void main(String[] args) {
        for(int i=0; i<100;i++){
            startTimer(i);
        }
    }

}
