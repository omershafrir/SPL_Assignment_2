package bgu.spl.mics.application.objects;

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
    public Model[] myModels;

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
    }

    public String getStatus() {
        if(status.equals("MSc"))
            return ("MSc");
        else
            return ("PhD");
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
