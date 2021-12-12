package bgu.spl.mics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	//fields:
	private HashMap<MicroService , BlockingQueue <Message>> microServiceMap;
	private HashMap<MicroService ,Class<? extends Event <?>>> x;
	private HashMap<Class<? extends Event <?>> , Queue<MicroService>> eventSubscriptions;
	private HashMap<Class<? extends Broadcast> , Queue<MicroService>> broadcastSubscriptions;


	/** Holder class for the MsgBusImpl singleton instance
	 */
	private static class MessageBusImplHolder{
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	/*** private constructor.
	 */
	private MessageBusImpl(){
		microServiceMap = new HashMap<MicroService , BlockingQueue<Message>>() ;
		eventSubscriptions = new HashMap<Class<? extends Event <?>> , Queue<MicroService>>();
	 	broadcastSubscriptions = new HashMap<Class<? extends Broadcast> , Queue<MicroService>>();
	}
	public static MessageBusImpl getInstance(){
		return MessageBusImplHolder.instance;
	}


	/***
	 * @param type The type of Class we want to check
	 * @param m The microservice we want to check if subscribed
	 * isSubscribedToEvent is basic query, therefore @pre , @post,
	 * @inv are irrelevant.
	 */
	@Override
	public <T> boolean isSubscribedToEvent(Class<? extends Event<T>> type, MicroService m){
		Queue<MicroService> queue = eventSubscriptions.get(m);
		return queue.contains(type);
	}

	/***
	 * @param type The type of Class we want to check
	 * @param m The microservice we want to check if subscribed
	 * isSubscribedToBroadcast is basic query, therefore @pre , @post,
	 * @inv are irrelevant.
	 */
	@Override
	public boolean isSubscribedToBroadcast(Class<? extends Broadcast> type, MicroService m){
		Queue<MicroService> queue = broadcastSubscriptions.get(m);
		return queue.contains(type);
	}

	/***
	 * @param m The microservice we want to check if registered
	 * isRegistered is basic query, therefore @pre , @post,
	 * @inv are irrelevant.
	 */
	@Override
	public boolean isRegistered(MicroService m){
		return microServiceMap.containsKey(m);
	}

	/***
	 * @param m The microservice which queue we want to check
	 * isQueueEmpty is basic query, therefore @pre , @post,
	 * @inv are irrelevant.
	 */
	@Override
	public boolean isQueueEmpty(MicroService m){
		return microServiceMap.get(m).isEmpty();
	}

	/**@param <T>  The type of the result expected by the completed event.
	 * @param type The type to subscribe to,
	 * @param m The subscribing micro-service
	 * @pre m!=null
	 * @pre this.isSubscribedToEvent(type , m) = false
	 * @inv	this.isRegistered(m) = true
	 * @post this.isSubscribedToEvent(type , m) = true
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m){
		if(m!=null && !isSubscribedToEvent(type , m) && isRegistered(m))
			eventSubscriptions.get(type).add(m);
	}

	 /** @param type The type to subscribe to,
	 * @param m The subscribing micro-service
	 * @pre m!=null
`	 * @pre this.isSubscribedToBroadcast(type , m) = false
	 * @inv	this.isRegistered(m) = true
	 * @post this.isSubscribedToBroadcast(type , m) = true
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(m!=null && !isSubscribedToBroadcast(type , m) && isRegistered(m))
			broadcastSubscriptions.get(type).add(m);
	}

	/**
	 * @param e The completed event.
	 * @param result The resolved result of the completed event.
	 * @param <T> the type of the result of the event
	 * @pre processing of Event e has been completed.
	 * @inv result != null
	 * @post
	 */
	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param b The broadcast to added to the queues.
	 * @pre b != null
	 * @post for every micro-service which is subscribed to b:
	 * 		 @pre (num of messages in queue) = (num of
	 * 		 messages in queue) -1
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub


	}

	/**
	 * @param e The event to add to the queue.
	 * @param <T> the type of event being processed
	 * @pre e != null
	 * @post for every micro-service which is subscribed to b:
	 * 		@pre (num of messages in queue)  = (num of
	 * 		 messages in queue) -1
	 * @post Future<T>.get() = null
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		return null;
	}

	/**
	 * @param m the micro-service to register create a queue for.
	 * @pre this.isRegistered(m) = false
	 * @pre m != null
	 * @post this.isRegistered(m) = true
	 */
	@Override
	public void register(MicroService m) {
		if(m!=null && !isRegistered(m)){
			BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
			microServiceMap.put(m , queue);
		}
	}

	/**
	 * @param m the micro-service to unregister and destroy it queue.
	 * @pre this.isRegistered(m) = true
	 * @pre m != null
	 * @post this.isRegistered(m) = false
	 */
	@Override
	public void unregister(MicroService m) {
		if(m!=null && isRegistered(m)){
			microServiceMap.remove(m);
		}
	}

	/**
	 * @param m The micro-service requesting to take a message from its message
	 *          queue.
	 * @pre this.isQueueEmpty(m) = false
	 * @inv this.isRegistered(m) = true
	 * @post @pre (num of messages in m queue) =
	 * 		 (num of messages in m queue) + 1
	 */
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

}
