package com.swan.customviews;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


public class MainActivity extends Activity {
	private MoveBallView moveBall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        moveBall = (MoveBallView) findViewById(R.id.moveball);
        new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				moveBall.postInvalidate();
			}
		}, 200, 50);//delay 200ms, run for each 50ms.
    }
}
