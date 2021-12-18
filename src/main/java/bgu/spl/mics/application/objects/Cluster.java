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
	public synchronized Vector<DataBatch> getUnprocessedData(){
			updateNextTreatedGPU();
			Vector<DataBatch> unprocessedData = new Vector<>();
			Vector<DataBatch> releventCPUVec = GPUToUnProcessed.get(nextTreatedGPU);
			int initialSize = releventCPUVec.size();
			for (int i=0 ; i < nextTreatedGPU.getCurrentModelSize()/(numOfCPUS*1000) &&  !releventCPUVec.isEmpty(); i++){
				unprocessedData.add(releventCPUVec.remove(0));
			}
		System.out.println("THE SIZE OF THE BLOCK IS:" + unprocessedData.size());	//////////////////////////////////
		gpuToSend = nextTreatedGPU;
		return unprocessedData;
	}
	public synchronized GPU getUnprocessedDataGPU(){
		return gpuToSend;
	}

	/**
	 * @return TRUE if there is unprocessed data in the cluster
	 * 		   FALSE otherwise
	 */
	public synchronized boolean isThereDataToProcess(){
		for (Vector<DataBatch> vec : GPUToUnProcessed.values()){

			if (!vec.isEmpty()) {
//				System.out.println("THERE ARE BATCHES WAITINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"); 	///////////////////////////
//				System.out.println("THE VECTOR : "+vec);                /////////////////////////////////////////////////////////
				return true;
			}
		}
		return false;
	}

	public synchronized Vector<DataBatch> getProcessedData(GPU gpu){
		Vector<DataBatch> data = GPUToProcessed.get(gpu);
		Vector<DataBatch> processedData = new Vector<>();
		for (int i = 0; i < gpu.getCurrentAvailableMemory() && !data.isEmpty(); i++) {
			processedData.add(data.remove(0));
		}
		return processedData;
	}

	public synchronized boolean dataBatchesAreWaiting(GPU gpu){
		return !GPUToProcessed.get(gpu).isEmpty();
	}
	/**
	 * updates the next GPU that it's data is going to be processed
	 * 		if there is one such that no CPU is processing, then he will be chosen
	 * 		else , the first GPU of the treated ones will be chosen
	 */
	public synchronized void updateNextTreatedGPU(){
		if (!boolToGPU.get(Boolean.FALSE).isEmpty()){
			nextTreatedGPU = boolToGPU.get(Boolean.FALSE).firstElement();
			addToHandled(nextTreatedGPU);
			removeFromUnhandled(nextTreatedGPU);
		}
		else{
//			System.out.println("NEXT TRUE GPU , IN TRUE: " + nextTreatedGPU.hashCode());/////////////////////////////////////////////////////////////////////////////////////////
			nextTreatedGPU = boolToGPU.get(Boolean.TRUE).firstElement();
		}
	}

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
	public synchronized void addProcessedData(GPU gpu,Vector<DataBatch> processedDataBlock){
		System.out.println(	);													///////////////////////////////////////////////
		System.out.println("PROCESSED DATA VECTOR SIZE IS: "+processedDataBlock.size()); ///////////////////////////////////////////////
		Vector<DataBatch> thisGPUWaitingProcessed = GPUToProcessed.get(gpu);
		thisGPUWaitingProcessed.addAll(processedDataBlock);

	}

	public synchronized void removeFromHandled(GPU gpu){
		boolToGPU.get(Boolean.TRUE).remove(gpu);
	}

	public synchronized void addToHandled(GPU gpu){
		boolToGPU.get(Boolean.TRUE).add(gpu);
	}

	public synchronized void removeFromUnhandled(GPU gpu){
		boolToGPU.get(Boolean.FALSE).remove(gpu);
	}

	public synchronized void addToUnhandled(GPU gpu){
		boolToGPU.get(Boolean.FALSE).add(gpu);
	}

	public Vector<Model> getTrainedModels(){
		return trainedModels;
	}


}
