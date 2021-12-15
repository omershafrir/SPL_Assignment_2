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
//
//    @Before
//    public void setUp() throws Exception {
//        cluster = Cluster.getInstance();
//        cpuExample = new CPU(3);
//        data = new Data("Text" , 3000);
//        dataBatch = new DataBatch(data , 0);
//    }
//    @After
//    public void tearDown() throws Exception {
//        cpuExample = null;
//        data = null;
//        dataBatch = null;
//    }
//
//    @Test
//    public void testAddData(){
//        int sizeBefore = cpuExample.getDataSize();
//        cpuExample.addData(dataBatch);
//        assertEquals(sizeBefore+1 , cpuExample.getDataSize() );
//    }
//    @Test
//    public void testProcessData() {
//        int sizeBefore = cpuExample.getDataSize();
//        cpuExample.addData(dataBatch);
//        assertEquals(sizeBefore -1 , cpuExample.getDataSize() );
//    }

}