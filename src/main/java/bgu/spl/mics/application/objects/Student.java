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

    public Student(String name, String department, String status) {
        this.name = name;
        this.department = department;
        if(status.equals("MSc"))
            this.status = Degree.MSc;
        else
            this.status = Degree.PhD;
        this.publications = 0;
        this.papersRead = 0;
    }

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;

}
