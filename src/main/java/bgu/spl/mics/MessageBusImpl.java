package bgu.spl.mics;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	//the queque supposed to be a queque of events and brodcast - messeges
	private LinkedList<Queue<Message>> queueLinkedList;

	public LinkedList<Queue<Message>> getQueueLinkedList(){
		return queueLinkedList;
	}

	public boolean isSubscribed(Class<? extends Message<T>> type, MicroService m){
		// TODO
		return false;
	}

//	//was implemented for subscribebrodcast test
//	public boolean all_contains_x(Broadcast b) {
//		boolean found = true;
//		for(Queue<Message> x : queueLinkedList){
//			if (!x.contains(b)){
//				found = false;
//			}
//		}
//
//		return found;
//	}


	/**@param <T>  The type of the result expected by the completed event.
	 * @param type The type to subscribe to,
	 * @param m    The subscribing micro-service.
	 * @pre
	 * @inv
	 * @post m is subscribed to events of
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(MicroService m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregister(MicroService m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
