package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;


import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	private int speed;
	private int duration;
	private Timer globalTimer;
	private int currentTime;
	private TimerTask task;

	public TimeService(int _speed , int _duration) {
		super("Time-Service");
		speed = _speed;
		duration = _duration;
		currentTime = 0;
		globalTimer = new Timer();

	}

	@Override
	protected void initialize() {
		MessageBus msgbus = MessageBusImpl.getInstance();
//		msgbus.register(this);

		//callback instructions for TerminateBroadcast
		Callback<TerminateBroadcast> instructionsForTerminate =
				new Callback<TerminateBroadcast>() {
					@Override
					public void call(TerminateBroadcast c) {
						terminate();
					}
				};
		subscribeBroadcast(TerminateBroadcast.class , instructionsForTerminate);

		task = new TimerTask() {
			@Override
			public void run() {
				currentTime++;
				synchronized (msgbus) {
					sendBroadcast(new TickBroadcast());
				}
			}
		};

		globalTimer.scheduleAtFixedRate(task , 0 , speed);

		try{
		Thread.currentThread().sleep(speed*duration-50);}
		catch(Exception e){}

		sendBroadcast(new TerminateBroadcast());
		System.out.println("Timer sent termination.");		//////////////////////////////////////
		globalTimer.cancel();
		Thread.currentThread().stop();
	}

}
