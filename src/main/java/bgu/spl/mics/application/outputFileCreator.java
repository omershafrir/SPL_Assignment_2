package bgu.spl.mics.application;


import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
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

    private outputFileCreator(){
        students = new ConcurrentLinkedQueue<Student>();
        ListOfArraysOfModels = new ConcurrentLinkedQueue<Model[]>();
        confrenceInformations = new ConcurrentLinkedQueue<ConfrenceInformation>();
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

    public void getTimeFromGPU(){

    }

    public void getTimeFromCPU(){

    }

    public void PrintModel() {
        for(Model[] modelArray : ListOfArraysOfModels){
            for(Model model : modelArray){
                System.out.println("Model Name is : " + model.getName()
                + "The status of this model is" + model.getStatus() + "He as been published :"
                        + model.getResult().equals("Good"));

            }
        }
    }

    //    Gson gson = new Gson();
//    JsonObject jsonObject = gson.toJson();

}
