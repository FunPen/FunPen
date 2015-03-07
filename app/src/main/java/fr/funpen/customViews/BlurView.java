package fr.funpen.customViews;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BlurView extends View {

	private Paint paint;
    private RectF background = new RectF();

    public BlurView(Context context) {
        this(context, null, 0);
    }

    public BlurView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlurView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint(0);
        paint.setColor(0xcc000000);
        paint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        
        background.top = 0;
        background.bottom = 600;
        background.left = 0;
        background.right = 400;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(background, paint);
    }

}