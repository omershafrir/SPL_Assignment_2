package bgu.spl.mics.application.objects;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.DataBatch;
import bgu.spl.mics.application.objects.Data;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */


public class CPU {
    private int cores;
    private Collection<DataBatch> data;
    private Cluster cluster;
    private int numberOfCPU;
    private static int counterOfCPU = 1;

    /**
     * constructor of CPU
     *
     * @param cores   Number of cores of the CPU
     * @param cluster The single cluster instance which is connected to this CPU
     * @returns the CPU instance
     */
    public CPU(int cores) {
        this.cores = cores;
        this.data = new LinkedList<DataBatch>();
//        this.cluster = cluster;
        this.numberOfCPU = counterOfCPU;
        counterOfCPU++;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }



    /**
     * @return num of cores
     */
    public int getCores() {
        return cores;
    }

    /**
     * @return collection of data batches
     */
    public Collection<DataBatch> getData() {
        return data;
    }

    /**
     * @return size of data batches collection
     */
    public int getDataSize() {
        return data.size();
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
        //TODO
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
    public DataBatch processData() {
//         TODO - after the CPU got the raw data from the GPU
        return null;
    }


    @Override
    public String toString() {
        return "CPU{" +
                "cores=" + cores +
                ", data=" + data +
                ", cluster=" + cluster +
                ", numberOfCPU=" + numberOfCPU +
                '}';
    }
}

