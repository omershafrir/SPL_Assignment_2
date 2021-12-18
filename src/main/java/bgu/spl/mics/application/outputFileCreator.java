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
    private ConcurrentLinkedQueue<ConcurrentLinkedQueue<Model>> ListOfVectorsOfModels;
    private ConcurrentLinkedQueue<ConfrenceInformation> confrenceInformations;

    private outputFileCreator(){
        students = new ConcurrentLinkedQueue<Student>();
        ListOfVectorsOfModels = new ConcurrentLinkedQueue<ConcurrentLinkedQueue<Model>>();
        confrenceInformations = new ConcurrentLinkedQueue<ConfrenceInformation>();
    }

    public static outputFileCreator getInstance(){
        return instance;
    }


    public void PrintModel() {
        for(ConcurrentLinkedQueue<Model> modelVector : ListOfVectorsOfModels){
            for(Model model : modelVector){
                System.out.println("Model Name is : " + model.getName()
                + "The status of this model is" + model.getStatus() + "He as been published :"
                        + model.getResult().equals("Good"));

            }
        }
    }

    //    Gson gson = new Gson();
//    JsonObject jsonObject = gson.toJson();

}
