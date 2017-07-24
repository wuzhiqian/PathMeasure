package com.wzq.pathmeasure;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by windows on 2017/7/24.
 */

public class CircleFlight extends View {

    private Paint paint;

    private Bitmap bitmap;
    private int mHeight;
    private int mWidth;
    private Path path;
    private PathMeasure pathMeasure;

    private Matrix matrix;
    private float distance;


    public CircleFlight(Context context) {
        this(context, null);
    }

    public CircleFlight(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleFlight(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleFlight(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap1 = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap1);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        Matrix m = new Matrix();
        m.postRotate(90);
        m.postScale(2, 2);
        bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(),
                m, true);
        return bitmap1;

    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);


//        Drawable drawable = getResources().getDrawable(R.drawable.ic_local_airport);
//        bitmap = drawableToBitmap(drawable);
        //BitmapFactory.decodeResource(getResources(), R.drawable.ic_local_airport);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arrow, options);
        path = new Path();

        matrix = new Matrix();
        distance = 0;
        startAnimation();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        //     startAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);

        path.reset();
        matrix.reset();
        path.addCircle(0, 0, 200, Path.Direction.CW);
        pathMeasure = new PathMeasure(path, false);
        canvas.drawPath(path, paint);

        pathMeasure.getMatrix(distance * pathMeasure.getLength(), matrix, PathMeasure.POSITION_MATRIX_FLAG | PathMeasure.TANGENT_MATRIX_FLAG);
        matrix.preTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2.0f);
        canvas.drawBitmap(bitmap, matrix, paint);

    }

    private void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                distance = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

}
