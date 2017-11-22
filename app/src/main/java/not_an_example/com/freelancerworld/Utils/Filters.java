package not_an_example.com.freelancerworld.Utils;

import not_an_example.com.freelancerworld.Enum.PaymentMethod;

public class Filters {
    private static int rangeFromYou;
    private static int rangeFromOffice;
    private static int minPayment;
    private static int maxPayment;
    private static float minTime;
    private static float maxTime;
    private static PaymentMethod paymentMethod = PaymentMethod.NOSERVICE;

    public static int getRangeFromYou() {
        return rangeFromYou;
    }

    public static void setRangeFromYou(int rangeFromYou) {
        Filters.rangeFromYou = rangeFromYou;
    }

    public static int getRangeFromOffice() {
        return rangeFromOffice;
    }

    public static void setRangeFromOffice(int rangeFromOffice) {
        Filters.rangeFromOffice = rangeFromOffice;
    }

    public static int getMinPayment() {
        return minPayment;
    }

    public static void setMinPayment(int minPayment) {
        Filters.minPayment = minPayment;
    }

    public static int getMaxPayment() {
        return maxPayment;
    }

    public static void setMaxPayment(int maxPayment) {
        Filters.maxPayment = maxPayment;
    }

    public static float getMinTime() {
        return minTime;
    }

    public static void setMinTime(float minTime) {
        Filters.minTime = minTime;
    }

    public static float getMaxTime() {
        return maxTime;
    }

    public static void setMaxTime(float maxTime) {
        Filters.maxTime = maxTime;
    }

    public static PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public static void setPaymentMethod(PaymentMethod paymentMethod) {
        Filters.paymentMethod = paymentMethod;
    }
}
