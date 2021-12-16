package bgu.spl.mics.application.objects;

import java.util.Vector;

public class Statistics {
    private Cluster cluster;

    public Statistics(Cluster cluster) {
        this.cluster = cluster;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public String namesOfAllTrainedModels(){
        String names="";
        Vector<Model> trainedModels = cluster.getTrainedModels();
        for (Model m : trainedModels)
            names = names + m.getName();
        return names;
    }

    public int totalNumDataBatchesProcessed(){
        return cluster.dataBatchesProcessed();
    }

    public int NumberOfCPUTimeUnitsUsed(){
        return CPU.getTotalTicksCounter();
    }

    public int NumberOfGPUTimeUnitsUsed(){
        int output = 0;
        GPU[] gpuArray = cluster.getGPUArray();
        for(GPU gpu : gpuArray){
            output += gpu.NumberOfGPUTimeUnitsUsed();
        }
        return output;
    }


}


