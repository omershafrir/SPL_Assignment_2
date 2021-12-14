package bgu.spl.mics.application.objects;


import java.util.Vector;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {

	private static Cluster single_instance;
	/**
     * Retrieves the single instance of this class.
     */
	public static Cluster getInstance() {
		if(single_instance == null){
			single_instance = new Cluster();
		}
		return single_instance;
	}
	private Cluster(){

	}

	//gets the unProcessed data from GPU
	//and sends it to an available CPU
	public void AddToCPUunProcessedData(Vector<DataBatch> to_send){
		//TODO - send unProcessed to a CPU
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
