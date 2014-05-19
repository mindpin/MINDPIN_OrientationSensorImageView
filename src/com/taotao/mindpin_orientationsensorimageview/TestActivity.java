package com.taotao.mindpin_orientationsensorimageview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
/**
 * 
 * @author taotao
 *
 */
public class TestActivity extends Activity{
	
	
	//OrientationSensorImageView sensorView;
	KCGravityScrollView view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivty_test);
		view = (KCGravityScrollView) findViewById(R.id.root);
		view.set_drawable(R.drawable.qingmin);
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		view.resume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		view.pause();
	}
	
}
