import java.util.Scanner;

public class test07 {
    public static void main(String[] args) {
        Scanner stdIn = new Scanner(System.in);
        System.out.print("몇 cm부터 :");
        int hMin = stdIn.nextInt();
        System.out.print("몇 cm까지 :");
        int hMax = stdIn.nextInt();
        System.out.print("몇 cm단위 : :");
        int step = stdIn.nextInt();
        System.out.print("신장 표준 체중");
        System.out.println("--------");

        for (int i = hMin; i <= hMax; i+=step)
            System.out.println(i + ""+0.9 * (i-100));
    }
}
