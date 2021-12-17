package bgu.spl.mics;

import bgu.spl.mics.application.messages.TrainModelEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	//fields:
	private static MessageBusImpl instance = new MessageBusImpl();
	private ConcurrentHashMap<MicroService , BlockingQueue <Message>> msToQueueMap;
	private ConcurrentHashMap<Class<? extends Event <?>> , Queue<MicroService>> eventSubscriptions;
	private ConcurrentHashMap<Class<? extends Broadcast> , Vector<MicroService>> broadcastSubscriptions;
	private ConcurrentHashMap<Event<?> , Future<?>> eventToFutureMap;

	/** Holder class for the MsgBusImpl singleton instance
	 */
//	private static class MessageBusImplHolder{
//		private static MessageBusImpl instance = new MessageBusImpl();
//	}

	/*** private constructor.
	 */
	private MessageBusImpl(){
		msToQueueMap = new ConcurrentHashMap<MicroService , BlockingQueue<Message>>() ;
		eventSubscriptions = new ConcurrentHashMap<Class<? extends Event <?>> , Queue<MicroService>>();
	 	broadcastSubscriptions = new ConcurrentHashMap<Class<? extends Broadcast> , Vector<MicroService>>();
		eventToFutureMap = new ConcurrentHashMap<Event<?> , Future<?>>();
	}
	public static MessageBusImpl getInstance(){
		return instance;
	}
//	public static MessageBusImpl getInstance(){
//		return MessageBusImplHolder.instance;
//	}


	/***
	 * @param type The type of Class we want to check
	 * @param m The microservice we want to check if subscribed
	 * isSubscribedToEvent is basic query, therefore @pre , @post,
	 * @inv are irrelevant.
	 */
	@Override
	public <T> boolean isSubscribedToEvent(Class<? extends Event<T>> type, MicroService m){
		Queue<MicroService> queue = eventSubscriptions.get(type);
		return queue.contains(m);
	}

	/***
	 * @param type The type of Class we want to check
	 * @param m The microservice we want to check if subscribed
	 * isSubscribedToBroadcast is basic query, therefore @pre , @post,
	 * @inv are irrelevant.
	 */
	@Override
	public boolean isSubscribedToBroadcast(Class<? extends Broadcast> type, MicroService m){
		Vector<MicroService> vec = broadcastSubscriptions.get(type);
		return vec.contains(m);
	}

	/***
	 * @param m The microservice we want to check if registered
	 * isRegistered is basic query, therefore @pre , @post,
	 * @inv are irrelevant.
	 */
	@Override
	public boolean isRegistered(MicroService m){
		return msToQueueMap.containsKey(m);
	}

	/***
	 * @param m The microservice which queue we want to check
	 * isQueueEmpty is basic query, therefore @pre , @post,
	 * @inv are irrelevant.
	 */
	@Override
	public boolean isQueueEmpty(MicroService m){
		return msToQueueMap.get(m).isEmpty();
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
		if(m!=null && isRegistered(m) ) {
			if (!eventSubscriptions.containsKey(type)) {    //check if this type has a queue
				eventSubscriptions.put(type, new LinkedBlockingQueue<>());
				eventSubscriptions.get(type).add(m);
			}
			else if (!isSubscribedToEvent(type, m) )
				eventSubscriptions.get(type).add(m);
		}
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
//		System.out.println(Thread.currentThread().getName() + "is subscribing to : "+ type);			////////////////////////////
//		System.out.println(Thread.currentThread().getName()+ " is registered :" + isRegistered(m));		/////////////////////////////////////
		if(m!=null && isRegistered(m) ) {
			if (!broadcastSubscriptions.containsKey(type)) {    //check if this type has a queue
				broadcastSubscriptions.put(type, new Vector<MicroService>());
				broadcastSubscriptions.get(type).add(m);
//				System.out.println(type+" "+m+ " ARRIVED");											////////////////////////////////////
			}
			else if (!isSubscribedToBroadcast(type, m)) {
				broadcastSubscriptions.get(type).add(m);
			}
		}
//		System.out.println("IS CONTAINING: " +broadcastSubscriptions.containsKey(type) + this);	 	//////////////////////////////////////////////////////////////
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
		if (result != null) {
			Future<T> future = (Future<T>) eventToFutureMap.get(e);
			future.resolve(result);
		}
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
//		System.out.println(Thread.currentThread().getName() + " is sending: " + b.getClass());	/////////////////////////////////////////
//		System.out.println(b.getClass() +" is in : "+ broadcastSubscriptions.containsKey(b.getClass()) + this);	/////////////////////////							/////////////////////////////////////////
		if( b!= null && broadcastSubscriptions.containsKey(b.getClass())) {
//			System.out.println("TRUE");														  ///////////////////////////////////////////////
//			System.out.println(broadcastSubscriptions.toString());							///////////////////////////////////////////////
			Vector<MicroService> relevent_vec = broadcastSubscriptions.get(b.getClass());
//			System.out.println("relvec is : "+ relevent_vec.toString());								/////////////////////////////////////////
			for (MicroService ms : relevent_vec) {
//				System.out.println("sending broadcast of type : " +b.getClass() +" to "+ms.getName());	/////////////
				msToQueueMap.get(ms).add(b);
			}
		}
//		System.out.println( broadcastSubscriptions.containsKey(b));		//////////////////////////////////////////////////////////////
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
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		if( e instanceof  TrainModelEvent)
				System.out.println("SENDING A NEW TRAIN_MODEL EVENT, THE MODEL IS :"+ ((TrainModelEvent) e).getModel().getName());	///////////////////////////////////////
		Future<T> future = new Future<>();
		//check if e!=null and if there's an ms subscribed to events of type e
		if (e != null && eventSubscriptions.containsKey(e.getClass())){
			Queue<MicroService> relevent_queue = eventSubscriptions.get(e.getClass());
			MicroService runner =  relevent_queue.remove();
			relevent_queue.add(runner);		//inserting runner immediately at the back of the queue
			msToQueueMap.get(runner).add(e);
//			System.out.println("sending event of type : " +e.getClass() +" to "+runner.getName());		///////////////////////////////////////////
			eventToFutureMap.put(e , future);
		}
		return future;
	}

	/**
	 * @param m the micro-service to register create a queue for.
	 * @pre this.isRegistered(m) = false
	 * @pre m != null
	 * @post this.isRegistered(m) = true
	 */
	@Override
	public synchronized void register(MicroService m) {
		if(m!=null && !isRegistered(m)){
//			System.out.println("Register: "+ Thread.currentThread().getName());
			BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
			msToQueueMap.put(m , queue);
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
			msToQueueMap.remove(m);
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
	public  Message awaitMessage(MicroService m) throws InterruptedException {
//		System.out.println(Thread.currentThread().getName() + " is awaiting message");			/////////////////////////////////////
//		System.out.println(Thread.currentThread().getName() + "Blocking queue is:");			////////////////////////////////////
//		System.out.println(msToQueueMap.get(m).toString());										///////////////////////////////////

		Message task = null;
		if (isRegistered(m)){
			task = msToQueueMap.get(m).take();
//			System.out.println(Thread.currentThread().getName()+" IS EXECUTING: "+task.toString());				////////////////////////////////////////////////////////////
		}
		else{
			//was determined in the interface
			InterruptedException IllegalStateException = new InterruptedException();
			throw IllegalStateException;
		}
		return task;
	}
}
