package productassembly;

import product.IProduct;

public interface IAssemblyLine {
    IProduct assembleProduct(IProduct product);
}
