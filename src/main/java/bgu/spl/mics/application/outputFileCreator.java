package bgu.spl.mics.application;


import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.fileReader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class outputFileCreator {

    private static outputFileCreator instance = new outputFileCreator();
    private ConcurrentLinkedQueue<Student> students;
    private ConcurrentLinkedQueue<Model[]> ListOfArraysOfModels;
    private ConcurrentLinkedQueue<ConfrenceInformation> confrenceInformations;
    private Statistics stats;
    private int timeOfGPU;
    private int timeOfCPU;

    private outputFileCreator(){
        students = new ConcurrentLinkedQueue<Student>();
        ListOfArraysOfModels = new ConcurrentLinkedQueue<Model[]>();
        confrenceInformations = new ConcurrentLinkedQueue<ConfrenceInformation>();
        stats = new Statistics(Cluster.getInstance());
    }

    public static outputFileCreator getInstance(){
        return instance;
    }


    public void getDataFromStudentMS(Student student ,Model[] models){
        students.add(student);
        ListOfArraysOfModels.add(models);
    }

    public void getDataFromConference(ConfrenceInformation myConfrence){
        confrenceInformations.add(myConfrence);
    }

//    public void setTimeFromGPU(){
//        Statistics stats = new Statistics(Cluster.getInstance());
//        timeOfGPU = stats.NumberOfGPUTimeUnitsUsed();
//    }
//
//    public void setTimeFromCPU(){
//        Statistics stats = new Statistics(Cluster.getInstance());
//        timeOfCPU = stats.NumberOfCPUTimeUnitsUsed();
//    }

    public void Print() {
        for(Model[] modelArray : ListOfArraysOfModels){
            for(Model model : modelArray){
                System.out.println(model.toString());
//                System.out.println("Model Name is : " + model.getName()
//                + " The status of this model is: " + model.getStatus() + ", his status is: "+ model.getStatus() + " He as been published :"
//                        + model.getResult().equals("Good"));

            }
        }
        System.out.println("Time of GPU is :  " + stats.NumberOfGPUTimeUnitsUsed());
        System.out.println("Time of CPU is :  " + stats.NumberOfCPUTimeUnitsUsed());
        System.out.println("Total amount of data batches that were processed: "+ stats.totalNumDataBatchesProcessed());
    }

    //    Gson gson = new Gson();
//    JsonObject jsonObject = gson.toJson();

}
