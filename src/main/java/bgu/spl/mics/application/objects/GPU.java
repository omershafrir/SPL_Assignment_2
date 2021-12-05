package bgu.spl.mics.application.objects;

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
    private Type type;
    private int currentAvailableMemory;

    public GPU(Type type, Model model, Cluster cluster){
        this.type = type;
        this.model = model;
        this.cluster = cluster;

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

    public Type getType() {
        return type;
    }

    public int getCurrentAvailableMemory(){return currentAvailableMemory;}

    /**
    *@inv: sends data only if it has room to store it when it returns
    *@pre:
    **/
    public DataBatch sendUnprocessedData(){
        //TODO - send chunks of unprocessed data in batches of 100 sample
        return null;
    }

    public void trainModel(){
        //TODO
    }
    public void complete(){
        //TODO - sets the future of TrainModelEvent
        //working with message bus
    }

}
