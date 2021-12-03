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
//        setUp();
        MessageBusImpl msb = new MessageBusImpl();
        StudentService stservice = new StudentService("Marina");
        msb.register(stservice);
        ExampleEvent ex = new ExampleEvent("Adler");
        msb.subscribeEvent(ex.getClass() , stservice);
        Future<String> output = msb.sendEvent(ex);
        //before await output gets a null future
        assertNull(output.get());
        msb.awaitMessage(stservice);
        //after await the output will be processed and completed thus not null
        assertNotNull(output.get());
        msb = null;
        stservice = null;








//        tearDown(msb);


//        TrainModel tm = new Event<Model>(){
//            String name = "model";
//            public String getName() {
//                return name;
//            }
//        };
    }

    @Test
    public void TestsubscribeBroadcast() {
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
        //this function can not be implemented here because the queque filed is private
//        LinkedList<Queue<MicroService>> to_check = msb.get(queueLinkedList);
        //we should consider implement contains in the msbimp
        //checking if each one of the services that subscribed got the message
        assertTrue(msb.all_contains_x(ex));
//        boolean contains = true;
//        for(Queue<Message> x : to_check){
//            if(!x.contains(ex)){
//                contains = false;
//            }
//            assertTrue(contains);
//        }
    }

    @Test
    public void Testcomplete() throws Exception {
        MessageBusImpl msb = new MessageBusImpl();
        StudentService stservice = new StudentService("Marina");
        ExampleEvent ex = new ExampleEvent("Adler");
        msb.register(stservice);
        msb.subscribeEvent(ex.getClass(),stservice);
        Future<String> output = msb.sendEvent(ex);
        msb.awaitMessage(stservice);
        //assuming (not sure) that if the service is completed - and
        // the function suppose to notify the msb that the complete happened
        //the way to check it is : if the event is not in the queque it use to be in(the service finished dealing with it)
        // a bit confusing but need to be look at from the msb pov

    }

    @Test
    public void TestsendBroadcast() {
    }

    @Test
    public void TestsendEvent() {
    }

    @Test
    public void Testregister() {
    }

    @Test
    public void Testunregister() {
    }

    @Test
    public void TestawaitMessage() {
    }
}