package com.android.launcher1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class ScrollViewPager extends ViewPager {

	private boolean isCanScrool = true;
	public ScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewPager(Context context) {
		super(context);
	}
	
	public void setCanScroll(boolean canScroll){
	//	Log.i(VIEW_LOG_TAG, "hdb--setCanScroll--canScroll:"+canScroll);
		this.isCanScrool = canScroll;
	}
	
	@Override
	public void scrollTo(int arg0, int arg1) {
		if (isCanScrool) {
			super.scrollTo(arg0, arg1);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(arg0);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(arg0);
	}
	
	

}
