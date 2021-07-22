package product;

public class Refrigerator extends Product {
    public Refrigerator(String model, int weight, int volume) {
        super(model, weight, volume);
    }

    @Override
    public void display() {
        System.out.println("Product type: Refrigerator");
        System.out.println("Model: " + getModel());
        System.out.println("Weight: " + getWeight());
        System.out.println("Volume: " + getVolume());
    }
}
