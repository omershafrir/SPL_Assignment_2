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
    private boolean isPublished;

    public Model(String name, Data data, Student student) {
        this.name = name;
        this.data = data;
        this.student = student;
        this.status = Status.PreTrained;
        this.result = Result.None;
        this.isPublished = false;
    }

    public String getName() {
        return name;
    }

    public Data getData() {
        return data;
    }

    public void publishModel(){
        isPublished = true;
    }
    public Student getStudent() {
        return student;
    }

    public String getStatus() {
        if(status.equals(Status.PreTrained))
            return "PreTrained";
        else if(status.equals(Status.Training))
            return "Training";
        else if(status.equals(Status.Trained))
            return "Trained";
        else
            return "Tested";
    }

    public String getResult() {
        if(result.equals(Result.None))
            return "Non";
        else if(result.equals(Result.Bad))
            return "Bad";
        else //if(result.equals(Result.Good))
            return "Good";
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

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", data=" + data +
                ", student=" + student +
                ", status=" + status +
                ", result=" + result +
                ", isPublished=" + isPublished +
                '}';
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
