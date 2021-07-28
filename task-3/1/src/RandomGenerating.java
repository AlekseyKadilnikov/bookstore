import java.util.Random;

public class RandomGenerating {
    public static void main(String[] args) {
        int num = GenerateRandomNumber(100, 1000);
        System.out.println("Number = " + num);
        System.out.println("Max digit = " + GetMaxDigit(num));
    }

    public static int GenerateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static int GetMaxDigit(int num) {
        int maxDigit = 0;
        int temp;
        int numLength = String.valueOf(num).length();
        for(int i = 0; i < numLength; i++) {
            temp = num % 10;
            if(temp > maxDigit) {
                maxDigit = temp;
            }
            num /= 10;
        }
        return maxDigit;
    }
}
