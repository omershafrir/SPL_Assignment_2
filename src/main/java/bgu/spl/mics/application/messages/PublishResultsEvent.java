package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Model;

public class PublishResultsEvent implements Event<Model> {
    private Model model;
    public PublishResultsEvent(Model _model){
        model = _model;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public MicroService getSender() {
        return null;
    }

    @Override
    public Future getFuture() {
        return null;
    }

    public void setFuture(Future<Model> future) {
    }
}
