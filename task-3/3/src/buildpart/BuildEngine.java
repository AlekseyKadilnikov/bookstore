package buildpart;

import productpart.Engine;
import productpart.IProductPart;

public class BuildEngine implements ILineStep {
    @Override
    public IProductPart buildProductPart() {
        System.out.println("Собрана новая делаль: двигаетль");
        return new Engine();
    }
}
