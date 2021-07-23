package product;

import productpart.IProductPart;

public class Car implements IProduct{

    @Override
    public void installFirstPart(IProductPart part) {
        System.out.println("Установлена деталь: " + part.getName());
    }

    @Override
    public void installSecondPart(IProductPart part) {
        System.out.println("Установлена деталь: " + part.getName());
    }

    @Override
    public void installThirdPart(IProductPart part) {
        System.out.println("Установлена деталь: " + part.getName());
    }
}
