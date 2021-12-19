package bgu.spl.mics.application.objects;


import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Vector;

import bgu.spl.mics.MessageBusImpl;
import jdk.internal.util.xml.impl.Pair;
import org.omg.CORBA.CharSeqHelper;

import java.util.Vector;
import java.util.concurrent.*;

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
	private ConcurrentHashMap<GPU , Vector<DataBatch>> GPUToUnProcessed;
	private ConcurrentHashMap<GPU , Vector<DataBatch>> GPUToProcessed;
	private GPU gpuToSend;
	private int numOfCPUS;


	private Cluster(){
		statistics = null;
		trainedModels = new Vector<Model>();
		gpuToSend = null;
	}

	public static Cluster getInstance(){
		return instance;
	}

	/**
	 * cluster instance initializing:
	 * @param GPUArray
	 */
	public void setGPUArray(GPU[] GPUArray) {
		this.GPUArray = GPUArray;
	}
	public void setCPUArray(CPU[] CPUArray) {
		this.CPUArray = CPUArray;
		numOfCPUS = CPUArray.length;
	}
	public void initializeCluster(){
		GPUToUnProcessed = new ConcurrentHashMap<>();
		GPUToProcessed = new ConcurrentHashMap<>();
		for (GPU gpu: GPUArray){	//TODO : check if necessary
			GPUToUnProcessed.put(gpu ,new Vector<DataBatch>());
			GPUToProcessed.put(gpu ,new Vector<>());
		}
	}

	/**
	 * sends the unprocessed data to the relevent CPU.
	 * @return vector of dataBatches that a CPU is going to process.
	 * @nextTreatedGPU is the GPU of which the data was taken from.
	 * before function ends
	 * , @nextTreatedGPU is updated.
	 */
	public Object[] getUnprocessedData(){
		synchronized (this) {
		Vector<DataBatch> toProcesse = new Vector<>();
		Object[] random_output= getRandomGPU();
		Vector<DataBatch> toUpdate = (Vector<DataBatch>) random_output[1];
		gpuToSend = (GPU) random_output[0];

		//////////////////test
//		synchronized (this){
				//50 was chosen as a relevant size - can be changed
				if (toUpdate.size() > 2) {
					for (int i = 0; i < 2; i++) {
						toProcesse.add(toUpdate.remove(i));
					}
				} else {
					for (int i = 0; i < toUpdate.size(); i++) {
						toProcesse.add(toUpdate.remove(i));
					}
				}
//			}
			random_output[1] = toProcesse;    ////////added line
			return random_output;
		}
	}

	private Object[] getRandomGPU(){
		Object[] output = new Object[2];
		HashMap<GPU,Vector<DataBatch>> x = new HashMap<>();

		for(GPU gpu : GPUToUnProcessed.keySet()){
			Vector<DataBatch> vec = GPUToUnProcessed.get(gpu);
			if(!vec.isEmpty()) {
				x.put(gpu, vec);
			}
		}
		int index = (int) ((Math.random())*(x.size()));
		int i=0;
		for (GPU gpu : x.keySet()){
			if(i == index){
				output[0] = gpu;
				output[1] = x.get(gpu);
			}
			i++;
		}
		return output;
 	}

	 ////////////////////////deleted Sync
	public synchronized GPU getUnprocessedDataGPU(){
		return gpuToSend;
	}

	/**
	 * @return TRUE if there is unprocessed data in the cluster
	 * 		   FALSE otherwise
	 */
	public boolean isThereDataToProcess(){
		synchronized (GPUToUnProcessed) {
			for (Vector<DataBatch> vec : GPUToUnProcessed.values()) {
				if (!vec.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	public Vector<DataBatch> getProcessedData(GPU gpu){
		synchronized (GPUToProcessed) {
			Vector<DataBatch> data = GPUToProcessed.get(gpu);
			Vector<DataBatch> processedData = new Vector<>();
			for (int i = 0; i < gpu.getCurrentAvailableMemory() && !data.isEmpty(); i++) {
				processedData.add(data.remove(0));
			}
			return processedData;
		}
	}


	////////////////////////deleted Sync
	public boolean dataBatchesAreWaiting(GPU gpu){
		synchronized (GPUToProcessed.get(gpu)) {
			return !GPUToProcessed.get(gpu).isEmpty();
		}
	}
	/**
	 * updates the next GPU that it's data is going to be processed
	 * 		if there is one such that no CPU is processing, then he will be chosen
	 * 		else , the first GPU of the treated ones will be chosen
	 */

	/**
	 * adding the unprocessed data to the relevent GPU unprocessed data vector
	 * @param gpu the GPU of which data will be processed
	 * @param unprocessedData the unprocessed data
	 */
	public synchronized void addUnProcessedData(GPU gpu,Vector<DataBatch> unprocessedData){
		GPUToUnProcessed.put(gpu , unprocessedData);
	}

	/**
	 * adding the processed data to the relevent GPU processed data vector
	 * @param gpu the GPU of which data has been processed
	 * @param processedDataBlock the completed processed data
	 */
	public void addProcessedData(GPU gpu,Vector<DataBatch> processedDataBlock){
		Vector<DataBatch> thisGPUWaitingProcessed = GPUToProcessed.get(gpu);
		thisGPUWaitingProcessed.addAll(processedDataBlock);

	}

	public Vector<Model> getTrainedModels(){
		return trainedModels;
	}


}
