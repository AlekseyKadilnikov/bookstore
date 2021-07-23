public class BuildChassis implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Собрана новая делаль: шасси");
        return new Chassis();
    }
}
