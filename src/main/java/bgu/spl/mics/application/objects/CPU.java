package bgu.spl.mics.application.objects;

import java.awt.*;
import java.util.Collection;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */


public class CPU {
    private int cores;
    private Collection<DataBatch> data;
    private Cluster cluster;

    /**
     * constructor of CPU
     * @param cores Number of cores of the CPU
     * @param data The data which the CPU needs to process
     * @param cluster The single cluster instance which is connected to this CPU
     * @returns the CPU instance
     */
    public CPU(int cores, Collection<DataBatch> data, Cluster cluster){
        this.cores = cores;
        this.data = data;
        this.cluster = cluster;
    }

    /***
     * @pre
     * @inv
     * @post
     */
    public void processData(){
        /**
         * TODO - after the CPU got the raw data from the GPU
         * processing the data recieved from GPU
         **/
    }

    /***
     * @pre
     * @inv
     * @post
     */
    public DataBatch sendProcessedData(){
        /**
         * TODO - after the CPU got the raw data from the GPU
         * he processes it and sends it back
        **/
        return null;
    }
}
