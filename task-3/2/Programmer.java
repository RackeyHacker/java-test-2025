package hierarchy;

public class Programmer extends Employee {

    public Programmer() {
        position = "Programmer";
        salary = 120_000;
    }

    public Programmer(String name) {
        this();
        this.name = name;
    }
}
