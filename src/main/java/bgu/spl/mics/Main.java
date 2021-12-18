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
        Thread t = new Thread(() -> System.out.println("JI") );
        t.start();
        System.out.println("HI");
        System.out.println(t.isAlive());
    }
}
