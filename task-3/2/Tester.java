package hierarchy;

public class Tester extends Employee {

    public Tester() {
        position = "Tester";
        salary = 80_000;
    }

    public Tester(String name) {
        this();
        this.name = name;
    }
}
