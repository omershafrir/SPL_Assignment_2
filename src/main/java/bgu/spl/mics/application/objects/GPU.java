package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.application.objects.DataBatch;
/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU {


    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}
    private Model model;
    private Cluster cluster;
    public Type type;
    private Data data;
    private DataBatch db;
    private int currentAvailableMemory;

    public GPU(Type type, Cluster cluster, Model model){
        this.type = type;
        this.cluster = cluster;
        this.model = model;
        if (type.equals(0))
            currentAvailableMemory = 32;
        else if(type.equals(1))
            currentAvailableMemory = 16;
        else
             currentAvailableMemory = 8;
    }

    public Model getModel() {
        return model;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public DataBatch getDb() {return db;}

    public Type getType() {
        return type;
    }

    public int getCurrentAvailableMemory(){return currentAvailableMemory;}


    //divides the data into batches of 1000 samples - DataBatchs
    //and stores them on the disk
    /**
     * @INV:
     * student sent a "TrainingModel" Event
     * this GPU activated the CallBack function
     * model != null
     * @PRE:
     * data was loaded to the model
     * @POST:
     * The data has been divided into DataBatches containing 1000 samples each
     */
    public void divideDataIntoBatches(){
        //TODO
    }




    /**
    *@inv:
     * sends data only if it has room to store it when it returns
    *@pre:
     * currentAvailableMemory > this.model.getData().getSize()
    **/
    public DataBatch sendUnprocessedData(){
        //TODO - send chunks of unprocessed data in batches of 100 sample
        return null;
    }

    /**
     * @PRE:
     * CPU got data from GPU
     * the data has been processed
     * @POST:
     * complete() function is activated
     *
     **/
    //creating the model itself
    //"training it" - waiting the proper ticks - depend on the Type

    /**
     * @INV:
     * A CPU has processed the data and sent it back to this GPU
     * @PRE:
     * model.getStatus() == PreTrained
     * @POST:
     * model.getStatus() == Trained
     * future object got assigned with a value
     */
    public void trainModel(){
        //TODO
    }

    /**
     * @INV:
     *
     * @PRE:
     * data was sent to the cpu
     * @POST:
     * future.getValue() != null
     * the future object is getting a value
     *
     */
    public boolean complete(){
        //TODO - sets the future of TrainModelEvent
        //working with message bus
        return true;
    }

    /**
     *
     * @param model
     * @return
     * model with a resolute
     */
    public Model testModel(Model model) {
        //TODO
        return null;
    }

}
