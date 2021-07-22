package warehouse;

import product.Product;

public class Warehouse implements ProductMovement {
    private static final int MAX_VOLUME = 200;
    private int currentVolume = 0;
    private int totalWeight = 0;

    @Override
    public void addProduct(Product product) {
        if(currentVolume + product.getVolume() <= MAX_VOLUME) {
            currentVolume += product.getVolume();
            totalWeight += product.getWeight();
            System.out.println("Product added to warehouse:");
            product.display();
        }
        else {
            System.out.println("Failed: no space in warehouse.");
            System.out.println("Max volume: " + MAX_VOLUME);
            System.out.println("Current volume: " + currentVolume);
            System.out.println("Total weight: " + totalWeight);
        }
        System.out.println();
    }
}
