package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.application.objects.DataBatch;

import java.util.Vector;

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
    private Vector<DataBatch> divided_data;
    private DataBatch db;
    private int currentAvailableMemory;



    public GPU(String type){
        if (type.equals("RTX3090")){
            currentAvailableMemory = 32;
            this.type = Type.RTX3090;
        }
        else if(type.equals("RTX2080")){
            currentAvailableMemory = 16;
            this.type = Type.RTX2080;
        }

        else{
            currentAvailableMemory = 8;
            this.type = Type.GTX1080;
        }

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

    public void setModel(Model model) {
        this.model = model;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void setData(Data data) {
        this.data = data;
    }
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
        Vector<DataBatch> dataBatchVector = new Vector<DataBatch>();
        for(int i = 0;i < data.getSize(); i = i + 1000){
            dataBatchVector.add(new DataBatch(data,i));
        }
        divided_data = dataBatchVector;
    }

    /**
    *@inv:
     * sends data only if it has room to store it when it returns
    *@pre:
     * currentAvailableMemory > this.model.getData().getSize()
    **/
    //going to send through cluster to CPU
    public synchronized void sendUnprocessedData(){
        //TODO - send chunks of unprocessed data in batches of 1000 sample
        cluster.AddToCPUunProcessedData(this.divided_data);
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


    @Override
    public String toString() {
        return "GPU{" +
                "model=" + model +
                ", cluster=" + cluster +
                ", type=" + type +
                ", data=" + data +
                ", db=" + db +
                ", currentAvailableMemory=" + currentAvailableMemory +
                '}';
    }

}
