package bgu.spl.mics;

//import java.lang.Runnable;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import sun.nio.ch.ThreadPool;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws InterruptedException {

//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }
        ExecutorService executor = Executors.newFixedThreadPool(4);

        Runnable run1 = ()->{
            System.out.println("this is thread1");
        };

        Runnable run2 = ()->{
            System.out.println("this is thread2");
        };
        Thread t1 = new Thread(run1);
        Thread t2 = new Thread(run2);
        Runnable run3 = ()->{


            System.out.println("this is thread3");
            System.out.println("enter:");
            t1.start();

            try {
                t1.wait(100000,1000);
                t2.wait(100000,1000);
            } catch (InterruptedException e) {
            }

            Scanner s = new Scanner(System.in);
            System.out.println(s);
        };


        Thread t3 = new Thread(run3);

        executor.execute(t1);
        executor.execute(t2);
        executor.execute(t3);

//        t3.join();

        executor.shutdown();
    }
}
