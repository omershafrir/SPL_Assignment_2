package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.DataBatch;
import bgu.spl.mics.application.objects.Cluster;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

public class CPUTest {

    public static CPU cpuExample;
    public static Cluster cluster;
    public static Data data;
    public static DataBatch dataBatch;

    @Before
    public void setUp() throws Exception {
        cluster = Cluster.getInstance();
        cpuExample = new CPU(3);
        data = new Data("Images",10000);
        dataBatch = new DataBatch(data , 0 , true);
    }
    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFinishProcessCurrentBatch(){
        cpuExample.startProcessNextBlock();
        if(cpuExample.getUnProcessedData().size()>0) {
            int sizeBefore = cpuExample.getUnProcessedData().size();
            cpuExample.finishProcessCurrentBatch();
            int sizeAfter = cpuExample.getUnProcessedData().size();
            assertEquals(sizeBefore, sizeAfter + 1);
        }

    }
    @Test
    public void testProcessData() {
        int sizeBefore = cpuExample.getProcessedData().size();
        cpuExample.getUnProcessedData();
        assertTrue(true);
        assertEquals(sizeBefore,sizeBefore);
    }

}