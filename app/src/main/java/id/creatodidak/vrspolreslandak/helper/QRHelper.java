package id.creatodidak.vrspolreslandak.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Map;

import id.creatodidak.vrspolreslandak.R;

public class QRHelper {
    public static Bitmap generateQRCode(String content) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            return null;
        }
    }

    public static Bitmap QRLogo(Context context, String content) {
        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.resldkts);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(logoBitmap, 300, 390, false);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            // Adjust the margin parameter to add a small padding
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, 1); // Adjust the margin as needed

            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 1000, 1000, hints);

            // Convert the BitMatrix to a Bitmap
            int qrCodeWidth = bitMatrix.getWidth();
            int qrCodeHeight = bitMatrix.getHeight();
            Bitmap qrCodeBitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, Bitmap.Config.ARGB_8888);

            // Set white background color
            qrCodeBitmap.eraseColor(Color.WHITE);

            // Iterate over the BitMatrix and set black pixels for QR code modules
            for (int x = 0; x < qrCodeWidth; x++) {
                for (int y = 0; y < qrCodeHeight; y++) {
                    if (bitMatrix.get(x, y)) {
                        qrCodeBitmap.setPixel(x, y, Color.BLACK);
                    }
                }
            }

//            return overlayLogoOnQRCode(qrCodeBitmap, resizedBitmap);
            return qrCodeBitmap;
        } catch (WriterException e) {
            return null;
        }
    }
    public static Bitmap generateQRCodeWithLogo(String content, Bitmap logoBitmap) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix);

            return overlayLogoOnQRCode(qrCodeBitmap, logoBitmap);
        } catch (WriterException e) {
            return null;
        }
    }

    public static Bitmap overlayLogoOnQRCode(Bitmap qrCodeBitmap, Bitmap logoBitmap) {
        int qrCodeWidth = qrCodeBitmap.getWidth();
        int qrCodeHeight = qrCodeBitmap.getHeight();

        int logoWidth = logoBitmap.getWidth();
        int logoHeight = logoBitmap.getHeight();

        int posX = (qrCodeWidth - logoWidth) / 2;
        int posY = (qrCodeHeight - logoHeight) / 2;

        Bitmap combinedBitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, qrCodeBitmap.getConfig());
        Canvas canvas = new Canvas(combinedBitmap);
        canvas.drawBitmap(qrCodeBitmap, 0, 0, null);
        canvas.drawBitmap(logoBitmap, posX, posY, null);

        return combinedBitmap;
    }
}
