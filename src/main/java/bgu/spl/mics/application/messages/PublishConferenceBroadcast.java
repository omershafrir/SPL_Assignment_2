package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;

import java.util.Vector;

public class PublishConferenceBroadcast implements Broadcast {

    private Vector<Model> models;
    public PublishConferenceBroadcast(Vector<Model> _models){
        models = _models;
    }

    public Vector<Model> getModels() {
        return models;
    }
}
