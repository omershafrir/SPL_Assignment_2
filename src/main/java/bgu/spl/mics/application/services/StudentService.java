package bgu.spl.mics.application.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.PublishConferenceBroadcast;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

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
    public StudentService(String name, Model[] myModels) {
        super(name);
        this.myModels = myModels;
    }

    @Override
    protected void initialize() {
        // I think that student shouldn't subscribe to any event - only send event and get the future
        // automatically

        /*
        it must sign up for the conference publication broadcasts.
        PublishConferenceBroadcast: Sent by the conference at a set time,
         will broadcast all the aggregated results to all the
         */
        PublishConferenceBroadcast b = new PublishConferenceBroadcast();

//        this.subscribeBroadcast(b.getType(),()->{
            //TODO - need to implement the callback for this broadcast
//        } );


    }
}
