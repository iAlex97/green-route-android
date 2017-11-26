package com.alex.greenroute.views;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by alex on 26/11/2017.
 */

public class ShadowView extends View {
    private int currentColor = Color.parseColor("#3F51B5");
    private Paint bgPaint;

    private int width;
    private int height;

    public ShadowView(Context context) {
        super(context);
        init();
    }

    public ShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShadowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        bgPaint = new Paint();
        // smooths
        bgPaint.setDither(true);
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(currentColor);
        bgPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w / 2;
        this.height = h / 2;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(width, height, width, bgPaint);
        super.onDraw(canvas);
    }

    public void changeColor(final int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), currentColor, colorTo);
        colorAnimation.setDuration(500);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                currentColor = (Integer) animator.getAnimatedValue();
                bgPaint.setColor(currentColor);
                postInvalidate();
            }

        });
        colorAnimation.start();
    }
}
