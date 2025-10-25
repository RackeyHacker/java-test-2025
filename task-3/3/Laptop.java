package laptopBuilding;

public class Laptop implements IProduct {
    private IProductPart firstPart;
    private IProductPart secondPart;
    private IProductPart thirdPart;

    @Override
    public void installFirstPart(IProductPart part) {
        this.firstPart = part;
        System.out.println("Installing first component: Case");
    }

    @Override
    public void installSecondPart(IProductPart part) {
        this.secondPart = part;
        System.out.println("Installing second component: Motherboard");
    }

    @Override
    public void installThirdPart(IProductPart part) {
        this.thirdPart = part;
        System.out.println("Installing third component: Monitor");
    }
}
