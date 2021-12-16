package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.DataBatch;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;

import java.util.*;
import java.util.concurrent.BlockingDeque;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    enum State{NotOccupied , Training , Testing}

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
        state = State.Training;
    }
    private void startTest(){
        state = State.Testing;
    }
    @Override
    protected void initialize() {
        GPUService self = this;
        //callback instructions for TrainModelEvent
        Callback<TrainModelEvent> instructionsTrain = new Callback<TrainModelEvent>() {
            @Override
            public void call(TrainModelEvent trainModelEvent) {
                self.currentEvent = trainModelEvent;
                if(state == State.Training)
                    awaitingEvents.addLast(trainModelEvent);
                if(state == State.Testing)
                    awaitingEvents.addLast(trainModelEvent);
                else{   //start processing TrainModelEvent

                    Model toTrain = trainModelEvent.getModel();
                    myGPU.setModel(toTrain);
                    myGPU.divideDataIntoBatches();
                    myGPU.sendUnprocessedData();
                    //start getting processed data
                    startTrain();
                    myGPU.continueTrainData();

                }
            }
        };

        //callback instructions for TestModelEvent
        Callback<TestModelEvent> instructionTest = new Callback<TestModelEvent>() {
            @Override
            public void call(TestModelEvent testModelEvent) {
                self.currentEvent = testModelEvent;
                if(state == State.Training)
                    awaitingEvents.addFirst(testModelEvent);
                if(state == State.Testing)
                    awaitingEvents.addFirst(testModelEvent);
                else {   //start training TrainModelEvent
                    startTest();
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
                    Model tested = testModelEvent.getModel();
                    tested.setResult(valueOfTest);
                    tested.setStatus("Tested");

                    complete(testModelEvent , tested);

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


        this.subscribeEvent(TrainModelEvent.class , instructionsTrain);
        this.subscribeEvent(TestModelEvent.class , instructionTest);
        this.subscribeBroadcast(TickBroadcast.class , instructionTimeTick);

    }
    public void afterTimeTickAction(Callback instructionsTrain ,Callback instructionTest){
        if(state == State.Training){
            boolean finished = myGPU.continueTrainData();
            if(finished){
                complete(currentEvent,myGPU.getModel());
                finishTask();
            }
        }

        // WE WILL NEVER GET A TICK WHILE TESTING BECAUSE TESTING IS INSTANT

        else{
            if (!awaitingEvents.isEmpty()){
                Event<Model> toExecute = awaitingEvents.pop();
                if(toExecute instanceof TrainModelEvent){
                    startTrain();
                    instructionsTrain.call(toExecute);
                }
                else {
                    startTest();
                    instructionTest.call(toExecute);
                }
            }

        }
    }
}
