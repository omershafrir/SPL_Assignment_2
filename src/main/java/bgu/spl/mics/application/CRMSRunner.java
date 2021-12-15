package bgu.spl.mics.application;

import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Student;
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
        int TickTime = reader.getTickTime();
        int Duration = reader.getDuration();
        cluster.setGPUArray(gpuArray);
        cluster.setCPUArray(cpuArray);
        cluster.initializeCluster();

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
