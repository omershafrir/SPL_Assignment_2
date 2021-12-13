package bgu.spl.mics;

import bgu.spl.mics.Future;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FutureTest {

    public Future<Integer> future;

    @Before
    public void setUp(){
        future = new Future<Integer>();
    }

    @After
    public void tearDown(){
        future = new Future<Integer>();
    }

    @Test
    public void get() throws InterruptedException {
        //was commented because future return the future value - this method NEVER returns null
//        assertNull(future.get());
        Integer result = 2;
        future.resolve(result);
        assertEquals("Expected: future.get() == result.",future.get(),result);
    }

    @Test
    public void resolve() throws InterruptedException {
//        assertNull("future is initialized with null",future.get());
        Integer result = 2;
        future.resolve(result);
        assertNotNull("future result member should not be null after",future.get());
    }

    @Test
    public void isDone() throws InterruptedException {
        future.resolve(2);
        assertTrue("The isResolved field didn't change",future.getIsResolved());
        assertNotNull("After using Resolve result field shouldn't be null",future.get());
    }

    @Test
    public void testGet() throws InterruptedException {
        //not resolved yet
        assertNull("The future is not resolved yet-but it is not null!",future.get(0, TimeUnit.MILLISECONDS));
        Integer result = 2;
        future.resolve(result);
        //assuming 100 ms is a decent amount of time
        assertNotNull("The future should have been resolved by now",future.get(100, TimeUnit.MILLISECONDS));
        assertEquals(new Integer(2), future.get(100, TimeUnit.MILLISECONDS));
    }
}