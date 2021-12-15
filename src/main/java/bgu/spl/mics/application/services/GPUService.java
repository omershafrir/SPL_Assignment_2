package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.DataBatch;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.BlockingDeque;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private GPU myGPU;
    private Cluster cluster;

    public GPUService(String name, GPU myGPU) {
        super(name);
        this.myGPU = myGPU;
        cluster = Cluster.getInstance();
    }

    @Override
    protected void initialize() {
        Callback<TrainModelEvent> instructions_train = new Callback<TrainModelEvent>() {
            @Override
            public void call(TrainModelEvent event) {
                Model toTrain = event.getModel();
                myGPU.setModel(toTrain);
                myGPU.divideDataIntoBatches();
                myGPU.sendUnprocessedData();
                try {
                    Vector<DataBatch> x = cluster.getGPUToProcessed().get(myGPU).take();
                } catch(Exception ex){}

            }
        };

        this.subscribeEvent(TrainModelEvent.class , instructions_train);
        Callback<TestModelEvent> instruction_test = new Callback<TestModelEvent>() {
            @Override
            public void call(TestModelEvent testModelEvent) {
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

                Future<Model> testFuture = testModelEvent.getFuture();
                Model toUpdate = testModelEvent.getModel();
                //updating the status of the model
                toUpdate.setResult(valueOfTest);
                toUpdate.setStatus("Tested");
                //updating the future of the model after the test
                testFuture.resolve(toUpdate);

                /*
                this type of
                    event is processed instantly, and will return results on the model, will return ‘Good’
                    results with a probability of 0.6 for MSc student, and 0.8 for PhD student. (yes this is
                    random), when the GPU finish handling the event it will update the object, and set
                    the future via the MessageBus, so the Student can see the change.
                */
            }
        };
        this.subscribeEvent(TestModelEvent.class , instruction_test);

    }
}
