package com.swan.customviews.views;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MoveBallView extends View {
	private static final int INIT_X = 100;
	private static final int INIT_Y = 100;
	private static final int RADIUS = 30;
	private static final int COLOR = Color.RED;
	private static final int OFFSET = 8;
	private static final int MAX_RANDOM_OFFSET = 8;
	private int mX;
	private int mY;
	private int mOffsetX;
	private int mOffsetY;
	private Paint mPaint;
	private Direction mLeftOrRight;
	private Direction mUpOrDown;
	private Random mRandom;
	
	public enum Direction {
		LEFT (0),
		RIGHT (1),
		UP (2),
		DOWN (3);
		
		private Direction(int nativeInt) {
			this.nativeInt = nativeInt;
		}
		final int nativeInt;
	}
	
	public MoveBallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(COLOR);
		mX = INIT_X;
		mY = INIT_Y;
		mOffsetX = OFFSET;
		mOffsetY = OFFSET;
		mLeftOrRight = Direction.RIGHT;
		mUpOrDown = Direction.DOWN;
		mRandom = new Random();
	}
	
	public void run() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				postInvalidate();
			}
		}, 100, 20);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawCircle(mX, mY, RADIUS, mPaint);
		int width = this.getMeasuredWidth();
		int height = this.getMeasuredHeight();
		if (mX < RADIUS) {
			mLeftOrRight = Direction.RIGHT;
			mOffsetX = OFFSET+mRandom.nextInt(MAX_RANDOM_OFFSET);
		}
		if (mX >= width - RADIUS) {
			mLeftOrRight = Direction.LEFT;
			mOffsetX = OFFSET+mRandom.nextInt(MAX_RANDOM_OFFSET);
		}
		if (mY < RADIUS) {
			mUpOrDown = Direction.DOWN;
			mOffsetY = OFFSET+mRandom.nextInt(MAX_RANDOM_OFFSET);
		}
		if (mY >= height - RADIUS) {
			mUpOrDown = Direction.UP;
			mOffsetY = OFFSET+mRandom.nextInt(MAX_RANDOM_OFFSET);
		}
		
		if (mLeftOrRight == Direction.LEFT) {
			mX -= mOffsetX;
		} else if (mLeftOrRight == Direction.RIGHT){
			mX += mOffsetX;
		}
		if (mUpOrDown == Direction.UP) {
			mY -= mOffsetY;
		} else if (mUpOrDown == Direction.DOWN){
			mY += mOffsetY;
		}
	}
}
