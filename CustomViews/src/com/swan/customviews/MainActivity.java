package com.swan.customviews;


import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends Activity {
	public static final String VIEW_STRING = "MainActivity.ToCustomViewActivity";
	private static final String MOVE_BALL_VIEW = "com.swan.customviews.views.MoveBallView";
	private static final String WATCH_VIEW = "com.swan.customviews.views.WatchView";
	//private MoveBallView moveBall;
	//private WatchView watch;
	private ListView listView = null;
	private List<String> viewList = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, viewList);
        listView.setAdapter(adapter);
        
        viewList.add(MOVE_BALL_VIEW);
        viewList.add(WATCH_VIEW);
        
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this, ViewShowActivity.class);
		        intent.putExtra(VIEW_STRING, viewList.get(position));
		        startActivity(intent);
			}
		});
    }
}
