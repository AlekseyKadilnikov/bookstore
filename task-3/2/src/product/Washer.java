package product;

public class Washer extends Product {
    public Washer(String model, int weight, int volume) {
        super(model, weight, volume);
    }

    @Override
    public void display() {
        System.out.println("Product type: Washer");
        System.out.println("Model: " + getModel());
        System.out.println("Weight: " + getWeight());
        System.out.println("Volume: " + getVolume());
    }
}
