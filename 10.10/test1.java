package t1010t;

import java.util.Scanner;

public class test1 {
    static int sumUp (int n)  {
        //1부터 n까지의 합 구하기
            int sum = 0;
            for (int i=1; i<=n; i++)
                sum += i;  //sum에 i를 더한다.
            return sum;
        }

    public static void main(String[] args) {
        Scanner stdIn = new Scanner(System.in);

        System.out.println("1부터 x까지의 합을 구하자.");
        int x;
        do {
            System.out.println("x의 값 : ");
            x = stdIn.nextInt();
        }while (x <=0);

        System.out.println("1부터" + x + "까지의 합은" + sumUp(x)+ "입니다.");
    }
}
