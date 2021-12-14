package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event<Model> {

    private Model model;
    private MicroService sender;
    private Future<Model> future;


    public TrainModelEvent(Model model , MicroService m, Future<Model> future){
        this.model = model;
        this.sender = m;
        this.future = future;
    }

    @Override
    public MicroService getSender() {
        return sender;
    }

    public Model getModel() {
        return model;
    }
    @Override
    public Future<Model> getFuture() {
        return future;
    }
}
