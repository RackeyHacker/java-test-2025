package laptopBuilding;

public class MonitorStep implements ILineStep {

    @Override
    public IProductPart buildProductPart() {
        System.out.println("The monitor is being produced");
        return new Monitor();
    }
}
