package bgu.spl.mics.application.objects;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    private Cluster cluster;
    public static AtomicInteger counterOfDead = new AtomicInteger(0);            /////////////////@@@//////////////////////////
    public static AtomicInteger counterOfTested = new AtomicInteger(0);            /////////////////@@@//////////////////////////
    private static Vector<String> conferencesResults = new Vector<>();

    public Statistics(Cluster cluster) {
        this.cluster = cluster;
//        conferencesResults = new Vector<>();
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

    public static Vector<String> getConferencesResults() {
        return conferencesResults;
    }

    public static void addConferenceStatistics(ConfrenceInformation con){
        String s = con.getName() +" : " ;
        for(Model model : con.getModels()){
            s += model.getName() + " , ";
        }
        conferencesResults.add(s);
    }

}


