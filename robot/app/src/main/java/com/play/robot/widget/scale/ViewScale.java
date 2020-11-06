package com.play.robot.widget.scale;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ViewScale extends View {

    private static final int SPACING = 40; // 每边距离View边缘的间距
    private static final int SCALE_LINE_LENGTH = 35; // 刻度尺每条线的长度
    private static final int SCALE_LINE_UNIT = 10; // 需要把刻度尺分成几等份

    private Paint mScaleLinePaint;
    private Paint mSignPaint;
    private Paint mTextPaint;
    private Path mSignPath;

    private int mValue;

    public ViewScale(Context context) {
        this(context, null);
    }

    public ViewScale(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewScale(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScaleLinePaint = new Paint();
        mScaleLinePaint.setColor(Color.WHITE);
        mScaleLinePaint.setAntiAlias(true);
        mScaleLinePaint.setStyle(Paint.Style.STROKE);
        mScaleLinePaint.setStrokeWidth(2);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setStrokeWidth(2);
        mTextPaint.setTextSize(24);

        mSignPaint = new Paint();
        mSignPaint.setColor(Color.RED);
        mSignPaint.setAntiAlias(true);
        mSignPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mSignPath = new Path();

//        setBackgroundColor(Color.argb(50, 0, 0, 0));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 每等份刻度尺的间隔
        float unit = (getWidth() - SPACING * 2) / SCALE_LINE_UNIT;

        // 画下面的一条横线
//        canvas.drawLine(SPACING, SCALE_LINE_LENGTH * 2, getWidth() - SPACING, SCALE_LINE_LENGTH * 2, mScaleLinePaint);

        // 画下面的标志（三角形）
        // 起点
        mSignPath.moveTo(getWidth() / 2, SCALE_LINE_LENGTH * 2 + 10);
        // 另外两个点
        mSignPath.lineTo(getWidth() / 2 - SCALE_LINE_LENGTH, SCALE_LINE_LENGTH * 3 + 10);
        mSignPath.lineTo(getWidth() / 2 + SCALE_LINE_LENGTH, SCALE_LINE_LENGTH * 3 + 10);
        // 闭合
        mSignPath.close();
        canvas.drawPath(mSignPath, mSignPaint);

        // 商
        int merchant = mValue / 10;
        // 余数
        int remainder = mValue % 10;
        // 偏移值
        int offset = getWidth() / 50 * (mValue % 5);
        // 当前值换算成（末尾为5或者0）的值
        int temp;
        if (remainder > 0) {
            if (remainder < 5) {
                temp = merchant * 10;
            } else if (remainder > 5) {
                temp = merchant * 10 + 5;
            } else {
                temp = mValue;
            }
        } else {
            temp = mValue;
        }

        for (int i = 0; i < 11; i++) {
            // 需要画线的x坐标值
            float x = unit * i + SPACING - offset;
            // 画刻度线
            canvas.drawLine(x, SCALE_LINE_LENGTH + 10, x, SCALE_LINE_LENGTH * 2, mScaleLinePaint);
            // 画刻度值
            int tt;
            int t = temp + 5 * i - 25;
            // 此公式在0°前后会出现正负，所以需要进行下面的判断，保证最后的数为0 - 360之间的数
            if (t < 0) {
                tt = 360 + t;
            } else if (t > 360) {
                tt = t - 360;
            } else {
                tt = t;
            }
            canvas.drawText(OrientationUtils.calculateDegree(tt), x, SCALE_LINE_LENGTH, mTextPaint);
        }
    }

    private int measureHeight(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 100;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    private int measureWidth(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 75;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;

    }

    /**
     * 设置当前值
     *
     * @param value 当前值
     */
    public void setValues(int value) {
        this.mValue = value;
        invalidate();
    }

}
