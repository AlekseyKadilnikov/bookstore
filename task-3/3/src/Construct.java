public class Construct {
    public static void main(String[] args) {
        IProduct car = new Car();
        CarAssembly carAssembly = new CarAssembly();
        car = carAssembly.assembleProduct(car);
    }
}
