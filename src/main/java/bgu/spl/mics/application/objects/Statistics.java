package bgu.spl.mics.application.objects;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    private Cluster cluster;
    public static AtomicInteger counterOfDead = new AtomicInteger(0);            /////////////////@@@//////////////////////////
    public static AtomicInteger counterOfTested = new AtomicInteger(0);            /////////////////@@@//////////////////////////

    public Statistics(Cluster cluster) {
        this.cluster = cluster;
    }

    public String namesOfAllTrainedModels(){
        String names="";
        Vector<Model> trainedModels = cluster.getTrainedModels();
        for (Model m : trainedModels)
            names = names + m.getName();
        return names;
    }

    public int totalNumDataBatchesProcessed(){
        return CPU.getTotalBatchesProcessed();
    }

    public int NumberOfCPUTimeUnitsUsed(){
        return CPU.getTotalTicksCounter();
    }

    public int NumberOfGPUTimeUnitsUsed(){
        return GPU.getTotalTicksCounter();
    }


}


