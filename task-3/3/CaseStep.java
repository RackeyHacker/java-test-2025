package laptopBuilding;

public class CaseStep implements ILineStep {

    @Override
    public IProductPart buildProductPart() {
        System.out.println("The case is being produced");
        return new Case();
    }
}
