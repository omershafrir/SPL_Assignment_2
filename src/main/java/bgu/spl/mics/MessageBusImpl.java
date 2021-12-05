package bgu.spl.mics;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	//fields:
	private LinkedList<Queue<Message>> queueLinkedList;
	private static MessageBusImpl single_instance;

	/*** private constructor.
	 *  to be called from getInstance() once.
	 *
	 */
	private MessageBusImpl(){
		LinkedList<Queue<Message>> queueLinkedList= new LinkedList<Queue<Message>>();
	}

	/***
	 *
	 * @return instance : Only instance of Message Bus Implementation
	 */
	public static MessageBusImpl getInstance(){
		if(single_instance == null)
			single_instance = new MessageBusImpl();

		return single_instance;
	}


	/***
	 *
	 * @param type The type of Class we want to check
	 * @param m The microservice we want to check if subscribed
	 * isSubscribedToEvent is basic query
	 *
	 */
	@Override
	public <T> boolean isSubscribedToEvent(Class<? extends Event<T>> type, MicroService m){
		// TODO
		return false;
	}

	/***
	 *
	 * @param type The type of Class we want to check
	 * @param m The microservice we want to check if subscribed
	 *
	 */
	@Override
	public boolean isSubscribedToBroadcast(Class<? extends Broadcast> type, MicroService m){
	//TODO
		return false;
	}

	/***
	 *
	 * @param m The microservice we want to check if registered
	 * @return
	 */
	@Override
	public boolean isRegistered(MicroService m){
		// TODO
		return false;
	}

	/***
	 *
	 * @param m The microservice which queue we want to check
	 * @return
	 */
	@Override
	public boolean isQueueEmpty(MicroService m){
		// TODO
		return false;
	}

	/**@param <T>  The type of the result expected by the completed event.
	 * @param type The type to subscribe to,
	 * @param m The subscribing micro-service
	 * @pre m!=null
	 * @inv	this.isRegistered(m) = true
	 * @post this.isSubscribedToEvent(type , m) = true
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub

	}


	 /** @param type The type to subscribe to,
	 * @param m The subscribing micro-service
	 * @pre m!=null
	 * @inv	this.isRegistered(m) = true
	 * @post this.isSubscribedToBroadcast(type , m) = true
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param e The completed event.
	 * @param result The resolved result of the completed event.
	 * @param <T> the type of the result of the event
	 * @pre
	 * @inv
	 * @inv
	 * @post
	 */
	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param b The broadcast to added to the queues.
	 * @pre
	 * @inv
	 * @post
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param e The event to add to the queue.
	 * @param <T> the type of event being processed
	 *
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *
	 * @param m the micro-service to register create a queue for.
	 * @pre this.isRegistered(m) = false
	 * @pre m != null
	 * @inv
	 * @post this.isRegistered(m) = true
	 */
	@Override
	public void register(MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param m the micro-service to unregister and destroy it queue.
	 * @pre this.isRegistered(m) = true
	 * @pre m != null
	 * @inv
	 * @post this.isRegistered(m) = false
	 */
	@Override
	public void unregister(MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param m The micro-service requesting to take a message from its message
	 *          queue.
	 * @pre this.isQueueEmpty(m) = false
	 * @inv this.isRegistered(m) = true
	 * @post
	 */
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
