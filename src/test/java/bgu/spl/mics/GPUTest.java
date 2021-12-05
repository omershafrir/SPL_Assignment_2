package test.java.bgu.spl.mics;

import main.java.bgu.spl.mics.application.objects.*;
import main.java.bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GPUTest {
    private GPU gpu;
    public Model modelExample;
    public Cluster clusterExample;
    public Data data;
    public Student student;

    @Before
    public void setUp() throws Exception {
        data = new Data(Data.Type.Images.Type,10000);
        student = new Student("OS","cs", Student.Degree.MSc,0,0);
        modelExample = new Model("omer's model",data, student);
//        clusterExample = new Cluster();
        gpu = new GPU(GPU.Type.GTX1080 , modelExample , clusterExample);
    }

    @After
    public void tearDown() throws Exception {
        this.gpu = new GPU(GPU.Type.GTX1080 , modelExample , clusterExample);
        this.modelExample = new Model("omer's model",data, student);
    }




    @Test
    public void sendUnprocessedDataTest() {
        //check if there is enough space before sending
        assertFalse(modelExample.getData().getSize() > gpu.getCurrentAvailableMemory());
        gpu.sendUnprocessedData();
        // we need to check that the data gone through the cluster
        // cluster has a function that indicates if the data was sent
        assertTrue(clusterExample.WasSentUnProcessedDataToCPU());

        //we need to check that the data is being processed in one of the cpus
        //we will have a function  that checks if the certain cpu is processing data
    }

    @Test
    public void testDivideDataIntoBatches(){
        //test the DataBatch
        assertNotEquals(Model.Status.Tested,gpu.getModel().getStatus());
        assertNotEquals(Model.Status.Trained,gpu.getModel().getStatus());
        assertEquals(Model.Status.PreTrained,gpu.getModel().getStatus());
        gpu.divideDataIntoBatches();
        assertNotNull(gpu.getDb());
    }
    @Test
    public void testTrainModel() {
        Model returned = gpu.testModel(modelExample);
        assertTrue(returned.getStatus() == Model.Status.Tested );
        assertTrue(returned.getResult() == Model.Result.Bad || returned.getResult() == Model.Result.Good);
        assertNull(gpu.getModel());
    }


    }
}