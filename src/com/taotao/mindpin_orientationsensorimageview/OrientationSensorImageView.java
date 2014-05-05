package com.taotao.mindpin_orientationsensorimageview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
/**
 * 
 * @author taotao
 * 注意：OrientationSensorImageView 不能作为layout的根视图。
 */
public class OrientationSensorImageView extends ImageView implements SensorEventListener{
	private static String TAG = "OrientationSensorImageView";

	public OrientationSensorImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public OrientationSensorImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public OrientationSensorImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	
	
	
	
	int width, height;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		Drawable draw = getDrawable();
		if(draw != null){
			Log.d(TAG, "onMeasure>>>width = " + width + " height = " + height + " draw.getIntrinsicWidth() = " + 
					draw.getIntrinsicWidth() + " draw.getIntrinsicHeight() = " + draw.getIntrinsicHeight());
			int drawWidth = draw.getIntrinsicWidth();
			int drawHeight = draw.getIntrinsicHeight();
			float rationW = (float)width/drawWidth;
			float rationH = (float)height/drawHeight;
			//LogUtil.d(this, " rationW = " + rationW + " rationH = " + rationH);
			if(rationW * drawHeight > height){
				setMeasuredDimension(width, (int)(rationW * drawHeight));
			}else{
				setMeasuredDimension((int)(rationH * drawWidth), height);
			}
			
		}else{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
		
	}
	
	
	private int rightBorder, leftBorder;//move的左右边界
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top , right, bottom );
		Log.d(TAG , "onLayout >>>> "+ changed + " --  " + left + " / " + top + "   " + right + " / " + bottom + "   ");
		rightBorder = right - width;
		leftBorder = left ;
		int sX = 0, sY = 0;
		if(width < (right - left)){
			sX = ((right - left) - width)/2 + left;
		}
		
		if((bottom - top) > height){
			sY = ((bottom - top) - height)/2 + top;
		}
		
		scrollTo(sX, sY);
		invalidate();
	}
	
	
	private SensorManager mSensorManager;
	
	private void init(){
		// 获取真机的传感器管理服务 
		mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
		
	}
	
	/**
	 * 在Activity onResume()时调用
	 */
	public void resume(){
		// 为系统的方向传感器注册监听器  
        mSensorManager.registerListener(this,
        		mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),  
                SensorManager.SENSOR_DELAY_UI); 
	}
	
	/**
	 * 在Activity onPause()时调用
	 */
	public void pause(){ 
		// 取消注册  
        mSensorManager.unregisterListener(this);  
        if(mDerection != DERECTION.STOP) moveObort();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onAccuracyChanged : " + accuracy);
	}
	
	/**
	 * 感应到的最小倾斜度
	 */
    private int MIN_SENSOR_DRGREE = 5;//
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//Log.d(TAG, "---onSensorChanged---");
		
		float[] values = event.values;  
        // 真机上获取触发的传感器类型  
        int sensorType = event.sensor.getType();
        switch (sensorType) {  
        case Sensor.TYPE_ORIENTATION:
        	//Log.v(TAG, "x = " + values[0] + " y = " + values[1] + " z = " + values[2]);
        	// 获取与Z轴的夹角  
            //float zAngle = values[0];
            // 获取与X轴的夹角  
            //float xAngle = values[1];
            // 获取与Y轴的夹角  
            float yAngle = values[2];
            float absYAngle = Math.abs(yAngle);
            if(absYAngle < MIN_SENSOR_DRGREE){
            	if(mDerection != DERECTION.STOP) moveObort();
            	return;
            }
            
            moveStepDis = (int)((absYAngle - MIN_SENSOR_DRGREE)*leanFactor);
            
            if(yAngle > 0){//右边高
            	if(mDerection != DERECTION.RIGHT)moveToRight();
            }else{//左边高
            	if(mDerection != DERECTION.LEFT)moveToLeft();
            }
            // 通知系统重绘View  
           //invalidate();
            break;  
        }
		
	}
	
	public enum DERECTION{STOP, LEFT, RIGHT};
	private DERECTION mDerection = DERECTION.STOP;
	
	/**
	 * 倾斜系数调整：倾斜角和moveStepDis的倍数关系
	 */
	private float leanFactor = 0.5f;//
	/**
	 * 每步移动的距离
	 */
	private int moveStepDis = 10;//
	/**
	 * 每步移动的时间间隔
	 */
	private int moveStepDuration = 30;//
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//Log.i(TAG, "scrollX = " + getScrollX());
			switch (msg.what) {
			case 0://---LEFT---
				if(getScrollX() > (leftBorder + moveStepDis)) {
					scrollBy(-moveStepDis, 0);
					if(mDerection == DERECTION.LEFT) mHandler.sendEmptyMessageDelayed(0, moveStepDuration);
				}else{
					scrollTo(leftBorder, 0);
					//moveObort();
					
				}
				break;

			case 1://---RIGHT---
				if(getScrollX() < (rightBorder - moveStepDis)) {
					scrollBy(moveStepDis, 0);
					if(mDerection == DERECTION.RIGHT) mHandler.sendEmptyMessageDelayed(1, moveStepDuration);
				}else{
					scrollTo(rightBorder, 0);
					//moveObort();
				}
				break;
			}
		};
	};
	
	//private boolean isMove;
	private void moveToLeft(){
		Log.d(TAG, "--moveToLeft--");
		//isMove = true;
		mDerection = DERECTION.LEFT;
		mHandler.sendEmptyMessageDelayed(0, moveStepDuration);
	}
	
	
	private void moveToRight(){
		Log.d(TAG, "--moveToRight--");
		//isMove = true;
		mDerection = DERECTION.RIGHT;
        mHandler.sendEmptyMessageDelayed(1, moveStepDuration);
	}
	
	private void moveObort(){
		Log.d(TAG, "===moveObort===");
		//isMove = false;
		mDerection = DERECTION.STOP;
		mHandler.removeMessages(0);
		mHandler.removeMessages(1);
	}
	
	/*public boolean isMove() {
		return isMove;
	}*/
	
	
	/**
	 * 调整移动速度
	 * @param leanFactor 倾斜系数调整：倾斜角和moveStepDis的倍数关系

	 */
	public void setLeanFactor(float leanFactor) {
		this.leanFactor = leanFactor;
	}
	
	/**
	 * 调整移动平滑度
	 * @param moveStepDuration
	 */
	public void setMoveStepDuration(int moveStepDuration) {
		this.moveStepDuration = moveStepDuration;
	}
	
	public void setMIN_SENSOR_DRGREE(int mIN_SENSOR_DRGREE) {
		MIN_SENSOR_DRGREE = mIN_SENSOR_DRGREE;
	}
}
