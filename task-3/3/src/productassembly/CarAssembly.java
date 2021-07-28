package productassembly;

import buildpart.BuildBody;
import buildpart.BuildChassis;
import buildpart.BuildEngine;
import product.IProduct;

public class CarAssembly implements IAssemblyLine {
    @Override
    public IProduct assembleProduct(IProduct product) {
        product.installFirstPart(new BuildBody().buildProductPart());
        product.installSecondPart(new BuildChassis().buildProductPart());
        product.installThirdPart(new BuildEngine().buildProductPart());
        return product;
    }
}
