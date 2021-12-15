package bgu.spl.mics.application.objects;

import java.util.Vector;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */


public class CPU3 {
    private int cores;
    private Vector<DataBatch> unProcessedData;
    private Vector<DataBatch> ProcessedData;
    private Cluster cluster;
    private int numberOfCPU;
    private int internalTimer;
    private DataBatch beingProcessed;
    private GPU  currentGPU;
    private boolean isProcessing;
    private int processStartTick;
    private static int counterOfCPU = 1;
    private static int totalTicksCounter = 0;

    /**
     * constructor of CPU
     *
     * @param cores   Number of cores of the CPU
     * @param cluster The single cluster instance which is connected to this CPU
     * @returns the CPU instance
     */
    public CPU3(int cores) {
        this.cores = cores;
        this.unProcessedData = new Vector<DataBatch>();
        this.ProcessedData =  new Vector<DataBatch>();
        this.cluster = Cluster.getInstance();
        this.internalTimer = 0;
        this.numberOfCPU = counterOfCPU;
        this.beingProcessed = null;
        this.isProcessing = false;
        this.processStartTick = -1;
        counterOfCPU++;
    }

    /**
     * @return num of cores
     */
    public int getCores() {
        return cores;
    }

    /**
     * updates the internal timer of the CPU for the data processing
     * checks if the there is a process in the making , if true , checks if new
     * tick is finishes the current processing
     */
    public void incrementTimer(){
        internalTimer++;
        if(isProcessing && (internalTimer - processStartTick
                == processingBatchRequiredTicks(beingProcessed))){
            finishProcess();
            ProcessedData.add(beingProcessed);
            totalTicksCounter++;
            if (unProcessedData.isEmpty())
                sendProcessedData();
                processNewData();
        }
        else if(isProcessing)   //incrementing totalTicksCounter when processing
            totalTicksCounter++;
        else if(!isProcessing)
            process();
    }

    /**
     * @return cluster
     */
    public Cluster getCluster() {
        return cluster;
    }

    /**
     * this method adds new DataBatch to the CPU unprocessed data collection
     *
     * @param newData new data that is being inserted to the collection
     * @pre @param newData != null
     * @post this.data.size() = @pre(this.data.size()) + 1
     * @post this.data.getLast() = @param newData
     */
    public void addData(DataBatch newData) {
        unProcessedData.add(newData);
    }

    /**
     *
     * @return the next dataBatch in the unProcessedData vector
     */
    public DataBatch getNextDataBatch(){
        return unProcessedData.remove(0);
    }

    /**
     * @param batch the batch that is going to be processed
     * @return number of ticks required to process this batch.
     */
    public int processingBatchRequiredTicks(DataBatch batch){
        int processingTime;
        if(batch.getData().getType().equals(Data.Type.Images))
            processingTime = (32/cores)*4;
        if(batch.getData().getType().equals(Data.Type.Text))
            processingTime = (32/cores)*2;
        else
            processingTime = (32/cores)*1;
        return processingTime;
    }

    /**
     * this method processes 1 data batch ,removes it from the data collection
     * and sends it back to the cluster
     *
     * @return processed data batch
     * @pre this.data.size() > 0
     * @post this.data.size() + 1 = @pre(this.data.size())
     * @post @return value DataBatch != null
     */
    public void processDataBatch() {
        isProcessing = true;
        beingProcessed = getNextDataBatch();
        processStartTick = internalTimer;
        }
    public void processDataBlock() {
        isProcessing = true;
        beingProcessed = getNextDataBatch();
        processStartTick = internalTimer;
    }
    /**
     * signals the current processing is finished
      */
    public void finishProcess(){
        isProcessing = false;
    }

    public void processNewData(){
        Vector<DataBatch> block = cluster.getDataBlock();

    }
    public void sendProcessedData(){
        cluster.addProcessedData(currentGPU , ProcessedData);
    }
    /**
     * @return true if cpu is processing data batch , else otherwise
     */
    public boolean isProcessing(){
        return isProcessing;
    }

    public static int getTotalTicksCounter(){
        return totalTicksCounter;
    }
    @Override
    public String toString() {
        return "CPU{" +
                "cores=" + cores +
                ", cluster=" + cluster +
                ", numberOfCPU=" + numberOfCPU +
                '}';
    }

}

