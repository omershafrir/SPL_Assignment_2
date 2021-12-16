//import java.util.concurrent.*;
import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import bgu.spl.mics.fileReader;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolImpl;
import sun.nio.ch.ThreadPool;

import java.util.*;
import java.util.concurrent.*;

public class main_test {
    public static void main(String []args) throws InterruptedException {

        fileReader reader = new fileReader();
        reader.readInputFile("example_input.json");  //the input path is starting from the folder of the project!

        /**
         * reading the input file
         */
        Student[] studentArray = reader.getStudents();
        GPU[] gpuArray = reader.getGPUArray();
        CPU[] cpuArray = reader.getCPUArray();
        ConfrenceInformation[] conferenceArray = reader.getConfrenceInformations();
        int TickTime = reader.getTickTime();
        int Duration = reader.getDuration();

        /**
         * instantiating the cluster and initializing it
         */
        Cluster cluster = Cluster.getInstance();
        cluster.setGPUArray(gpuArray);
        cluster.setCPUArray(cpuArray);
        cluster.initializeCluster();



        /**
         * instantiating the threads empty arrays
         */
        Thread[] studentServices = new Thread[studentArray.length];
        Thread[] CPUServices = new Thread[cpuArray.length];
        Thread[] GPUServices = new Thread[gpuArray.length];
        Thread[] confrencesServices = new Thread[conferenceArray.length];

        /**
         * instantiating the micro - services
         */

        MicroService timer = new TimeService(TickTime , Duration);
        for (int i=0 ;i< studentServices.length ; i++){
            MicroService tmpservice = new StudentService(studentArray[i].getName() , studentArray[i]);
            studentServices[i] = new Thread(tmpservice);
        }
        for (int i=0 ; i < CPUServices.length; i++){
            MicroService tmpservice =new CPUService("CPU", cpuArray[i]);
            CPUServices[i] = new Thread(tmpservice);
        }
        for (int i=0 ; i < GPUServices.length; i++){
            MicroService tmpservice = new GPUService("GPU", gpuArray[i]);
            GPUServices[i] = new Thread(tmpservice);
        }
        for (int i=0 ; i < confrencesServices.length; i++){
            MicroService tmpservice = new ConferenceService(conferenceArray[i].getName() , conferenceArray[i]);
            confrencesServices[i] = new Thread(tmpservice);
        }

        /**
         * running the micro-services one after another
         */
        Thread clock = new Thread(timer);
        clock.setName("TIMER_THREAD");
        clock.start();
        studentServices[0].setName("STUDENT");
        studentServices[0].start();



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
