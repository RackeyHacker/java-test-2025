package hierarchy;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Director director = new Director("Dima", 200_000, "Director");
        Programmer programmer = new Programmer("Oleg", 150_000, "Programmer");
        Tester tester = new Tester("Katya", 120_000, "Tester");

        List<Employee> employees = new ArrayList<>();
        employees.add(director);
        employees.add(programmer);
        employees.add(tester);

        int totalSalary = 0;
        for (Employee e : employees) {
            totalSalary += e.getSalary();
        }
        System.out.println("Total monthly salary: " + totalSalary + " RUB");
    }
}
