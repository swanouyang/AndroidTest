package com.swan.customviews.views;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class WatchView extends View {
	private static final int WIDTH_CIRCLE = 3;
	private static final int WIDTH_HOUR = 5;
	private static final int WIDTH_MINUTE = 4;
	private static final int WIDTH_SECOND = 2;
	private static final int WIDTH_CUSOR_LARGE = 5;
	private static final int WIDTH_CUSOR_SMALL = 3;
	private static final float WIDTH_NUMBER = 1.0F;
	
	private Paint mPaint;
	private Calendar mCalendar;
	
	public WatchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		
		mCalendar = Calendar.getInstance();
	}
	
	public void run() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				postInvalidate();
			}
		}, 0, 1000);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int width = this.getMeasuredWidth();
		int height = this.getMeasuredHeight();
		int len = Math.min(width, height);
		drawPlate(canvas, len);
		drawPoints(canvas, len);
	}

	/*
	 * draw plate of watch
	 */
	private void drawPlate(Canvas canvas, int len) {
		canvas.save();
		
		int r = len / 2;
		mPaint.setColor(Color.GRAY);
		mPaint.setStrokeWidth(WIDTH_CIRCLE);
		canvas.drawCircle(r, r, r, mPaint);
		for (int i = 0; i < 60; i++) {
			if (i % 5 == 0) {
				mPaint.setColor(Color.RED);
				mPaint.setStrokeWidth(WIDTH_CUSOR_LARGE);
				canvas.drawLine(r + 9*r/10, r, len, r, mPaint);
			} else {
				mPaint.setColor(Color.LTGRAY);
				mPaint.setStrokeWidth(WIDTH_CUSOR_SMALL);
				canvas.drawLine(r + 14*r/15, r, len, r, mPaint);
			}
			canvas.rotate(360/60, r, r);
		}
		
		canvas.restore();
		canvas.save();
		
		mPaint.setColor(Color.GRAY);
		mPaint.setStrokeWidth(WIDTH_NUMBER);
		//mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(18.0f);
		int numberX = 0;
		int numberY = 0;
		int degree = 0;
		double radians = 0;
		int hour = 0;
		for (int i = 0; i < 12; i++) {
			degree = 30 * i;
			radians = Math.toRadians(degree);
			numberX = r + (int)(0.81 * r * Math.cos(radians));
			numberY = r + (int)(0.81 * r * Math.sin(radians));
			hour = (i + 3) % 12;
			if ( hour == 0 ) {
				hour = 12;
			}
			canvas.drawText(Integer.toString(hour), numberX, numberY, mPaint);
		}
					
		canvas.restore();
	}
	
	private void drawPoints(Canvas canvas, int len) {
		canvas.save();
		
		int r = len/2;
		mCalendar.setTimeInMillis(System.currentTimeMillis());
		int hours = mCalendar.get(Calendar.HOUR) % 12;
		int minutes = mCalendar.get(Calendar.MINUTE);
		int seconds = mCalendar.get(Calendar.SECOND);
		
		//int degree = 360 / 12 * hours;//hour point move(jump) every hour
		double degree = 360.0 / (12 * 60 * 60) * 
				(hours * 60 * 60 + minutes * 60 + seconds);//hour point move(jump) every seconds.
		double radians = Math.toRadians(degree);
		int endX = r + (int)(r * 0.4 * Math.cos(radians));
		int endY = r + (int)(r * 0.4 * Math.sin(radians));
		mPaint.setStrokeWidth(WIDTH_HOUR);
		mPaint.setColor(Color.BLACK);
		canvas.rotate(-90, r, r);
		canvas.drawLine(r, r, endX, endY, mPaint);
		
		canvas.restore();
		canvas.save();
		
		//degree = 360 / 60 * minutes;
		degree = 360.0 / (60 * 60) * (60 * minutes + seconds);//minute point move(jump) every seconds.
		radians = Math.toRadians(degree);
		endX = r + (int)(r * 0.6 * Math.cos(radians));
		endY = r + (int)(r * 0.6 * Math.sin(radians));
		mPaint.setStrokeWidth(WIDTH_MINUTE);
		mPaint.setColor(Color.parseColor("#FF555555"));
		canvas.rotate(-90, r, r);
		canvas.drawLine(r, r, endX, endY, mPaint);
		
		canvas.restore();
		canvas.save();
		
		degree = 360 / 60 * seconds;
		radians = Math.toRadians(degree);
		endX = r + (int)(r * 0.8 * Math.cos(radians));
		endY = r + (int)(r * 0.8 * Math.sin(radians));
		mPaint.setStrokeWidth(WIDTH_SECOND);
		mPaint.setColor(Color.parseColor("#FFFF5555"));
		canvas.rotate(-90, r, r);
		canvas.drawLine(r, r, endX, endY, mPaint);
		
		canvas.restore();
	}
}


