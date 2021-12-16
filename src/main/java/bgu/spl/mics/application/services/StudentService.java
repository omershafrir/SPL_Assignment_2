package bgu.spl.mics.application.services;

//import bgu.spl.mics.*;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.Vector;

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
    private Student myStudent;
    private Future<Model> future;

    public StudentService(String name , Student _myStudent) {
        super(name);
        myStudent = _myStudent;
        myModels = myStudent.getModels();
    }

    public String getStatus(){
        return myStudent.getStatus();
    }
    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);
        //callback instructions for TickBroadcast
        Callback<TickBroadcast> instructionsForTick = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                myStudent.incrementTimer();
                System.out.println("Recieved Tick (in student service call)");

                afterTimeTickAction();
            }
        };

        //callback instructions for PublishConferenceBroadcast
        Callback<PublishConferenceBroadcast> instructionsForConference =
                                     new Callback<PublishConferenceBroadcast>() {
            @Override
            public void call(PublishConferenceBroadcast c) {
                    Vector<Model> vecOfModels = c.getModels();
                    for (Model model : vecOfModels){
                        if (model.getStudent().equals(myStudent))
                            myStudent.incrementPublished();
                        else
                            myStudent.readPaper();
                    }
            }
        };

       subscribeBroadcast(TickBroadcast.class , instructionsForTick);
       subscribeBroadcast(PublishConferenceBroadcast.class, instructionsForConference);
    }
    public void afterTimeTickAction(){

            future = this.myStudent.getFuture();
            //if there is a model to train
        if(future == null) {
            if (myStudent.getCounterTestedModels() < myModels.length) {
                TrainModelEvent e = new TrainModelEvent(myModels[myStudent.getCounterTestedModels()], this);
                myStudent.setFuture(sendEvent(e));
            }
        }
        else { //future != null
                if (future.isDone()){
                    Model currentModel = future.get();
                    //if there is a model to test
                    if(future.get().getStatus() == "Trained") {
                        TestModelEvent testEvent = new TestModelEvent(currentModel, this);
                        myStudent.setFuture(sendEvent(testEvent));
                    }
                    else if(future.get().getStatus() == "Tested"){
                            if(future.get().getResult() == "Good"){
                            PublishResultsEvent publishEvent = new PublishResultsEvent(currentModel);
                            myStudent.setFuture(sendEvent(publishEvent));
                            }
                        myStudent.setFuture(null);
                        }
                }
            }
    }
}