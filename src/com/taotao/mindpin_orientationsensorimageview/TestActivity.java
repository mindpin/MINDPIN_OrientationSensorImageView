package com.taotao.mindpin_orientationsensorimageview;

import android.app.Activity;
import android.os.Bundle;
/**
 * 
 * @author taotao
 *
 */
public class TestActivity extends Activity{
	
	
	OrientationSensorImageView sensorView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivty_test);
		sensorView = (OrientationSensorImageView) findViewById(R.id.sensorview);
		sensorView.setImageResource(R.drawable.qingmin);
		sensorView.setMoveStepDuration(35);
		sensorView.setLeanFactor(0.45f);
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sensorView.resume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sensorView.pause();
	}
	
}
