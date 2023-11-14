package id.creatodidak.vrspolreslandak.helper;

import java.util.Random;

public class RandomStringGenerator {
    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-";
    private static final String ALLOWED_NUMS = "0123456789";

    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static long randomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_NUMS.length());
            char randomChar = ALLOWED_NUMS.charAt(randomIndex);
            sb.append(randomChar);
        }

        // Mengonversi hasil akhir StringBuilder menjadi bilangan bulat
        try {
            long randomNumber = Long.parseLong(sb.toString());
            // Mengambil nilai absolut dari bilangan yang dihasilkan
            return Math.abs(randomNumber);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0L; // Mengembalikan nilai default jika ada kesalahan dalam mengonversi
        }
    }



}