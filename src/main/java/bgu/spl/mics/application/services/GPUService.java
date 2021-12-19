package bgu.spl.mics.application.services;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.*;
import bgu.spl.mics.application.outputFileCreator;

import java.util.*;
import java.util.concurrent.BlockingDeque;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the { DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    enum State{NotOccupied ,WaitingForProcessedData , Training , Testing}

    private GPU myGPU;
    private Cluster cluster;
    private Deque<Event> awaitingEvents;
    private State state;
    private int remainingTicksToTrain;
    private Event<Model> currentEvent;


    public GPUService(String name, GPU myGPU) {
        super(name);
        this.myGPU = myGPU;
        cluster = Cluster.getInstance();
        state = State.NotOccupied;
        remainingTicksToTrain = 0;
        awaitingEvents = new LinkedList<>();
        currentEvent = null;
    }
    private void finishTask(){
        state = State.NotOccupied;
    }
    private void startTrain(){
//        cluster.addToUnhandled(myGPU);
        state = State.Training;
    }
    private void startTest(){
        state = State.Testing;
    }
//    private void startWait(){
//        state = State.WaitingForProcessedData;
//    }

    @Override
    protected void initialize() {
        MessageBusImpl.getInstance().register(this);
        GPUService self = this;
        //callback instructions for TrainModelEvent
        Callback<TrainModelEvent> instructionsTrain = new Callback<TrainModelEvent>() {
            @Override
            public void call(TrainModelEvent trainModelEvent) {
//                self.currentEvent = trainModelEvent;            ///////////////////////////////////////////@@@///////////////////////////
                if(state == State.Training)
                    awaitingEvents.addLast(trainModelEvent);
                else if(state == State.Testing)
                    awaitingEvents.addLast(trainModelEvent);
                else{   //start processing TrainModelEvent
                    self.currentEvent = trainModelEvent;        ///////////////////////////////////////////@@@///////////////////////////
                        startTrain();
//                    System.out.println("STEP 1 : THE MODEL HAS BEGUN TRAINING:   "+trainModelEvent.getModel().getName());    /////////////////////////////////////
                    Model toTrain = trainModelEvent.getModel();
                    myGPU.setModel(toTrain);
                    myGPU.getModel().setStatus("Training"); // change the model status!
                    myGPU.setData(toTrain.getData());
//                    System.out.println("THE MODEL THAT "+ getName() + " START TRAINING IS " + toTrain.getName());
                    myGPU.divideDataIntoBatches();
                    myGPU.sendUnprocessedData();
                    //start getting processed data
                    myGPU.continueTrainData();

                }
            }
        };

        //callback instructions for TestModelEvent
        Callback<TestModelEvent> instructionTest = new Callback<TestModelEvent>() {
            @Override
            public void call(TestModelEvent testModelEvent) {
//                self.currentEvent = testModelEvent;             ///////////////////////////////////////////@@@///////////////////////////
                if(state == State.Training){
                    awaitingEvents.addFirst(testModelEvent);
                }
                else if(state == State.Testing) {
                    awaitingEvents.addFirst(testModelEvent);
                }
                else {   //start training TrainModelEvent
                    self.currentEvent = testModelEvent;             ///////////////////////////////////////////@@@///////////////////////////
                    startTest();
//                    System.out.println("THE MODEL THAT "+ getName() + " START TESTING IS " + testModelEvent.getModel().getName());     /////////////////////////               startTest();
                    String valueOfTest;
                    Random gen = new Random();
                    int prob = gen.nextInt(100);
                    //function with prob of 0.6
                    if(testModelEvent.getSenderStatus().equals("MSc")){
                        if(prob <= 60){
                            valueOfTest = "Good";
                        }
                        else
                            valueOfTest = "Bad";
                    }
                    //PhD case
                    else{
                        //function with prob of 0.8
                        if(prob <= 80){
                            valueOfTest = "Good";
                        }
                        else
                            valueOfTest = "Bad";
                    }
                    myGPU.incrementGPUTimeUsage();          // for statistics
                    Model tested = testModelEvent.getModel();
                    tested.setResult(valueOfTest);
                    tested.setStatus("Tested");             // change the model status!
                    Statistics.counterOfTested.incrementAndGet();       /////////////////////////////////////////@@@?///////////////////////

                    complete(testModelEvent , tested);
//                    System.out.println("STEP 4: THE CURRENT MODEL HAS FINISHED THE TEST :" + myGPU.getModel().getName());//////////////////////////////////////////
                    finishTask();
                }
            }
        };

        //callback instructions for TickBroadcast
        Callback<TickBroadcast> instructionTimeTick = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                myGPU.incrementTimer();
                afterTimeTickAction(instructionsTrain , instructionTest);
            }
        };

        //callback instructions for TerminateBroadcast
        Callback<TerminateBroadcast> instructionsForTerminate =
                new Callback<TerminateBroadcast>() {
                    @Override
                    public void call(TerminateBroadcast c) {
                        terminate();
                    }
                };

        subscribeBroadcast(TickBroadcast.class , instructionTimeTick);
        subscribeEvent(TrainModelEvent.class , instructionsTrain);
        subscribeEvent(TestModelEvent.class , instructionTest);
        subscribeBroadcast(TerminateBroadcast.class , instructionsForTerminate);


    }
    public void afterTimeTickAction(Callback instructionsTrain ,Callback instructionTest){
        if(state == State.Training){
            if(myGPU.getModel().getName().equals("VIT")){
            }
            boolean finished = myGPU.continueTrainData();
            if(myGPU.getModel().getName().equals("VIT")){               /////////////////////////
            }
            if(finished){
                myGPU.getModel().setStatus("Trained");         // change the model status!
                finishTask();
                complete(currentEvent,myGPU.getModel());
            }
        }

        // WE WILL NEVER GET A TICK WHILE TESTING BECAUSE TESTING IS INSTANT

        else{   // current state is NotOccupied
            if (!awaitingEvents.isEmpty()){
                Event<Model> toExecute = awaitingEvents.pop();
                if(toExecute instanceof TrainModelEvent){
                    instructionsTrain.call(toExecute);
                }
                if(toExecute instanceof TestModelEvent){
                    instructionTest.call(toExecute);
                }
            }

        }
    }
}
