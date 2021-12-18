//import java.util.concurrent.*;
import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.outputFileCreator;
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
           output file initialization
         */
        outputFileCreator output = outputFileCreator.getInstance();




        /**
         * running the micro-services one after another
         */

        GPU[] GPUArrayDemo = new GPU[1];
        GPUArrayDemo[0] = gpuArray[0];

        CPU[] CPUArrayDemo = new CPU[1];
        CPUArrayDemo[0] = cpuArray[0];

        Cluster cluster = Cluster.getInstance();
        cluster.setGPUArray(GPUArrayDemo);
        cluster.setCPUArray(CPUArrayDemo);
        cluster.initializeCluster();

        Thread[] studentServices = new Thread[1];
        MicroService tmpservice1 = new StudentService(studentArray[0].getName() , studentArray[0]);
        studentServices[0] = new Thread(tmpservice1);

        Thread[] CPUServices = new Thread[1];
        MicroService tmpservice2 = new CPUService("CPU1", CPUArrayDemo[0]);
        CPUServices[0] = new Thread(tmpservice2);

        Thread[] GPUServices = new Thread[1];
        MicroService tmpservice3 = new GPUService("GPU1", GPUArrayDemo[0]);
        GPUServices[0] = new Thread(tmpservice3);



        TimeService timer = new TimeService(20 , 50000);
        Thread clock = new Thread(timer);
        clock.setName("TIMER_THREAD");
        clock.start();

        studentServices[0].setName(studentServices[0].getName());
        GPUServices[0].setName("GPU1");
        CPUServices[0].setName("CPU1");

        studentServices[0].start();
        GPUServices[0].start();
        CPUServices[0].start();


        GPUServices[0].join();
        CPUServices[0].join();

        clock.join();
        output.PrintModel();
    }
}
