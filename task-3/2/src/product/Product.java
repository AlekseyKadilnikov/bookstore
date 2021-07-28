package product;

public abstract class Product {
    private String model;
    private int weight;
    private int volume;

    public Product(String model, int weight, int volume) {
        this.model = model;
        this.weight = weight;
        this.volume = volume;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public abstract void display();
}
