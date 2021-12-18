package bgu.spl.mics.application.services;

//import bgu.spl.mics.*;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.application.outputFileCreator;

import java.util.NoSuchElementException;
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

    public Model[] getMyModels() {
        return myModels;
    }

    public Student getMyStudent() {
        return myStudent;
    }

    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);
        StudentService self = this;
        //callback instructions for TickBroadcast
        Callback<TickBroadcast> instructionsForTick = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                myStudent.incrementTimer();
//                System.out.println("Recieved Tick (in student service call)");  ///////////////////////////////////////////

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
                        System.out.println("MODEL: "+model.toString());  //////////////////////////////////
                        if (model.getStudent() .equals(myStudent))
                            myStudent.incrementPublished();
                        else
                            myStudent.readPaper();
                    }
            }
        };

        //callback instructions for TerminateBroadcast
        Callback<TerminateBroadcast> instructionsForTerminate =
                new Callback<TerminateBroadcast>() {
                    @Override
                    public void call(TerminateBroadcast c) {
                        outputFileCreator output = outputFileCreator.getInstance();
                        //sending the data and storing it in a Thread Safe DS
                        output.getDataFromStudentMS(self.getMyStudent(), self.getMyModels());
                        terminate();
                    }
                };

       subscribeBroadcast(TickBroadcast.class , instructionsForTick);
       subscribeBroadcast(PublishConferenceBroadcast.class, instructionsForConference);
       subscribeBroadcast(TerminateBroadcast.class , instructionsForTerminate);
    }
    public void afterTimeTickAction(){

            future = this.myStudent.getFuture();
            //if there is a model to train
        if(future == null) {
            if (myStudent.getCounterTestedModels() < myModels.length) {
                TrainModelEvent e = new TrainModelEvent(myModels[myStudent.getCounterTestedModels()], this);
                System.out.println(Thread.currentThread().getName()+" is sending: "+ e.getClass());        ///////////////////////////////////////////////////////////////////////
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
                        myStudent.incrementModelCounter();
                            if(future.get().getResult() == "Good"){
                                System.out.println("PUBLISHED!!!"); /////////////////////////////////
                            PublishResultsEvent publishEvent = new PublishResultsEvent(currentModel);
                            try {
                                myStudent.setFuture(sendEvent(publishEvent));
                            }catch (NoSuchElementException ex){
                                System.out.println("The student sent the model to publish after the last conference ended. ");
                                future.resolve(currentModel);
                            }
                            }
                        myStudent.setFuture(null);
                        }
                }
            }
    }
}