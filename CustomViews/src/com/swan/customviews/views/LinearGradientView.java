package com.swan.customviews.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation") public class LinearGradientView extends View {
	private static final int OFFSET = 100;
	private Paint mPaint;
	
	public LinearGradientView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.FILL);
	}
	
	public void run() {
		postInvalidate();
	}
	
	@SuppressLint("DrawAllocation") 
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect rect1 = new Rect(100, 100, 500, 400);
		LinearGradient linearGradient = new LinearGradient(
				rect1.left, rect1.top, rect1.right, rect1.bottom, 
				Color.RED, Color.BLUE, Shader.TileMode.CLAMP);
		mPaint.setShader(linearGradient);
		canvas.drawRect(rect1, mPaint);
		
		canvas.translate(0, rect1.height() + OFFSET);
		
		Rect rect2 = new Rect(rect1);
		rect2.inset(-100, -100);
		linearGradient = new LinearGradient(
				rect2.left, rect2.top, rect2.right, rect2.bottom, 
				Color.RED, Color.BLUE, Shader.TileMode.CLAMP);
		mPaint.setShader(linearGradient);
		canvas.drawRect(rect1, mPaint);
		
		canvas.translate(0, rect1.height() + OFFSET);
		
		Rect rect3 = new Rect(rect1);
		rect3.inset(100, 100);
		linearGradient = new LinearGradient(
				rect3.left, rect3.top, rect3.right, rect3.bottom, 
				Color.RED, Color.BLUE, Shader.TileMode.CLAMP);
		mPaint.setShader(linearGradient);
		canvas.drawRect(rect1, mPaint);
	}
}


