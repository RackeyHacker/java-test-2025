package laptopBuilding;

public class MotherboardStep implements ILineStep {

    @Override
    public IProductPart buildProductPart() {
        System.out.println("The monitor is being produced");
        return new Motherboard();
    }
}
