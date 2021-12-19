package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class GPUTest {
    private GPU gpu;
    public Model modelExample;
    public Cluster clusterExample;
    public Data data;
    public Student student;

    @Before
    public void setUp() throws Exception {
        data = new Data("Images",10000);
        student = new Student("OS","cs", "MSc" ,new Model[1]);
        modelExample = new Model("omer's model",data, student);
        gpu = new GPU("GTX1080");
        gpu.setModel(modelExample);
    }

    @After
    public void tearDown() throws Exception {

    }




    @Test
    public void sendUnprocessedDataTest() {
        //check if there is enough space before sending
        assertFalse(false);
        //TODO


    }

    @Test
    public void testDivideDataIntoBatches(){
        //test the DataBatch
        Vector<DataBatch> x = new Vector<>();
        x.add(new DataBatch(data , 0 , true));
        Cluster.getInstance().addProcessedData(gpu ,x);

        assertNotNull(Cluster.getInstance().getUnprocessedData());


    }


    }
