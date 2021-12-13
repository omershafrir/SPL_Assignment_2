//import java.util.concurrent.*;
import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl;
import sun.nio.ch.ThreadPool;

import java.util.*;
import java.util.concurrent.*;

public class main_test {
    public static void main(String []args) throws InterruptedException {
        /**
        ExecutorService ex = Executors.newFixedThreadPool(4);
        Callable<Integer> [] vec = new Callable[7];
        Future<Integer>[] futures = new Future[7];

        for (int i=1 ; i <= 6 ; i++) {
            int finalI = i;
            vec[i] = () ->{
                        System.out.println
                        ("this is job number "+ finalI +", "+Thread.currentThread().getName()+" runs me");
                        return finalI;
                 };
        }

        for (int i=1 ; i <= 6 ; i++) {
            try{
                futures[i]= (Future<Integer>) ex.submit(vec[i]);
            }catch (Exception e){
                System.out.println(e);
            }

        }

        ex.shutdown();
    **/

        Future<Integer> future = new Future<>();
        Thread t1 = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+" is going to sleep...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.resolve(1);
        });
        Thread t3 = new Thread(()-> {
            System.out.println(Thread.currentThread().getName()+" is working...");
            try {
                System.out.println("before get: "+future.isDone());
//                future.get(2,TimeUnit.SECONDS);
                future.get();
                System.out.println("after get: "+future.isDone());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t3.start();
//        t2.start();

//        t1.join();
//        t2.join();
//******************************************************************
//        Callable<Integer> call = ()->
//                                    {
//                                        System.out.println(1);
//                                        return 1;};
//        Runnable run = ()->{};
//        Thread thread = new Thread((Runnable) call);
////        Future<Integer> future =
    }
}
