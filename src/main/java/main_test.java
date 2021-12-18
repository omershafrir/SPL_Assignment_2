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

        GPU[] GPUArrayDemo = new GPU[3];
        GPUArrayDemo[0] = gpuArray[0];
        GPUArrayDemo[1] = gpuArray[1];
        GPUArrayDemo[2] = gpuArray[2];

        CPU[] CPUArrayDemo = new CPU[5];
        CPUArrayDemo[0] = cpuArray[0];
        CPUArrayDemo[1] = cpuArray[1];
        CPUArrayDemo[2] = cpuArray[2];
        CPUArrayDemo[3] = cpuArray[3];
        CPUArrayDemo[4] = cpuArray[4];

        Cluster cluster = Cluster.getInstance();
        cluster.setGPUArray(GPUArrayDemo);
        cluster.setCPUArray(CPUArrayDemo);
        cluster.initializeCluster();

        Thread[] studentServices = new Thread[2];
        MicroService tmpservice1 = new StudentService(studentArray[0].getName() , studentArray[0]);
        studentServices[0] = new Thread(tmpservice1);
        MicroService tmpservice11 = new StudentService(studentArray[1].getName() , studentArray[1]);
        studentServices[1] = new Thread(tmpservice11);

        Thread[] CPUServices = new Thread[5];
        MicroService tmpservice2 = new CPUService("CPU1", CPUArrayDemo[0]);
        CPUServices[0] = new Thread(tmpservice2);
        MicroService tmpservice22 = new CPUService("CPU2", CPUArrayDemo[1]);
        CPUServices[1] = new Thread(tmpservice22);
        MicroService tmpservice222 = new CPUService("CPU3", CPUArrayDemo[2]);
        CPUServices[2] = new Thread(tmpservice222);
        MicroService tmpservice2222 = new CPUService("CPU3", CPUArrayDemo[3]);
        CPUServices[3] = new Thread(tmpservice2222);
        MicroService tmpservice22222 = new CPUService("CPU3", CPUArrayDemo[4]);
        CPUServices[4] = new Thread(tmpservice22222);

        Thread[] GPUServices = new Thread[3];
        MicroService tmpservice3 = new GPUService("GPU1", GPUArrayDemo[0]);
        GPUServices[0] = new Thread(tmpservice3);
        MicroService tmpservice32 = new GPUService("GPU2", GPUArrayDemo[1]);
        GPUServices[1] = new Thread(tmpservice32);
        MicroService tmpservice34 = new GPUService("GPU3", GPUArrayDemo[2]);
        GPUServices[2] = new Thread(tmpservice34);

        Thread[] ConferenceServices = new Thread[1];
        MicroService tmpservice4 = new ConferenceService("CONFERENCE1", conferenceArray[0]);
        ConferenceServices[0] = new Thread(tmpservice4);

        TimeService timer = new TimeService(1 , 5500);
        Thread clock = new Thread(timer);

        studentServices[0].setName("SIMBA");
        studentServices[1].setName("Zazu");

        GPUServices[0].setName("GPU1");
        GPUServices[1].setName("GPU2");
        GPUServices[2].setName("GPU3");

        CPUServices[0].setName("CPU1");
        CPUServices[1].setName("CPU2");
        CPUServices[2].setName("CPU3");
        CPUServices[3].setName("CPU4");
        CPUServices[4].setName("CPU5");

        ConferenceServices[0].setName("CONFERENCE1");

        GPUServices[0].start();
        GPUServices[1].start();

        CPUServices[0].start();
        CPUServices[1].start();
        CPUServices[2].start();
        CPUServices[3].start();
        CPUServices[4].start();

        clock.setName("TIMER_THREAD");

        ConferenceServices[0].start();

        Thread.currentThread().sleep(300);
        studentServices[0].start();
        studentServices[1].start();

        Thread.currentThread().sleep(300);
        clock.start();


        clock.join();

        output.Print();
        System.out.println();
        System.out.println("Program terminated.");


    }
}
