package hierarchy;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Director director = new Director("Dima");
        Programmer programmer = new Programmer();
        Tester tester = new Tester();

        List<Employee> employees = new ArrayList<>();
        employees.add(director);
        employees.add(programmer);
        employees.add(tester);

        for (Employee e : employees) {
            System.out.println(e.getPosition() + ": " + e.getName() + " - " + e.getSalary());
        }

    }
}
