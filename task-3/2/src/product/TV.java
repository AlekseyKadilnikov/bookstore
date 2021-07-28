package product;

public class TV extends Product {

    public TV(String model, int weight, int volume) {
        super(model, weight, volume);
    }

    @Override
    public void display() {
        System.out.println("Product type: TV");
        System.out.println("Model: " + getModel());
        System.out.println("Weight: " + getWeight());
        System.out.println("Volume: " + getVolume());
    }
}
