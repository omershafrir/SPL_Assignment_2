package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.fileReader;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        fileReader reader = new fileReader();
        //the input path is starting from the folder of the project!
        reader.readInputFile("example_input.json");
        Cluster cluster = Cluster.getInstance();
        Student[] studentArray = reader.getStudents();
        GPU[] gpuArray = reader.getGPUArray();
        CPU[] cpuArray = reader.getCPUArray();
        ConfrenceInformation[] conferenceArray = reader.getConfrenceInformations();
        int TickTime = reader.getTickTime();
        int Duration = reader.getDuration();
        cluster.setGPUArray(gpuArray);
        cluster.setCPUArray(cpuArray);
        cluster.initializeCluster();


        for(GPU gpu : gpuArray ){
            Thread gpuService = new Thread((Runnable) gpu);
            gpuService.start();
        }
        for(CPU cpu : cpuArray ){
            Thread gpuService = new Thread((Runnable) cpu);
            gpuService.start();
        }
        for(Student s : studentArray){
            s
        }


//        GPU[] g = reader.getGPUArray();
//        CPU[] c = reader.getCPUArray();
//
//        for(Student z1 : s){
//            System.out.println(z1);
//        }
//        for(GPU x : g){
//            System.out.println(x.getCluster());
//        }
//        for(CPU y : c){
//            System.out.println(y);
//        }
    }
}
