package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.objects.Data;
import bgu.spl.mics.application.objects.Student;
/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {
    enum Status {PreTrained, Training, Trained , Tested}
    enum Result {None, Good, Bad }

    private String name;
    private Data data;
    private Student student;
    private Status status;
    private Result result;


    public Model(String name, Data data, Student student) {
        this.name = name;
        this.data = data;
        this.student = student;
        this.status = Status.PreTrained;
        this.result = Result.None;
    }

    public String getName() {
        return name;
    }

    public Data getData() {
        return data;
    }

    public Student getStudent() {
        return student;
    }

    public Status getStatus() {
        return status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(String result) {
        if(result.equals("Good"))
            this.result = Result.Good;
        else
            this.result = Result.Bad;
    }

    public void setStatus(String status) {
        //PreTrained, Training, Trained , Tested
        if(status.equals("PreTrained"))
            this.status = Status.PreTrained;
        else if(status.equals("Training"))
            this.status = Status.Training;
        else if(status.equals("Trained"))
                this.status = Status.Trained;
        else
            this.status = Status.Tested;



    }
}
