public class BuildBody implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Собрана новая делаль: кузов");
        return new Body();
    }
}
