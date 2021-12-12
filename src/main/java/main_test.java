//import java.util.concurrent.*;
import bgu.spl.mics.Callback;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl;
import sun.nio.ch.ThreadPool;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class main_test {
    public static void main(String []args){
        ExecutorService ex = Executors.newFixedThreadPool(4);
        Runnable [] vec = new Runnable[7];

        for (int i=1 ; i <= 6 ; i++) {
//            Runnable Runnable;
            int finalI = i;
            vec[i] = () ->System.out.println
                ("this is job number "+ finalI +", "+Thread.currentThread().getName()+" runs me");
        }
        for (int i=1 ; i <= 6 ; i++) {
           ex.execute(vec[i]);
        }

        ex.shutdown();
    }
}
