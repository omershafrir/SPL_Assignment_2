package bgu.spl.mics;

import java.util.concurrent.TimeUnit;
/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
	private boolean isResolved = false;
	private T value;


	/**
	 * This should be the only public constructor in this class.
	 */
	public Future() {
		this.isResolved = false;
		value = null;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     * @inv:
	 * return value != null
	 * @PRE:
	 * @POST:
	 * value != null
     */
	public T get() {
		if(isResolved){
			return value;
		}

		while(!isResolved) {
			try{
				Thread.currentThread().sleep(1000);
			}catch(Exception exception){}

		}
		return value;
	}
	
	/**
     * Resolves the result of this Future object.
	 * @param result != null
	 * @PRE:
	 * value == null
	 * @POST:
	 * value != null
	 * value == result
     */
	public void resolve (T result) {
		isResolved = true;
		value = result;
	}
	
	/**
     * @return true if this object has been resolved, false otherwise
	 * @INV:
	 * !(isResolved & value == null)
	 * @PRE:
	 * @POST:
	 *
     */
	public boolean isDone() {
		if(isResolved)
			return true;
		return false;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
	 *
	 * @INV:
	 * !(isResolved & value == null)
	 * @PRE:
	 * timeout >= 0
	 * @POST:
     */
	public T get(long timeout, TimeUnit unit)  {
		if ( !isResolved){
			try {
				unit.sleep(timeout);
			}catch (Exception exception){}

		}
		return value;
	}

	/** ADDED FUNCTION
	 * basic query - getter
	 * @return TRUE if the Future as been resolved, else false
	 * @INV:
	 * !(isResolved & value == null)
	 * @PRE:
	 * @POST:
	 */
	public boolean getIsResolved(){
		return this.isResolved;
	}

}
