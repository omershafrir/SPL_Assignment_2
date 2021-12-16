package bgu.spl.mics.application.services;

//import bgu.spl.mics.*;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Model;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConfrenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {
    private ConfrenceInformation myConfrence;

    public ConferenceService(String name , ConfrenceInformation _myConfrence) {
        super(name);
        myConfrence = _myConfrence;
    }

    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);
        MicroService self = this;

        //callback instructions for PublishResultsEvent
        Callback<PublishResultsEvent> instructionsForPublish
                             = new Callback<PublishResultsEvent>() {
            @Override
            public void call(PublishResultsEvent c) {
                Model model = c.getModel();
                myConfrence.addModel(model);
                myConfrence.addEvent(c);
            }
        };

        //callback instructions for TickBroadcast
        Callback<TickBroadcast> instructionsForTick = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                myConfrence.incrementTimer();
                if(myConfrence.getInternalTimer() == myConfrence.getDate()){
                    sendBroadcast(new PublishConferenceBroadcast(myConfrence.getModels()));
                    MessageBus msgbus = MessageBusImpl.getInstance();
                    for (PublishResultsEvent resultPublish : myConfrence.getEvents()) {
                        complete(resultPublish, resultPublish.getModel());
                        resultPublish.getModel().publishModel();
                        // to decide : number of publications of a student will increase when:
                        //    1. the conference goes out. 2. the student reads the conference broadcast
                        // basically same thing
//                        resultPublish.getModel().getStudent().incrementPublished();
                    }
                    msgbus.unregister(self);
                }
            }
        };

        subscribeEvent(PublishResultsEvent.class , instructionsForPublish);
        subscribeBroadcast(TickBroadcast.class , instructionsForTick);
    }
}
