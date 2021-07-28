package buildpart;

import productpart.Body;
import productpart.IProductPart;

public class BuildBody implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Собрана новая делаль: кузов");
        return new Body();
    }
}
