package id.creatodidak.vrspolreslandak.helper;

public class NumberHelper {
    public static int FPB(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
