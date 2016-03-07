package main.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2016-01-11.
 */
public class TestThreadPool {
    public static void main(String args[]){
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int j=0;j<5;j++) {
            for (int i = 0; i < 5; i++){
                executorService.execute(new TestRunnable());
                System.out.println("************* a" + (j*10+i) + " *************");
            }
        }
        executorService.shutdown();
    }
}

class TestRunnable implements Runnable{
    public void run(){
        System.out.println(Thread.currentThread().getName() + "线程被调用了。");
    }
}