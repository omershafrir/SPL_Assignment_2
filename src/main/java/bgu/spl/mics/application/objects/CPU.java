package bgu.spl.mics.application.objects;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU {

    private int cores;
    private Vector<DataBatch> unProcessedData;
    private Vector<DataBatch> processedData;
    private Cluster cluster;
    private int numberOfCPU;
    private int internalTimer;
    private GPU currentGPU;
    private boolean isProcessing;
    private int processStartTick;
    private int currentProcessRequiredTime;
    private static int counterOfCPU = 1;
    private static AtomicInteger totalTicksCounter = new AtomicInteger(0);
    private static AtomicInteger totalBatchesProcessed = new AtomicInteger(0);

    /**
     * constructor of CPU
     *
     * @param cores   Number of cores of the CPU
     * @returns the CPU instance
     */
    public CPU(int cores) {
        this.cores = cores;
        this.unProcessedData = new Vector<DataBatch>();
        this.processedData = new Vector<DataBatch>();
        this.cluster = Cluster.getInstance();
        this.internalTimer = 0;
        this.numberOfCPU = counterOfCPU;
        this.isProcessing = false;
        this.processStartTick = -1;
        this.currentProcessRequiredTime = -1;
        counterOfCPU++;
    }

    /**
     * @return num of cores
     */
    public int getCores() {
        return cores;
    }

    /**
     * @return cluster
     */
    public Cluster getCluster() {
        return cluster;
    }

    public void incrementTimer() {
        internalTimer++;
        afterTickAction();
    }

    public void afterTickAction() {
        if (isProcessing) {     //in the middle of processing
            System.out.println(Thread.currentThread().getName() + "IS PROCESSING DATA");
            incrementCounter();
            if (internalTimer ==    //current batch processing finished
                    currentProcessRequiredTime + processStartTick) {
                incrementBatchesCounter();
                if (unProcessedData.size() == 1) {   //finished current block
                    finishProcessCurrentBatch();
                    finishProcessCurrentBlock();
                } else {       //current block is not finished
                    finishProcessCurrentBatch();
                    startProcessNextBatch();
                }
            } else ;       // current batch is not finished
        }
        else {       //isnt is the middle of processing
            synchronized (cluster) {
                if (cluster.isThereDataToProcess()) {   //there is data waiting in the cluster
                    startProcessNextBlock();
                    startProcessNextBatch();
                } else ;                                //there isnt data waiting in the cluster
            }
        }
    }
    //////////////////////////////////////////////////////SYNC///////////////////////////////////////////////////////////////////////////
    public synchronized void startProcessNextBatch() {
        processStartTick = internalTimer;
        currentProcessRequiredTime =
                processingBatchRequiredTicks(unProcessedData.elementAt(0));
        isProcessing = true;
    }

    public synchronized void finishProcessCurrentBatch() {
        processedData.add(unProcessedData.remove(0));
        isProcessing = false;
    }

    public synchronized void startProcessNextBlock() {
        processedData.removeAllElements();
        unProcessedData = cluster.getUnprocessedData();
        currentGPU = cluster.getUnprocessedDataGPU();
        System.out.println(); ////////////////////////////////////////////////////////////////
        System.out.println("UNPROCESSED DATA VECTOR SIZE IS: "+unProcessedData.size()); ///////////////////////////////////////////////////////////////////////
        System.out.println();   ////////////////////////////////////////////////////////////////

    }

    public synchronized void finishProcessCurrentBlock() {
//        System.out.println("PROCESSSSSSSED: "+ processedData.size());   /////////////////////////////
        cluster.addProcessedData(currentGPU, processedData);
        synchronized (cluster) {
            if (cluster.isThereDataToProcess()) {
                startProcessNextBlock();
                startProcessNextBatch();
            }
        }
    }

    public int processingBatchRequiredTicks(DataBatch batch) {
        int processingTime;
        if (batch.getData().getType().equals(Data.Type.Images))
            processingTime = (32 / cores) * 4;
        if (batch.getData().getType().equals(Data.Type.Text))
            processingTime = (32 / cores) * 2;
        else
            processingTime = (32 / cores) * 1;
        return processingTime;
    }

    public static int getTotalTicksCounter() {
        return totalTicksCounter.intValue();
    }

    public static int getTotalBatchesProcessed() {
        return totalBatchesProcessed.intValue();
    }

    public static void incrementCounter(){
        totalTicksCounter.incrementAndGet();
    }

    public static void incrementBatchesCounter(){totalBatchesProcessed.incrementAndGet();};
    @Override
    public String toString() {
        return "CPU{" +
                "cores=" + cores +
                ", cluster=" + cluster +
                ", numberOfCPU=" + numberOfCPU +
                '}';
    }
}


