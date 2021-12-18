package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.*;
import bgu.spl.mics.fileReader;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
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
        clock.start();

        for (int i=0 ;i< studentServices.length ; i++){
            studentServices[i].start();
        }
        for (int i=0 ; i < CPUServices.length; i++){
            CPUServices[i].start();
        }
        for (int i=0 ; i < GPUServices.length ; i++){
            GPUServices[i].start();
        }
        for(int i=0 ; i < confrencesServices.length ; i++){
            confrencesServices[i].start();
        }


//        clock.join();

        System.out.println();
        System.out.println("Program terminated.");
    }
}
