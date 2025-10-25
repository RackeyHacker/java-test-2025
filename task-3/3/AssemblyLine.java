package laptopBuilding;

public class AssemblyLine implements IAssemblyLine {
    private final ILineStep step1;
    private final ILineStep step2;
    private final ILineStep step3;

    public AssemblyLine(ILineStep step1, ILineStep step2, ILineStep step3) {
        this.step1 = step1;
        this.step2 = step2;
        this.step3 = step3;
    }

    @Override
    public IProduct assembleProduct(IProduct product) {
        System.out.println("The beginning of laptop assembly");

        IProductPart p1 = step1.buildProductPart();
        product.installFirstPart(p1);
        IProductPart p2 = step2.buildProductPart();
        product.installSecondPart(p2);
        IProductPart p3 = step3.buildProductPart();
        product.installThirdPart(p3);

        System.out.println("Laptop assembly is complete");
        return product;
    }
}
