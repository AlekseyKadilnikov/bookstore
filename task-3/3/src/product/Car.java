package product;

import productpart.IProductPart;

public class Car implements IProduct{
    IProductPart body;
    IProductPart engine;
    IProductPart chassis;

    @Override
    public void installFirstPart(IProductPart body) {
        this.body = body;
        System.out.println("Установлена деталь: " + body.getName());
    }

    @Override
    public void installSecondPart(IProductPart chassis) {
        this.chassis = chassis;
        System.out.println("Установлена деталь: " + chassis.getName());
    }

    @Override
    public void installThirdPart(IProductPart engine) {
        this.engine = engine;
        System.out.println("Установлена деталь: " + engine.getName());
    }
}
