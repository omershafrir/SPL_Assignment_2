package bgu.spl.mics.application;


import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.fileReader;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
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
        //not needed for now
        ListOfArraysOfModels = new ConcurrentLinkedQueue<Model[]>();
        confrenceInformations = new ConcurrentLinkedQueue<ConfrenceInformation>();
        stats = new Statistics(Cluster.getInstance());
    }

    public static outputFileCreator getInstance(){
        return instance;
    }


    public void getDataFromStudentMS(Student student ,Model[] models){
        student.setMyModels(models);
        students.add(student);
        //will update the students models
//        ListOfArraysOfModels.add(models);
    }

    public void getDataFromConference(ConfrenceInformation myConfrence){
        confrenceInformations.add(myConfrence);
    }


    public Student[] StudentToArray(){
        Student[] output = new Student[students.size()];
        for(int i = 0; i < students.size(); i++){
            output[i] = students.remove();
        }
        return output;

    }
    public void generateTheFile(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        Gson gson = new Gson();
        try {
//            Writer writer = Files.newBufferedWriter(Paths.get("WITHthePaths.json"));
            FileWriter writer = new FileWriter("WithoutthePaths.json");
//            HashMap<String,Object> general = new HashMap<>();
            JsonObject general = new JsonObject();
//            HashMap<String,Object> students_hash = new HashMap<>();
//            HashMap<String,Object> conferences_hash = new HashMap<>();
            JsonArray studentArray = new JsonArray();


            for(Student s: students) {
                JsonObject z = new JsonObject();
                z.addProperty("name",s.getName());
                z.addProperty("department", s.getDepartment());
                z.addProperty("status",s.getStatus());
                z.addProperty("publications", s.getPublications());
                z.addProperty("papersRead", s.getPapersRead());
                JsonArray modelsArray = new JsonArray();
                for(Model m: s.getMyModels()) {
                    if(!m.getResult().equals("Non")) {
                        JsonObject model = new JsonObject();
                        model.addProperty("name", m.getName());
                        //
                        JsonObject dataModelo = new JsonObject();
                        JsonPrimitive size = new JsonPrimitive(m.getData().getSize());
                        JsonPrimitive type = new JsonPrimitive(String.valueOf(m.getData().getType()));
                        dataModelo.add("type", type);
                        dataModelo.add("size", size);
                        model.add("data", dataModelo);
                        //
                        model.addProperty("status", m.getStatus());
                        model.addProperty("result", m.getResult());
                        modelsArray.add(model);
                    }
                }
                z.add("trainedModels",modelsArray);
                studentArray.add(z);
            }


            //generating conference hash
            JsonArray confrenceArray = new JsonArray();
            for (ConfrenceInformation conference: this.confrenceInformations) {
                JsonObject conferenceSpecific = new JsonObject();
                conferenceSpecific.addProperty("name",conference.getName());
                conferenceSpecific.addProperty("date",conference.getDate());
                JsonArray publication = new JsonArray();
                for(Model m : conference.getModels()){
                    JsonObject model2 = new JsonObject();
                    model2.addProperty("name", m.getName());
                    //
                    JsonObject dataModelo = new JsonObject();
                    JsonPrimitive size = new JsonPrimitive(m.getData().getSize());
                    JsonPrimitive type = new JsonPrimitive(String.valueOf(m.getData().getType()));
                    dataModelo.add("type", type);
                    dataModelo.add("size", size);
                    model2.add("data", dataModelo);
                    //
                    model2.addProperty("status", m.getStatus());
                    model2.addProperty("result", m.getResult());
                    publication.add(model2);
                }
                conferenceSpecific.add("publications",publication);
                confrenceArray.add(conferenceSpecific);
            }




            JsonPrimitive GPUsage = new JsonPrimitive(stats.NumberOfGPUTimeUnitsUsed());
            JsonPrimitive CPUsage = new JsonPrimitive(stats.NumberOfCPUTimeUnitsUsed());
            JsonPrimitive baches = new JsonPrimitive(stats.totalNumDataBatchesProcessed());
            //adding everything to the general hash
            general.add("students", studentArray);
            general.add("conferences", confrenceArray);
            general.add("gpuTimeUsed", GPUsage);
            general.add("cpuTimeUsed", CPUsage);
            general.add("batchesProcessed", baches);

            gson.toJson(general,writer);
//            gson.toJson(studentArray);
//            gson.toJson(confrenceArray);












            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Print() {

        for (Student s :
             students) {
            System.out.println(s.getName());
        }
//        for(Model[] modelArray : ListOfArraysOfModels){
//            for(Model model : modelArray){
//                System.out.println(model.toString());
////                System.out.println("Model Name is : " + model.getName()
////                + " The status of this model is: " + model.getStatus() + ", his status is: "+ model.getStatus() + " He as been published :"
////                        + model.getResult().equals("Good"));
//
//            }
//        }
//        System.out.println("Time of GPU is :  " + stats.NumberOfGPUTimeUnitsUsed());
//        System.out.println("Time of CPU is :  " + stats.NumberOfCPUTimeUnitsUsed());
//        System.out.println("Total amount of data batches that were processed: "+ stats.totalNumDataBatchesProcessed());
    }

    //    Gson gson = new Gson();
//    JsonObject jsonObject = gson.toJson();

}
