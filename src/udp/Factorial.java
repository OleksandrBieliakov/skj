package udp;

public class Factorial {

    public static int compute(int n) {
        if (n == 1 || n == 0) return 1;
        return n * compute(n - 1);
    }

}
