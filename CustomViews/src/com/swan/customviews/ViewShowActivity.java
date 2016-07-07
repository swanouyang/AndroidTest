package com.swan.customviews;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewShowActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		String viewString = getIntent().getStringExtra(MainActivity.VIEW_STRING);
		
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		TextView textView = new TextView(this);
		textView.setText(viewString);
		textView.setTextSize(18.0F);
		linearLayout.addView(textView);
		
		try {
			Class<?> viewClass = Class.forName(viewString);
			Log.d("ViewShowActivity", viewClass.toString());
			Constructor<?> constructor = viewClass.getConstructor(Context.class, AttributeSet.class);
			View view = (View) constructor.newInstance(this, null);
			
			Method method = viewClass.getMethod("run");
			method.invoke(view);
			linearLayout.addView(view);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		setContentView(linearLayout);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
		Log.d("ViewShowActivity", "Finish");
	}
}
