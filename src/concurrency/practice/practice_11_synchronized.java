package concurrency.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class practice_11_synchronized {
    // 共享的资源，两个域，一个操作域的方法，一个读写域的方法，
    static class SharedResource{
        // 使用并发时将域设置为private，保证了只用加上synchronized的方法能访问域
        private int increment;
        private int decrement;

        // 对任何要操作域的方法都必须要加synchronized关键字
        public synchronized void cal(){
            // 不使用 synchronized时，在cal函数中计算时另一个线程可能在进行judge计算，此时计算会得出错误结果
            increment++;
            decrement--;
        }

        // cal()和judge()有一个不加程序就会报告错误
        public synchronized int[] judge(){
            int storeInc = increment;
            int storeDec = decrement;
            return storeInc+storeDec==0?null:new int[]{storeInc,storeDec};

            //下面无法返回正确的域状态，在表达式前面判断和是否为零时读取一次，后面新建立int[]又读取一次，两次结果可能不同
            //return increment+decrement==0?null:new int[]{increment,decrement};
        }
    }

    static class Task implements Runnable{
        SharedResource resource;
        Task(SharedResource s){
            this.resource = s;
        }
        @Override
        public void run() {
            while(true) {
                resource.cal();
                int[] back = resource.judge();
                if (back != null) {
                    System.out.println(Thread.currentThread() + " get the wrong status " +"Increment: " + back[0] + " Decrement: "+ back[1]);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0; i<5; i++){
            exec.execute(new Task(sharedResource));
        }
        exec.shutdown();
    }
}
