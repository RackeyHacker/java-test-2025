package randomNumbers;

public class RandomNumbers {
    public static void main(String[] args) {

        int num1 = 100 + (new java.util.Random()).nextInt(900);
        System.out.println("First num: " + num1);
        int num2 = 100 + (new java.util.Random()).nextInt(900);
        System.out.println("Second num: " + num2);
        int num3 = 100 + (new java.util.Random()).nextInt(900);
        System.out.println("Third num: " + num3);

        int combo = Integer.parseInt("" + num1 + num2);
        int diff = combo - num3;
        System.out.println("Difference: " + diff);
    }
}