package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Model;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {

    private Model[] myModels;
    private int numberOfModels;
    private String status;
    private Future<Model> future;

    public StudentService(String name, Model[] myModels, String status) {
        super(name);
        this.myModels = myModels;
        numberOfModels = myModels.length;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    protected void initialize() {
        // I think that student shouldn't subscribe to any event -
        // only send event and get the future
        // automatically

        /*
        it must sign up for the conference publication broadcasts.
        PublishConferenceBroadcast: Sent by the conference at a set time,
         will broadcast all the aggregated results to all the
         */
        PublishConferenceBroadcast b = new PublishConferenceBroadcast();

//        this.subscribeBroadcast(b.getClass(),){
            //TODO - need to implement the callback for this broadcast
//        } );


//        this.subscribeBroadcast(t.getClass(),callbackFunction);


    }

}
