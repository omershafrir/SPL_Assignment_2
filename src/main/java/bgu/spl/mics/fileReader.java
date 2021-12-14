package bgu.spl.mics;

import bgu.spl.mics.application.objects.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class fileReader {
    //TODO - read file and clean main
    // end of main is run for the student

    //fields
    private Student[] students;
    private List<Model[]> ListOfArraysOfModels;
    private GPU[] GPUArray;
    private CPU[] CPUArray;
    private ConfrenceInformation[] confrenceInformations;
    private int TickTime;
    private int Duration;


    //constructor
    public fileReader(){}

    public void readInputFile(String pathName){
        // this is a test main to check the read of the json file
        File input = new File(pathName);
        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObject = fileElement.getAsJsonObject();

            //Student array
            JsonArray jsonArrayOfStudents = fileObject.get("Students").getAsJsonArray();
            //Initializing the student array
            students = new Student[jsonArrayOfStudents.size()];
            //Initializing the DS that will hold the models for each student
            ListOfArraysOfModels = new ArrayList<Model[]>();


            int counterOfStudents = 0;
            int counterOfModel = 0;
            int counterOfGPU = 0;
            int counterOfCPU = 0;
            int counterOfConfernces = 0;
            for(JsonElement studentElement : jsonArrayOfStudents){
                // Get : name ,department , status , model
                JsonObject studentJsonObject = studentElement.getAsJsonObject();
                String name = studentJsonObject.get("name").getAsString();
                String department = studentJsonObject.get("department").getAsString();
                String status = studentJsonObject.get("status").getAsString();

                // Get : model array
                JsonArray jsonArrayOfModels = studentJsonObject.get("models").getAsJsonArray();
                Model[] models = new Model[jsonArrayOfModels.size()];
                for(JsonElement modelElement : jsonArrayOfModels){
                    JsonObject modelJsonObject = modelElement.getAsJsonObject();
                    String modelName = modelJsonObject.get("name").getAsString();
                    //Creating a new data
                    String data_type = modelJsonObject.get("type").getAsString();
                    String data_size = modelJsonObject.get("size").getAsString();
                    int data_size_int = Integer.parseInt(data_size);
                    Data data = new Data(data_type, data_size_int);
                    //constructing the model
                    models[counterOfModel] = new Model(modelName,data,students[counterOfStudents]);
                    counterOfModel++;
                }
                counterOfModel = 0;



                //appending the model array into the list
                //each model array will be in the same index as the student who came with it
                ListOfArraysOfModels.add(models);
                //assigning the values after we read them
                students[counterOfStudents]= (new Student(name,department, status,ListOfArraysOfModels.get(counterOfStudents)));
                counterOfStudents++;
                //////////////////////////////////////////done getting students//////////////////////////
            }
            //////////////////////////////////////////////////getting GPUs////////////////////////////////
            JsonArray JsonArrayOfGpu = fileObject.get("GPUS").getAsJsonArray();
            GPUArray = new GPU[JsonArrayOfGpu.size()];

            for(JsonElement GPUElement : JsonArrayOfGpu){
                String type = GPUElement.getAsString();
                GPUArray[counterOfGPU] = new GPU(type);
                counterOfGPU++;
            }
            /////////////////////////////////////////////////getting CPUs////////////////////////////////
            JsonArray JsonArrayOfCpu = fileObject.get("CPUS").getAsJsonArray();
            CPUArray = new CPU[JsonArrayOfCpu.size()];

            for(JsonElement CPUElement : JsonArrayOfCpu){
                int cores = CPUElement.getAsInt();
                CPUArray[counterOfCPU] = new CPU(cores);
                counterOfCPU++;
            }
            /////////////////////////////////////////////////getting Conferences////////////////////////////////
            JsonArray JsonArrayOfConferences = fileObject.get("Conferences").getAsJsonArray();
            confrenceInformations = new ConfrenceInformation[JsonArrayOfConferences.size()];
            for(JsonElement ConferenceElement : JsonArrayOfConferences){
                JsonObject ConferenceJsonObject = ConferenceElement.getAsJsonObject();
                String name = ConferenceJsonObject.get("name").getAsString();
                int date = Integer.parseInt(ConferenceJsonObject.get("date").getAsString());
                confrenceInformations[counterOfConfernces] = new ConfrenceInformation(name,date);
                counterOfConfernces++;
            }
            /////////////////////////////////////////////////getting TickTime&Duration////////////////////////
            TickTime = Integer.parseInt(fileObject.get("TickTime").getAsString());
            Duration = Integer.parseInt(fileObject.get("Duration").getAsString());
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public Student[] getStudents() {
        return students;
    }

    public List<Model[]> getListOfArraysOfModels() {
        return ListOfArraysOfModels;
    }

    public GPU[] getGPUArray() {
        return GPUArray;
    }

    public CPU[] getCPUArray() {
        return CPUArray;
    }

    public ConfrenceInformation[] getConfrenceInformations() {
        return confrenceInformations;
    }

    public int getTickTime() {
        return TickTime;
    }

    public int getDuration() {
        return Duration;
    }





}
