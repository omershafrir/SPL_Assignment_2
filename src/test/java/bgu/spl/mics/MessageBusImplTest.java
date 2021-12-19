package bgu.spl.mics;


import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import bgu.spl.mics.application.services.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import bgu.spl.mics.MessageBusImpl;

import static org.junit.Assert.*;

public class MessageBusImplTest {
    static MessageBusImpl msb;
    static StudentService stservice;
    static  GPUService gpuservice;
    static TrainModelEvent train;
    static TickBroadcast tick;
    static Model model;
    static Data data;
    static Student student;

    @Before
    public static void setUp() throws Exception {
        msb = MessageBusImpl.getInstance();
        data = new Data("Text" , 1000);
        student = new Student("Rocker" , "CS" , "MSc" , new Model[1]);
        stservice = new StudentService("Rocker" , student);
        Model model = new Model("Marina" , data , student);
        train = new TrainModelEvent(model , stservice);
        tick = new TickBroadcast();

    }

    @After
    public static void tearDown(MessageBusImpl msb) throws Exception {

    }

    @Test
    public void testSubscribeEvent() throws Exception {
        msb.register(stservice);
        assertFalse(msb.isSubscribedToEvent(train.getClass(), stservice));
        msb.subscribeEvent(train.getClass(), stservice);
        assertTrue(msb.isSubscribedToEvent(train.getClass(), stservice));

    }

    @Test
    public void testSubscribeBroadcast() {
        msb.register(stservice);
        msb.register(gpuservice);

        assertFalse(msb.isSubscribedToBroadcast(tick.getClass() , stservice));
        assertFalse(msb.isSubscribedToBroadcast(tick.getClass() , gpuservice));

        msb.subscribeBroadcast(tick.getClass(), stservice);
        msb.subscribeBroadcast(tick.getClass(), gpuservice);

        assertTrue(msb.isSubscribedToBroadcast(tick.getClass() , stservice));
        assertTrue(msb.isSubscribedToBroadcast(tick.getClass() , gpuservice));

    }

    @Test
    public void testComplete() throws Exception {
        msb.register(stservice);
        msb.subscribeEvent(train.getClass(), stservice);
        Future<Model> output = msb.sendEvent(train);
        //before await
        assertNull(output.get());
        msb.awaitMessage(stservice);
        //after await
        assertNotNull(output.get());

    }

    @Test
    public void testSendBroadcast() {

        msb.register(stservice);
        msb.register(gpuservice);

        msb.subscribeBroadcast(tick.getClass() , stservice);
        msb.subscribeBroadcast(tick.getClass() , gpuservice);

        msb.sendBroadcast(tick);

        assertFalse(msb.isQueueEmpty(stservice));
        assertFalse(msb.isQueueEmpty(gpuservice));

    }

    @Test
    public void testSendEvent() {

        msb.register(stservice);
        msb.subscribeEvent(train.getClass(), stservice);
        msb.sendEvent(train);

        assertFalse(msb.isQueueEmpty(stservice));

    }

    @Test
    public void testRegister() {

        assertFalse(msb.isRegistered(stservice));
        msb.register(stservice);
        assertTrue(msb.isRegistered(stservice));

    }

    @Test
    public void testUnregister() {

        msb.register(stservice);
        assertTrue(msb.isRegistered(stservice));
        msb.unregister(stservice);
        assertFalse(msb.isRegistered(stservice));

    }

    @Test
    public void testAwaitMessage() throws InterruptedException {

        msb.register(stservice);
        msb.subscribeEvent(train.getClass(), stservice);
        msb.sendEvent(train);

        assertFalse(msb.isQueueEmpty(stservice));
        msb.awaitMessage(stservice);
        assertTrue(msb.isQueueEmpty(stservice));

    }
}