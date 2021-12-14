package bgu.spl.mics.application.services;

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

		task = new TimerTask() {
			@Override
			public void run() {
				currentTime++;
				sendBroadcast(new TickBroadcast());
				System.out.println("Current Time: "+currentTime);
//				if (currentTime == duration)
//					globalTimer.cancel();

			}
		};
	}

	@Override
	protected void initialize() {
		globalTimer.scheduleAtFixedRate(task , 0 , speed);
		try{
		Thread.currentThread().sleep(duration);}
		catch(Exception e){}

		globalTimer.cancel();
		sendBroadcast(new TerminateBroadcast());
		
	}

}
