package bgu.spl.mics;

import bgu.spl.mics.application.services.*;
import bgu.spl.mics.example.ServiceCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import bgu.spl.mics.example.messages.*;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

public class MessageBusImplTest {

    @Before
    public void setUp() throws Exception {
//        MessageBusImpl msb = new MessageBusImpl();
    }

    @After
    public void tearDown(MessageBusImpl msb) throws Exception {
//        msb = null;
    }

    @Test
    public void testSubscribeEvent() throws Exception {
        MessageBusImpl msb = new MessageBusImpl();
        StudentService stservice = new StudentService("Marina");
        msb.register(stservice);
        ExampleEvent ex = new ExampleEvent("Adler");
        assertFalse(msb.isSubscribed(ex.getClass(), stservice));
        msb.subscribeEvent(ex.getClass(), stservice);
        assertTrue(msb.isSubscribed(ex.getClass(), stservice));
    }

    @Test
    public void testSubscribeBroadcast() {
        MessageBusImpl msb = new MessageBusImpl();
        StudentService stservice = new StudentService("Marina");
        StudentService stservice2 = new StudentService("Adler");
        GPUService gpuservice = new GPUService("Gpu");

        msb.register(stservice);
        msb.register(stservice2);
        msb.register(gpuservice);

        ExampleBroadcast ex = new ExampleBroadcast("Adler");


        assertFalse(msb.isSubscribed(ex.getClass() , stservice));
        assertFalse(msb.isSubscribed(ex.getClass() , stservice2));
        assertFalse(msb.isSubscribed(ex.getClass() , gpuservice));

        msb.subscribeBroadcast(ex.getClass(), stservice);
        msb.subscribeBroadcast(ex.getClass(), stservice2);
        msb.subscribeBroadcast(ex.getClass(), gpuservice);

        assertTrue(msb.isSubscribed(ex.getClass() , stservice));
        assertTrue(msb.isSubscribed(ex.getClass() , stservice2));
        assertTrue(msb.isSubscribed(ex.getClass() , gpuservice));





    }

    @Test
    public void testComplete() throws Exception {
        MessageBusImpl msb = new MessageBusImpl();
        StudentService stservice = new StudentService("Marina");
        msb.register(stservice);
        ExampleEvent ex = new ExampleEvent("Adler");
        msb.subscribeEvent(ex.getClass(), stservice);
        Future<String> output = msb.sendEvent(ex);
        //before await output gets a null future
        assertNull(output.get());
        msb.awaitMessage(stservice);
        //after await the output will be processed and completed thus not null
        assertNotNull(output.get());
        msb = null;
        stservice = null;
//        MessageBusImpl msb = new MessageBusImpl();
//        StudentService stservice = new StudentService("Marina");
//        ExampleEvent ex = new ExampleEvent("Adler");
//        msb.register(stservice);
//        msb.subscribeEvent(ex.getClass(),stservice);
//        Future<String> output = msb.sendEvent(ex);
//        msb.awaitMessage(stservice);
//        //assuming (not sure) that if the service is completed - and
//        // the function suppose to notify the msb that the complete happened
//        //the way to check it is : if the event is not in the queque it use to be in(the service finished dealing with it)
//        // a bit confusing but need to be look at from the msb pov

    }

    @Test
    public void testSendBroadcast() {
        MessageBusImpl msb = new MessageBusImpl();
        StudentService stservice = new StudentService("Marina");
        StudentService stservice2 = new StudentService("Adler");
        GPUService gpuservice = new GPUService("Gpu");

        msb.register(stservice);
        msb.register(stservice2);
        msb.register(gpuservice);
        ExampleBroadcast ex = new ExampleBroadcast("Ass2_FAQ");
        msb.subscribeBroadcast(ex.getClass() , stservice);
        msb.subscribeBroadcast(ex.getClass() , stservice2);
        msb.subscribeBroadcast(ex.getClass() , gpuservice);
        msb.sendBroadcast(ex);
        LinkedList<Queue<Message>> to_check = msb.getQueueLinkedList();
        //checking if each one of the services that subscribed got the message
        boolean contains = true;
        for(Queue<Message> x : to_check){
            if(!x.contains(ex)){
                contains = false;
            }
            assertTrue("Microservice: "+ x.toString()+" didnt get the broadcast",contains);
        }
        msb = null;
        stservice = null;
    }

    @Test
    public void testSendEvent() {
        //        setUp();
        MessageBusImpl msb = new MessageBusImpl();
        StudentService stservice = new StudentService("Marina");
        msb.register(stservice);
        ExampleEvent ex = new ExampleEvent("Adler");
        msb.subscribeEvent(ex.getClass(), stservice);
        msb.sendEvent(ex);
        LinkedList<Queue<Message>> to_check = msb.getQueueLinkedList();
        //checking if stservice subscribed  and therefore got the event
        boolean contains = false;
        assertTrue(to_check.getFirst().contains(ex));
        msb = null;
        stservice = null;
    }

    @Test
    public void testRegister() {
        MessageBusImpl msb = new MessageBusImpl();
        StudentService stservice = new StudentService("Marina");
        LinkedList<Queue<Message>> to_check = msb.getQueueLinkedList();
        assertNull(to_check.getFirst());
        msb.register(stservice);
        assertNotNull(to_check.getFirst());
        msb = null;
        stservice = null;
    }

    @Test
    public void testUnregister() {
        MessageBusImpl msb = new MessageBusImpl();
        StudentService stservice = new StudentService("Marina");
        LinkedList<Queue<Message>> to_check = msb.getQueueLinkedList();
        msb.register(stservice);
        assertNotNull(to_check.getFirst());
        msb.unregister(stservice);
        assertNull(to_check.getFirst());

        msb = null;
        stservice = null;
    }

    @Test
    public void testAwaitMessage() throws InterruptedException {
        MessageBusImpl msb = new MessageBusImpl();
        StudentService stservice = new StudentService("Marina");
        msb.register(stservice);
        ExampleEvent ex = new ExampleEvent("Adler");
        msb.subscribeEvent(ex.getClass(), stservice);
        msb.sendEvent(ex);
        LinkedList<Queue<Message>> to_check = msb.getQueueLinkedList();
        assertNotNull(to_check.getFirst().peek());
        msb.awaitMessage(stservice);
        assertNull(to_check.getFirst().peek());
        msb = null;
        stservice = null;
    }
}