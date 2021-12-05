package test.java.bgu.spl.mics.application.objects;

import bgu.spl.mics.application.objects.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

public class CPUTest {


    static CPU cpuExample;
    Cluster cluster;
    Data data;
    DataBatch dataBatch;

    @Before
    public void setUp() throws Exception {
        cluster = Cluster.getInstance();
        cpuExample = new CPU(3 , cluster);
        data = new Data(Data.Type.Text , 3000);
        dataBatch = new DataBatch(data , 0);
    }
    @After
    public void tearDown() throws Exception {
        cpuExample = null;
        data = null;
        dataBatch = null;
    }

    @Test
    public void testAddData(){
        int sizeBefore = cpuExample.getDataSize();
        cpuExample.addData(dataBatch);
        assertEquals(sizeBefore+1 , cpuExample.getDataSize() );
    }
    @Test
    public void testProcessData() {
        int sizeBefore = cpuExample.getDataSize();
        cpuExample.addData(dataBatch);
        assertEquals(sizeBefore -1 , cpuExample.getDataSize() );
    }

}