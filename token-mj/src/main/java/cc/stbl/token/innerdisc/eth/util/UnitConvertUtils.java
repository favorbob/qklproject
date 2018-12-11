package cc.stbl.token.innerdisc.eth.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class UnitConvertUtils {

    private static BigDecimal decimals = new BigDecimal("1000000000000000000");

    public static BigDecimal toEther(BigInteger bigInteger){
        if(bigInteger.intValue() == 0) return new BigDecimal("0");
        BigDecimal divide = new BigDecimal(bigInteger.toString()).divide(decimals);
        divide = divide.setScale(10,BigDecimal.ROUND_HALF_UP);
        return divide;
    }

    public static BigInteger toEtherBigInter(BigInteger bigInteger){
        if(bigInteger.intValue() == 0) return new BigInteger("0");
        return new BigInteger(bigInteger.toString()).divide(decimals.toBigInteger());
    }

    public static BigInteger toWei(BigDecimal bigDecimal){
        if(bigDecimal.doubleValue() == 0) return new BigInteger("0");
        BigDecimal multiply = bigDecimal.multiply(decimals);
        multiply = multiply.setScale(10,BigDecimal.ROUND_HALF_UP);
        return multiply.toBigInteger();
    }

    public static void main(String[] args) {
        System.out.println(toEther(new BigInteger("40000000000000000000000001")));
        System.out.println(toWei(new BigDecimal("0.61")));
    }
}
