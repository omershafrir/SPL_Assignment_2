package bgu.spl.mics;

import org.junit.Before;
import org.junit.Test;
import bgu.spl.mics.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FutureTest {
    @Before
    public void setUp(){
        Future<Integer> future = new Future<Integer>();
    }

    @Test
    public void get() {
        Future<Integer> future = new Future<Integer>();
        assertNull(future.get());
        Integer result = future.get() + 1;
        future.resolve(result);
        assertEquals("Expected: future.get() == result.",future.get(),result);
    }

    @Test
    public void resolve() {
        Future<Integer> future = new Future<Integer>();
        assertNull("future is initialized with null",future.get());
        Integer result = future.get() + 1;
        future.resolve(result);
        assertNotNull("future result member should not be null after",future.get());
    }

    @Test
    public void isDone() {
        Future<Integer> future = new Future<Integer>();
        future.resolve(2);
        assertTrue("The isResolved field didn't change",future.isResolved());
        assertNotNull("After using Resolve result field shouldn't be null",future.get());
    }

    @Test
    public void testGet() {
        Future<Integer> future = new Future<Integer>();
        //not resolved yet
        assertNull("The future is not resolved yet-but it is not null!",future.get(0, TimeUnit.MILLISECONDS));
        Integer result = future.get() + 1;
        future.resolve(result);
        //assuming 100 ms is a decent amount of time
        assertNotNull("The future should have been resolved by now",future.get(100, TimeUnit.MILLISECONDS));
    }
}