package id.creatodidak.vrspolreslandak.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class VerticalSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(-90); // Putar searah jarum jam untuk membuat vertikal
        canvas.translate(-getHeight(), 0); // Pindahkan posisi
        super.onDraw(canvas);
    }
}
