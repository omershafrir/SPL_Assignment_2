package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.services.StudentService;
import com.sun.org.apache.xpath.internal.operations.Mod;

public class TestModelEvent implements Event<Model> {

    private Model model;
    private MicroService sender;
    private String senderStatus;
    private Future<Model> future;

    public TestModelEvent(Model model , StudentService m , Future<Model> future){
        this.model = model;
        this.sender = m;
        this.senderStatus = m.getStatus();
        this.future = future;
    }

    public String getSenderStatus() {
        return senderStatus;
    }

    public Model getModel() {
        return model;
    }

    @Override
    public MicroService getSender() {
        return sender;
    }

    @Override
    public Future<Model> getFuture() {
        return this.future;
    }
}
