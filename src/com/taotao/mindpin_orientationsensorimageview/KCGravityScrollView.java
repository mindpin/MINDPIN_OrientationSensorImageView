package com.taotao.mindpin_orientationsensorimageview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

public class KCGravityScrollView extends FrameLayout {

	public KCGravityScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public KCGravityScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	
	OrientationSensorImageView child;
	private void init(){
		child = new OrientationSensorImageView(getContext());
		child.setMoveStepDuration(35);
		child.setLeanFactor(0.45f);
		FrameLayout.LayoutParams parmas = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		this.addView(child, parmas);
	}
	
	public void set_drawable(int resId){
		child.setImageResource(resId);
	}
	
	public void set_drawable(Drawable drawable){
		child.setImageDrawable(drawable);
	}
	
	/**
	 * 在Activity onResume()时调用
	 */
	public void resume(){
		// 为系统的方向传感器注册监听器  
		child.resume();
         
	}
	
	/**
	 * 在Activity onPause()时调用
	 */
	public void pause(){ 
		// 取消注册  
		child.pause();
        
	}
	

}
