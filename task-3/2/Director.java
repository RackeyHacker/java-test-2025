package hierarchy;

public class Director extends Employee {

    public Director() {
        position = "Director";
        salary = 350_000;
    }

    public Director(String name) {
        this();
        this.name = name;
    }
}
