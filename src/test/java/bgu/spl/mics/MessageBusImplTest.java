package bgu.spl.mics;


import bgu.spl.mics.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.services.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.example.messages.*;

import static org.junit.Assert.*;

public class MessageBusImplTest {
    static MessageBusImpl msb;
    static StudentService stservice;
    static StudentService stservice2;
    static  GPUService gpuservice;
    static  ExampleEvent ex_event;
    static ExampleBroadcast ex_broad;

    @Before
    public static void setUp() throws Exception {
        msb = MessageBusImpl.getInstance();
        stservice = new StudentService("Marina",new Model[1]);
        stservice2 = new StudentService("Adler",new Model[1]);
        gpuservice = new GPUService("GPU");
        ex_event = new ExampleEvent("Drake Concert");
        ex_broad = new ExampleBroadcast("Hello CS");

    }

    @After
    public static void tearDown(MessageBusImpl msb) throws Exception {
        msb = null;
        stservice = null;
        stservice2 = null;
        gpuservice = null;
        ex_event = null;
        ex_broad = null;
    }

    @Test
    public void testSubscribeEvent() throws Exception {
        msb.register(stservice);
        assertFalse(msb.isSubscribedToEvent(ex_event.getClass(), stservice));
        msb.subscribeEvent(ex_event.getClass(), stservice);
        assertTrue(msb.isSubscribedToEvent(ex_event.getClass(), stservice));

    }

    @Test
    public void testSubscribeBroadcast() {
        msb.register(stservice);
        msb.register(stservice2);
        msb.register(gpuservice);

        assertFalse(msb.isSubscribedToBroadcast(ex_broad.getClass() , stservice));
        assertFalse(msb.isSubscribedToBroadcast(ex_broad.getClass() , stservice2));
        assertFalse(msb.isSubscribedToBroadcast(ex_broad.getClass() , gpuservice));

        msb.subscribeBroadcast(ex_broad.getClass(), stservice);
        msb.subscribeBroadcast(ex_broad.getClass(), stservice2);
        msb.subscribeBroadcast(ex_broad.getClass(), gpuservice);

        assertTrue(msb.isSubscribedToBroadcast(ex_broad.getClass() , stservice));
        assertTrue(msb.isSubscribedToBroadcast(ex_broad.getClass() , stservice2));
        assertTrue(msb.isSubscribedToBroadcast(ex_broad.getClass() , gpuservice));

    }

    @Test
    public void testComplete() throws Exception {
        msb.register(stservice);
        msb.subscribeEvent(ex_event.getClass(), stservice);
        Future<String> output = msb.sendEvent(ex_event);
        //before await
        assertNull(output.get());
        msb.awaitMessage(stservice);
        //after await
        assertNotNull(output.get());

    }

    @Test
    public void testSendBroadcast() {

        msb.register(stservice);
        msb.register(stservice2);
        msb.register(gpuservice);

        msb.subscribeBroadcast(ex_broad.getClass() , stservice);
        msb.subscribeBroadcast(ex_broad.getClass() , stservice2);
        msb.subscribeBroadcast(ex_broad.getClass() , gpuservice);

        msb.sendBroadcast(ex_broad);

        assertFalse(msb.isQueueEmpty(stservice));
        assertFalse(msb.isQueueEmpty(stservice2));
        assertFalse(msb.isQueueEmpty(gpuservice));

    }

    @Test
    public void testSendEvent() {

        msb.register(stservice);
        msb.subscribeEvent(ex_event.getClass(), stservice);
        msb.sendEvent(ex_event);

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
        msb.subscribeEvent(ex_event.getClass(), stservice);
        msb.sendEvent(ex_event);

        assertFalse(msb.isQueueEmpty(stservice));
        msb.awaitMessage(stservice);
        assertTrue(msb.isQueueEmpty(stservice));

    }
}