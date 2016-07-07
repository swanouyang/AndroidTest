package com.swan.customviews.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class FiveChessView extends View {
	private static final int WIDTH_LINE = 2;
	private static final int SIZE = 100;
	private static final int OFFSET = 10;
	private static final float GRADIENT_SIZE_MULTIPLE = 0.625F;
	private static final float CHESS_SIZE_MULTIPLE = 0.5F;
	private static final int SHADOW_RADIUS = 6;
	private static final int SHADOW_DX = 4;
	private static final int SHADOW_DY = 4;
	private Paint mPaint;
	
	private enum ChessType {
		BLACK,
		WHITE,
	}

	public FiveChessView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	public void run() {
		postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int width = this.getMeasuredWidth();
		int height = this.getMeasuredHeight();
		int rows = height / SIZE;
		int columns = width / SIZE;
		
		drawChessBoard(canvas, rows, columns);
		drawChessPiece(canvas, 6, 4, ChessType.BLACK);
		drawChessPiece(canvas, 6, 5, ChessType.BLACK);
		drawChessPiece(canvas, 6, 3, ChessType.WHITE);
		drawChessPiece(canvas, 5, 4, ChessType.WHITE);
		drawChessPiece(canvas, 6, 6, ChessType.BLACK);
		drawChessPiece(canvas, 6, 7, ChessType.WHITE);
		drawChessPiece(canvas, 7, 4, ChessType.BLACK);
		drawChessPiece(canvas, 4, 5, ChessType.WHITE);
		drawChessPiece(canvas, 3, 6, ChessType.BLACK);
		drawChessPiece(canvas, 7, 2, ChessType.WHITE);
		drawChessPiece(canvas, 8, 1, ChessType.BLACK);
		drawChessPiece(canvas, 5, 6, ChessType.WHITE);
		drawChessPiece(canvas, 7, 8, ChessType.BLACK);
	}

	private void drawChessBoard(Canvas canvas, int rows, int columns) {
		mPaint.setColor(Color.DKGRAY);
		mPaint.setStrokeWidth(WIDTH_LINE);
		mPaint.setShadowLayer(0, 0, 0, Color.GRAY);
		for (int i = 0; i <= rows; i++) {
			canvas.drawLine(0, i * SIZE, columns * SIZE, i * SIZE, mPaint);
		}
		for (int i = 0; i <= columns; i++) {
			canvas.drawLine(i * SIZE, 0, i * SIZE, rows * SIZE, mPaint);
		}
	}

	private void drawChessPiece(Canvas canvas, int x, int y, ChessType chessType) {
		int colorOuter = (chessType == ChessType.BLACK) ? Color.BLACK : Color.LTGRAY;
		int colorInner = Color.WHITE;
		
		RadialGradient radialGradient = new RadialGradient(
				x * SIZE + OFFSET, y * SIZE + OFFSET, GRADIENT_SIZE_MULTIPLE * SIZE, 
				colorInner, colorOuter, Shader.TileMode.CLAMP);
		mPaint.setShader(radialGradient);
		this.setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);
		mPaint.setShadowLayer(SHADOW_RADIUS, SHADOW_DX, SHADOW_DY, 
				Color.parseColor("#AACCCCCC"));
		canvas.drawCircle(x * SIZE, y * SIZE, CHESS_SIZE_MULTIPLE * SIZE, mPaint);
	}
}
