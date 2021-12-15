package bgu.spl.mics.application.objects;


import java.util.HashMap;
import java.util.Vector;

import bgu.spl.mics.MessageBusImpl;
import org.omg.CORBA.CharSeqHelper;

import java.util.Vector;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

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
	private HashMap<GPU , Vector<DataBatch>> GPUToUnProcessed;
	private HashMap<GPU , BlockingDeque<Vector<DataBatch>>> GPUToProcessed;
	private HashMap<Boolean , Vector<GPU>> boolToGPU;
	private GPU nextTreatedGPU;
	private GPU gpuToSend;
	private int numOfCPUS;


	private Cluster(){
		statistics = null;
		trainedModels = new Vector<Model>();
		dataBatchesProcessed = 0;
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
	public synchronized HashMap<GPU, Vector<DataBatch>> getGPUToUnProcessed() {
		return GPUToUnProcessed;
	}

	public synchronized HashMap<GPU, BlockingDeque<Vector<DataBatch>>> getGPUToProcessed() {
		return GPUToProcessed;
	}


	public void initializeCluster(){
		GPUToUnProcessed = new HashMap<>();
		GPUToProcessed = new HashMap<>();
		boolToGPU = new HashMap<>();
		boolToGPU.put(Boolean.FALSE , new Vector<GPU>());
		for (GPU gpu: GPUArray){	//TODO : check if necessary
			GPUToUnProcessed.put(gpu ,new Vector<DataBatch>());
			GPUToProcessed.put(gpu ,new LinkedBlockingDeque<>());
			GPUToProcessed.get(gpu).add(new Vector<>());
			GPUToProcessed.get(gpu).add(new Vector<>());
			boolToGPU.get(Boolean.FALSE).add(gpu);
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
			Vector<DataBatch> unprocessedData = new Vector<>();
			Vector<DataBatch> releventCPUVec = GPUToUnProcessed.get(nextTreatedGPU);
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
			if (!vec.isEmpty())
				return true;
		}
		return false;
	}

	/**
	 * updates the next GPU that it's data is going to be processed
	 * 		if there is one such that no CPU is processing, then he will be chosen
	 * 		else , the first GPU of the treated ones will be chosen
	 */
	public synchronized void updateNextTreatedGPU(){
		if (!boolToGPU.get(Boolean.FALSE).isEmpty()){
			nextTreatedGPU = boolToGPU.get(Boolean.FALSE).remove(0);
			boolToGPU.get(Boolean.TRUE).add(nextTreatedGPU);
		}
		else{
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
	 * @param processedData the completed processed data
	 */
	public synchronized void addProcessedData(GPU gpu,Vector<DataBatch> processedData){
		Vector<DataBatch> vector = GPUToProcessed.get(gpu).getLast();
			while (vector.size() < gpu.getCurrentAvailableMemory()
						&& !processedData.isEmpty()){
			vector.add(processedData.remove(0));
		}
			while (!processedData.isEmpty()){
				Vector<DataBatch> x = new Vector<>();
				for(int i = 0;!processedData.isEmpty()
						&& i < gpu.getCurrentAvailableMemory(); i++){
					x.add(processedData.remove(0));
				}
				GPUToProcessed.get(gpu).add(x);
			}
	}
	public Vector<Model> getTrainedModels(){
		return trainedModels;
	}

	public int dataBatchesProcessed(){
		return dataBatchesProcessed;
	}

}
