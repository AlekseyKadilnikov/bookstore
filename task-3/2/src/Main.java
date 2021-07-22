import product.Product;
import product.Refrigerator;
import product.TV;
import product.Washer;
import warehouse.Warehouse;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        Product product1 = new Refrigerator("LG", 100, 45);
        Product product2 = new Washer("Hotpoint", 100, 56);
        Product product3 = new TV("Xiaomi", 10, 30);

        warehouse.addProduct(product1);
        warehouse.addProduct(product2);
        warehouse.addProduct(product3);
        warehouse.addProduct(product1);
        warehouse.addProduct(product3);
    }
}
