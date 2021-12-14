//import java.util.concurrent.*;
import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.TimeService;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl;
import sun.nio.ch.ThreadPool;

import java.util.*;
import java.util.concurrent.*;

public class main_test {
    public static void main(String []args) throws InterruptedException {

//        MicroService timer = new TimeService(1000 , 3000);
//        timer.run();

        CPU cpu = new CPU(2);
        CPUService srv = new CPUService("rocker" , cpu);
        srv.run();


/**
        Future<Integer> future = new Future<>();
        Thread t1 = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+" is going to sleep...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.resolve(1);
        });
        Thread t3 = new Thread(()-> {
            System.out.println(Thread.currentThread().getName()+" is working...");
                System.out.println("before get: "+future.isDone());
                future.get(3,TimeUnit.SECONDS);
//                future.get();
                System.out.println("after get: "+future.isDone());

        });
        t1.start();
        t3.start();
**/
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
