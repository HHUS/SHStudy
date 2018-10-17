package com.csii.sh.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by on 2017/10/15.
 */

public class ArcHeaderView extends View {


    private Paint mPaint;
    private PointF mStartPoint,mEndPoint,mControlPoint;
    private int mWidth;
    private int mHeight;
    private Path mPath = new Path();
    private int mARCHeight = 100;

    private int mStartColor;
    private int mEndColor;

    private LinearGradient mLinearGradient;


    public ArcHeaderView(Context context) {
        super(context);
        init();
    }

    public ArcHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArcHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.FILL);

        mStartPoint = new PointF(0,0);
        mEndPoint = new PointF(0,0);
        mControlPoint = new PointF(0,0);

        mStartColor = Color.parseColor("#FF3A80");
        mEndColor = Color.parseColor("#FF3745");


    }


    public void setColor(int startColor,int endColor){
        mStartColor = startColor;
        mEndColor = endColor;
        mLinearGradient = new LinearGradient(mWidth / 2,0,mWidth / 2,mHeight,mStartColor,mEndColor, Shader.TileMode.MIRROR);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        mWidth = w;
        mHeight = h;
        mPaint.reset();

        mPath.moveTo(0,0);

        mPath.addRect(0,0,mWidth,mHeight-mARCHeight, Path.Direction.CCW);

        mStartPoint.x = 0;
        mStartPoint.y = mHeight - mARCHeight;

        mEndPoint.x = mWidth;
        mEndPoint.y = mHeight - mARCHeight;

        mControlPoint.x = mWidth / 2 - mARCHeight / 2;
        mControlPoint.y = mHeight + mARCHeight;


        mLinearGradient = new LinearGradient(mWidth / 2,0,mWidth / 2,mHeight,mStartColor,mEndColor, Shader.TileMode.MIRROR);

        invalidate();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setShader(mLinearGradient);

        mPath.moveTo(mStartPoint.x,mStartPoint.y);

        mPath.quadTo(mControlPoint.x,mControlPoint.y,mEndPoint.x,mEndPoint.y);

        canvas.drawPath(mPath,mPaint);
    }
}


