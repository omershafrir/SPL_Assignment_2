package bgu.spl.mics;

import bgu.spl.mics.application.objects.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import bgu.spl.mics.application.objects.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {

    public static void main(String[] args) {


        //Testing fileReader
    fileReader reader = new fileReader();
    //the input path is starting from the folder of the project!
    reader.readInputFile("example_input.json");
    Student[] s = reader.getStudents();
    GPU[] g = reader.getGPUArray();
    CPU[] c = reader.getCPUArray();
    for(Student z1 : s){
        System.out.println(z1);
    }
    for(GPU x : g){
        System.out.println(x);
    }
    for(CPU y : c){
        System.out.println(y);
    }


    }
}
