package bgu.spl.mics.application.objects;


import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Vector;

import bgu.spl.mics.MessageBusImpl;
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
	private ConcurrentHashMap<Boolean , Vector<GPU>> boolToGPU;
	private GPU nextTreatedGPU;
	private GPU gpuToSend;
	private int numOfCPUS;


	private Cluster(){
		statistics = null;
		trainedModels = new Vector<Model>();
		nextTreatedGPU = null;
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
	public synchronized ConcurrentHashMap<GPU, Vector<DataBatch>> getGPUToUnProcessed() {
		return GPUToUnProcessed;
	}

	public synchronized ConcurrentHashMap<GPU, Vector<DataBatch>> getGPUToProcessed() {
		return GPUToProcessed;
	}


	public void initializeCluster(){
		GPUToUnProcessed = new ConcurrentHashMap<>();
		GPUToProcessed = new ConcurrentHashMap<>();
		boolToGPU = new ConcurrentHashMap<>();
		boolToGPU.put(Boolean.FALSE , new Vector<GPU>());
		boolToGPU.put(Boolean.TRUE , new Vector<GPU>());
		for (GPU gpu: GPUArray){	//TODO : check if necessary
			GPUToUnProcessed.put(gpu ,new Vector<DataBatch>());
			GPUToProcessed.put(gpu ,new Vector<>());
		}
	}

	public GPU[] getGPUArray() {
		return GPUArray;
	}
	public CPU[] getCPUArray() {
		return CPUArray;
	}

	/**
	 * sends the unprocessed data to the relevent CPU.
	 * @return vector of dataBatches that a CPU is going to process.
	 * @nextTreatedGPU is the GPU of which the data was taken from.
	 * before function ends , @nextTreatedGPU is updated.
	 */
	public Vector<DataBatch> getUnprocessedData(){
		Vector<DataBatch> toProcesse = new Vector<>();
		Vector<DataBatch> toUpdate = new Vector<>();
		for (Vector<DataBatch> vec : GPUToUnProcessed.values()){
			if(!vec.isEmpty()){
				for(GPU gpu : GPUArray){
					if(GPUToUnProcessed.get(gpu).equals(vec)){
						gpuToSend = gpu;
						break;
					}
				}
				toUpdate = vec;
				break;
			}
		}
		synchronized (Cluster.getInstance()) {
			//40 was chosen as a relevant size - can be changed
			if (toUpdate.size() > 100) {
				for (int i = 0; i < 100; i++) {
					toProcesse.add(toUpdate.remove(i));
				}
			} else {
				for (int i = 0; i < toUpdate.size(); i++) {
					toProcesse.add(toUpdate.remove(i));
				}
			}
		}
		return toProcesse;
	}
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

	public boolean dataBatchesAreWaiting(GPU gpu){
		return !GPUToProcessed.get(gpu).isEmpty();
	}

	/**
	 * adding the unprocessed data to the relevent GPU unprocessed data vector
	 * @param gpu the GPU of which data will be processed
	 * @param unprocessedData the unprocessed data
	 */
	public void addUnProcessedData(GPU gpu,Vector<DataBatch> unprocessedData){
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

//	/**
//	 * updates the next GPU that it's data is going to be processed
//	 * 		if there is one such that no CPU is processing, then he will be chosen
//	 * 		else , the first GPU of the treated ones will be chosen
//	 */
//	public synchronized void updateNextTreatedGPU(){
//		if (!boolToGPU.get(Boolean.FALSE).isEmpty()){
//			nextTreatedGPU = boolToGPU.get(Boolean.FALSE).firstElement();
//			addToHandled(nextTreatedGPU);
//			removeFromUnhandled(nextTreatedGPU);
//		}
//		else{
////			System.out.println("NEXT TRUE GPU , IN TRUE: " + nextTreatedGPU.hashCode());/////////////////////////////////////////////////////////////////////////////////////////
//			nextTreatedGPU = boolToGPU.get(Boolean.TRUE).firstElement();
//		}
//	}

//	public synchronized void removeFromHandled(GPU gpu){
//		boolToGPU.get(Boolean.TRUE).remove(gpu);
//	}
//
//	public synchronized void addToHandled(GPU gpu){
//		boolToGPU.get(Boolean.TRUE).add(gpu);
//	}
//
//	public synchronized void removeFromUnhandled(GPU gpu){
//		boolToGPU.get(Boolean.FALSE).remove(gpu);
//	}
//
//	public synchronized void addToUnhandled(GPU gpu){
//		boolToGPU.get(Boolean.FALSE).add(gpu);
//	}

	public Vector<Model> getTrainedModels(){
		return trainedModels;
	}


}
