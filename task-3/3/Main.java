package laptopBuilding;

public class Main {
    public static void main(String[] args) {
        System.out.println("Launching the laptop assembly line");

        ILineStep step1 = new CaseStep();
        ILineStep step2 = new MotherboardStep();
        ILineStep step3 = new MonitorStep();

        IAssemblyLine line = new AssemblyLine(step1, step2, step3);

        IProduct laptopBlank = new Laptop();

        IProduct finished = line.assembleProduct(laptopBlank);

        System.out.println(finished);
    }
}
