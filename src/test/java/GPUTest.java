package test.java;

import src.main.java.bgu.spl.mics.application.objects.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GPUTest {
    GPU gpu;
    Model modelExample;
    Cluster clusterExample;
    Data data;

    @Before
    public void setUp() throws Exception {
        data = new Data(0,0,10);
        modelExample = new Model("omers model",data, );
        clusterExample = new Cluster();
        gpu = new GPU(2 , modelExample , clusterExample);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void sendUnprocessedDataTest() {
        //check if there is enough space before sending
        assertFalse(modelExample.getData().getSize() > gpu.getCurrentAvailableMemory());
        // we need to check that the data gone through the cluster

        //we ned to check that the data is being processed in one of the cpus

    }

    @Test
    public void trainModel() {
    }
}