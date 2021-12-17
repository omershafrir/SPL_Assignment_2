package bgu.spl.mics.application.objects;


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
			boolToGPU.get(Boolean.FALSE).add(gpu);

		}
		nextTreatedGPU = GPUArray[0];
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

			Vector<DataBatch> unprocessedData = new Vector<>();
			Vector<DataBatch> releventCPUVec = GPUToUnProcessed.get(nextTreatedGPU);
//			System.out.println("THE VECTOR ASHKARA: "+releventCPUVec);			///////////////////////////////////////////////////////////////////////////
			int initialSize = releventCPUVec.size();
			for (int i=0 ; i < initialSize/numOfCPUS &&  !releventCPUVec.isEmpty(); i++){
				unprocessedData.add(releventCPUVec.remove(0));
			}
		gpuToSend = nextTreatedGPU;
		updateNextTreatedGPU();
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
		for (int i = 0; i < gpu.getCurrentAvailableMemory(); i++) {
			data.add(data.remove(0));
		}
		return data;
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
			System.out.println("NEXT TRUE GPU , IN FALSE: " + nextTreatedGPU.hashCode());/////////////////////////////////////////////////////////////////////////
//			Vector<GPU> x = boolToGPU.get(Boolean.FALSE);
//			nextTreatedGPU = x.firstElement();
//			System.out.println("VECTOR OF GPUS:  "+x);		/////////////////////////////////////////////////////////////////////////
//			x.remove(0);
//			System.out.println("--------------------------------------------------------");			///////////////////////////////////////////////////////
//			System.out.println("WHO should we help?   ");						///////////////////////////////////////////////////////
//			System.out.println("THIS GPU!!!!! :   "+nextTreatedGPU.hashCode());						///////////////////////////////////////////////////////
//			boolToGPU.put(Boolean.FALSE, x);
			nextTreatedGPU = boolToGPU.get(Boolean.FALSE).remove(0);
			boolToGPU.get(Boolean.TRUE).add(nextTreatedGPU);
		}
		else{
			System.out.println("NEXT TRUE GPU , IN TRUE: " + nextTreatedGPU.hashCode());/////////////////////////////////////////////////////////////////////////////////////////
			nextTreatedGPU = boolToGPU.get(Boolean.TRUE).firstElement();
		}
	}

	/**
	 * adding the unprocessed data to the relevent GPU unprocessed data vector
	 * @param gpu the GPU of which data will be processed
	 * @param unprocessedData the unprocessed data
	 */
	public synchronized void addUnProcessedData(GPU gpu,Vector<DataBatch> unprocessedData){
//		boolToGPU.get(Boolean.TRUE).add(gpu);
		GPUToUnProcessed.put(gpu , unprocessedData);
	}

	/**
	 * adding the processed data to the relevent GPU processed data vector
	 * @param gpu the GPU of which data has been processed
	 * @param processedDataBlock the completed processed data
	 */
	public synchronized void addProcessedData(GPU gpu,Vector<DataBatch> processedDataBlock){
		Vector<DataBatch> thisGPUWaitingProcessed = GPUToProcessed.get(gpu);
		thisGPUWaitingProcessed.addAll(processedDataBlock);

//		Vector<DataBatch> vector = GPUToProcessed.get(gpu).getLast();
//			while (vector.size() < gpu.getCurrentAvailableMemory()
//						&& !processedData.isEmpty()){
//			vector.add(processedData.remove(0));
//		}
//			while (!processedData.isEmpty()){
//				Vector<DataBatch> x = new Vector<>();
//				for(int i = 0;!processedData.isEmpty()
//						&& i < gpu.getCurrentAvailableMemory(); i++){
//					x.add(processedData.remove(0));
//				}
//				GPUToProcessed.get(gpu).add(x);
//			}
	}

	public Vector<Model> getTrainedModels(){
		return trainedModels;
	}


}
