package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    enum Degree {
        MSc, PhD
    }

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;
    private Model[] myModels;
    private int counterTestedModels = 0;
    private Future<Model> future = null;
    private int interalTimer;

    public Model[] getModels() {
        return myModels;
    }

    public Student(String name, String department, String status, Model[] modelsArray) {
        this.name = name;
        this.department = department;
        if(status.equals("MSc"))
            this.status = Degree.MSc;
        else
            this.status = Degree.PhD;
        this.publications = 0;
        this.papersRead = 0;
        myModels = modelsArray;
        interalTimer = 0;
    }
    public void incrementModelCounter(){
        counterTestedModels++;
    }

    public int getCounterTestedModels(){
        return counterTestedModels;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        if(status.equals("MSc"))
            return ("MSc");
        else
            return ("PhD");
    }

    public void setFuture(Future<Model> future) {
        this.future = future;
    }

    public Future<Model> getFuture(){
        return this.future;
    }
    public synchronized void incrementPublished(){
        publications++;
    }

    public synchronized void readPaper(){
        papersRead++;
    }

    public void incrementTimer(){
        interalTimer++;
    }
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", status=" + status +
                ", publications=" + publications +
                ", papersRead=" + papersRead +
                '}';
    }



}
