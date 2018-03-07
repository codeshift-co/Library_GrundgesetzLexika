package com.example.lfranken.grundgesetzlexikalibrary.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.lfranken.grundgesetzlexikalibrary.R;

/**
 * Created by lfranken on 14.12.2017.
 */

public class CornerTriangleView extends View {

    private static final int UP_LEFT = 0;
    private static final int UP_RIGHT = 1;
    private static final int DOWN_RIGHT = 2;
    private static final int DOWN_LEFT = 3;
    private static final int DEFAULT_COLOR = Color.WHITE;

    private int corner = UP_LEFT;
    private int startColor;
    private int endColor;
    private float widthFactor;
    private float heightFactor;
    private Paint paint;

    public CornerTriangleView(Context context) {
        this(context, null);
    }

    public CornerTriangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerTriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CornerTriangleView);
        this.corner = typedArray.getInt(R.styleable.CornerTriangleView_corner, UP_LEFT);
        this.startColor = typedArray.getColor(R.styleable.CornerTriangleView_start_color, DEFAULT_COLOR);
        this.endColor = typedArray.getColor(R.styleable.CornerTriangleView_end_color, DEFAULT_COLOR);
        int alpha = Math.round(255 * typedArray.getFloat(R.styleable.CornerTriangleView_alpha, 1f));
        this.widthFactor = typedArray.getFloat(R.styleable.CornerTriangleView_width_factor, 1f);
        this.heightFactor = typedArray.getFloat(R.styleable.CornerTriangleView_height_factor, 1f);
        typedArray.recycle();

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(alpha);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(makeTrianglePath(), paint);
    }

    private Path makeTrianglePath(){
        Path trianglePath = new Path();
        int width = Math.round(getWidth() * widthFactor);
        int height = Math.round(getHeight() * heightFactor);
        Point a, b, c;
        float x0, y0, x1, y1;
        switch (corner){
            case UP_LEFT:
                a = new Point(0, 0);
                b = new Point(width, 0);
                c = new Point(0, height);
                x0 = y0 = x1 = 0;
                y1 = height;
                break;
            case UP_RIGHT:
                a = new Point(width, 0);
                b = new Point(width, height);
                c = new Point(getCXForUpRight(width, widthFactor), 0);
                x0 = y0 = 0;
                x1 = width;
                y1 = height;
                break;
            case DOWN_RIGHT:
                a = new Point(width, height);
                b = new Point(0, height);
                c = new Point(width, 0);
                x0 = x1 = width;
                y0 = 0;
                y1 = height;
                break;
            case DOWN_LEFT:
            default:
                a = new Point(0, height);
                b = new Point(0, 0);
                c = new Point(width, height);
                x0 = x1 = y1 = 0;
                y0 = height;
                break;
        }
        paint.setShader(new LinearGradient(x0, y0, x1, y1, startColor, endColor, Shader.TileMode.REPEAT));
        trianglePath.moveTo(a.x, a.y);
        trianglePath.lineTo(b.x, b.y);
        trianglePath.lineTo(c.x, c.y);
        return trianglePath;
    }

    private int getCXForUpRight(int width, float widthFactor){
        if (widthFactor <= 1) return 0;
        return Math.round(-(widthFactor-1) * width);
    }

}
