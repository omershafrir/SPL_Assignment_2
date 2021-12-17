package bgu.spl.mics.application.objects;

import java.util.Vector;

public class Statistics {
    private Cluster cluster;

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


