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

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) {

        for (int i=0 ;i<100 ; i++){
            System.out.println((int)((Math.random())*(10+1)));
        }

    }
}
