package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.messages.PublishResultsEvent;

import java.util.Vector;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    private String name;
    private int date;
    private Vector<Model> models;
    private int internalTimer;
    private Vector<PublishResultsEvent> events;

    public ConfrenceInformation(String name, int date) {
        this.name = name;
        this.date = date;
        models = new Vector<>();
        internalTimer = 0;
        events = new Vector<>();
    }

    public String getName() {
        return name;
    }

    public void addModel(Model model){
        models.add(model);
    }

    public Vector<Model> getModels() {
        return models;
    }

    public void addEvent(PublishResultsEvent event){
        events.add(event);
    }

    public Vector<PublishResultsEvent> getEvents() {
        return events;
    }

    public int getDate() {
        return date;
    }
    public String toString(){
        String output ="";

        for(int i=1 ; i< events.size() ; i++)
            output +="Model number " + i + " : " + events.elementAt(i).getModel().getName() + " , ";

        for(int i=1 ; i< models.size() ; i++)
            output +="Model number " + i + " : " + models.elementAt(i).getName() + " , ";
        return output;
    }
    public void incrementTimer(){
        internalTimer++;
    }

    public int getInternalTimer() {
        return internalTimer;
    }


}
