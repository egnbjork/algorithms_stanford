import java.util.*;
import java.math.*;

public class Multiplication {

    public static void main(String[] arg){
        String num1 = arg[0];
        String num2 = arg[1];

        String result = karatsuba(new BigDecimal(num1), new BigDecimal(num2)).toString();
        System.out.println("result is:" + result);
    }

    private static BigDecimal karatsuba(BigDecimal num1, BigDecimal num2) {
        if(num1.compareTo(BigDecimal.TEN) < 0 || num2.compareTo(BigDecimal.TEN) < 0) {
            return num1.multiply(num2);
        }

        int m = num1.toString().length() / 2;
        System.out.println("m is " + m);

        BigDecimal[] firstNumber = num1.divideAndRemainder(BigDecimal.TEN.pow(m));
        BigDecimal a = firstNumber[0];
        BigDecimal b = firstNumber[1];

        BigDecimal[] secondNumber = num2.divideAndRemainder(BigDecimal.TEN.pow(m));
        BigDecimal c = secondNumber[0];
        BigDecimal d = secondNumber[1];

        BigDecimal ac = karatsuba(a, c);
        System.out.println("ac = " + ac.toString());
        BigDecimal bd = karatsuba(b, d);
        System.out.println("bd = " + bd.toString());

        BigDecimal ad = karatsuba(a, d);
        BigDecimal bc = karatsuba(b, c);
        BigDecimal gauss = ad.add(bc);

        BigDecimal step1 = BigDecimal.TEN.pow(m * 2).multiply(ac);
        BigDecimal step2 = BigDecimal.TEN.pow(m).multiply(ad.add(bc));
        return step1.add(step2).add(bd);
    }
}
