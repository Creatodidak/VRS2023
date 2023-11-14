package id.creatodidak.vrspolreslandak.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Locale;

import id.creatodidak.vrspolreslandak.R;

public class BitmapUtils {
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap createBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }

    public static String rasio(String photoPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);

        int width = options.outWidth;
        int height = options.outHeight;

        int fpb = NumberHelper.FPB(width, height);
        int simplifiedWidth;
        int simplifiedHeight;

        if (fpb != 0) {
            simplifiedWidth = width / fpb;
            simplifiedHeight = height / fpb;
        } else {
            simplifiedWidth = 3;
            simplifiedHeight = 4;
        }

        return  String.format(Locale.getDefault(), "%d:%d", simplifiedWidth, simplifiedHeight);
    }

    public static AlertDialog expandImage(Context context, String image, String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.myFullscreenAlertDialogStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_image_zoom, null);

        PhotoView zoomableImageView = view.findViewById(R.id.zoomableImageView);
        TextView judul = view.findViewById(R.id.textView51);
        judul.setText(s);
        Button btn = view.findViewById(R.id.button);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(image)
                .placeholder(R.drawable.defaultimg)
                .error(R.drawable.failedimg)
                .apply(requestOptions)
                .into(zoomableImageView);

        builder.setView(view);

        AlertDialog dialog = builder.create();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // Set dialog window attributes
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();

        return dialog;
    }

}
