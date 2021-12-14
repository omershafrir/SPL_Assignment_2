package bgu.spl.mics.application.objects;


import java.util.Vector;

import bgu.spl.mics.MessageBusImpl;

import java.util.Vector;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private static Cluster instance = new Cluster();
	private GPU[] GPUArray;
	private CPU[] CPUArray;
	private Statistics statistics;
	private Vector<Model> trainedModels;
	private int dataBatchesProcessed;

	private Cluster(){
		statistics = null;
		trainedModels = new Vector<Model>();
		dataBatchesProcessed = 0;
	}

	public void setGPUArray(GPU[] GPUArray) {
		this.GPUArray = GPUArray;
	}

	public void setCPUArray(CPU[] CPUArray) {
		this.CPUArray = CPUArray;
	}

	public static Cluster getInstance(){
		return instance;
	}

	public GPU[] getGPUArray() {
		return GPUArray;
	}
	public CPU[] getCPUArray() {
		return CPUArray;
	}
	//gets the unProcessed data from GPU
	//and sends it to an available CPU
	public void AddToCPUunProcessedData(Vector<DataBatch> to_send){
		//TODO - send unProcessed to a CPU
	}

	public Vector<Model> getTrainedModels(){
		return trainedModels;
	}

	public int dataBatchesProcessed(){
		return dataBatchesProcessed;
	}
	//TODO - function that indicates if the data was sent from the GPU through the Cluster to the CPU
	public boolean WasSentUnProcessedDataToCPU(){
		return true;
	}

	//TODO - after the data was processed will indicate that the data was sent back to the GPU for training the model
	public boolean WasSentProcessedDataToGPU(){
		return true;
	}

}
