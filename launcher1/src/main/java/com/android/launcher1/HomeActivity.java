package com.android.launcher1;

import java.util.ArrayList;
import java.util.List;



import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;


public class HomeActivity extends FragmentActivity {

	private static final String TAG = "HomeActivity";
	protected static final int CHANGE_SCROLL_STATUS = 1;
	protected static final int CHANGE_SCROLL_CAN = 2;
	private static final int REFRESH_DISPLAY_ADD = 3;
	private ArrayList<PackageInfo> dataPackageInfos = null;
	private ScrollViewPager mViewPager;
	private PackageManager pm;
	private ArrayList<BaseFragment> fragments;
	private MyFragmentAdapter mFragmentAdapter;
	
	private int currentPotion = 0;
	
	private int pagerCount = 0;
	private ImageView ivDelete;
	private boolean isScrolling = false;
	private static final int ONE_PAGE_APP_SIZE  = 12;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CHANGE_SCROLL_STATUS:
				isScrolling = false;
				if (mOnViewPagerChanging != null) {
					mOnViewPagerChanging.onPagerChanging(isScrolling);
				}
				HomeFragment hFragment = (HomeFragment) fragments.get(currentPotion);
				if (hFragment != null) {
					hFragment.setonPagerChanging(isScrolling);
				}
				mViewPager.requestDisallowInterceptTouchEvent(false);
				break;
			case CHANGE_SCROLL_CAN:
				mViewPager.setCanScroll(true);
				break;
			case REFRESH_DISPLAY_ADD:
				if (cureentInstallPackage != null) {
					refreshDisplayAdd(cureentInstallPackage);
				}
				
				break;

			default:
				break;
			}
		};
	};
	private UninstallAndInstallAppReceiver mInstallReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		pm = getPackageManager();
		mViewPager = (ScrollViewPager) findViewById(R.id.vp_app_date);
		ivDelete = (ImageView) findViewById(R.id.iv_delete);
		
		pageOne = (ImageView) findViewById(R.id.iv_page_one);
		pageTwo = (ImageView) findViewById(R.id.iv_page_two);
		
		dataPackageInfos = new ArrayList<PackageInfo>();
		fragments = new ArrayList<BaseFragment>();
		StatusBarUtils.makeStatusBarTransparent(this);
	}

	private void registerInstallReceiver(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		filter.addDataScheme("package");
		mInstallReceiver = new UninstallAndInstallAppReceiver();
		registerReceiver(mInstallReceiver, filter );
	}
	private void unregisterInstallReceiver(){
		if (mInstallReceiver != null) {
			unregisterReceiver(mInstallReceiver);
			mInstallReceiver = null;
		}
	}
	
	
	private int currentPage ;
	
	private void initData() {
		dataPackageInfos.clear();
		fragments.clear();
		mViewPager.setAdapter(null);
		mViewPager.setOnPageChangeListener(null);
		getDateAppInfo();
	//	Log.i(TAG, "hdb---size:"+dataPackageInfos.size());
		if ((dataPackageInfos.size() % ONE_PAGE_APP_SIZE) == 0) {
			pagerCount = dataPackageInfos.size() / ONE_PAGE_APP_SIZE ;
		}else {
			pagerCount = (dataPackageInfos.size() / ONE_PAGE_APP_SIZE ) + 1;
		}
		for(int i = 0; i < pagerCount; i++){
			fragments.add(new HomeFragment(dataPackageInfos,i , pagerCount,this));
		}
		mFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mFragmentAdapter);
		mViewPager.setOnPageChangeListener(mPagerListener);
		initPagePoint();
		currentPage = getCurrentPageValue();
		selectPagePoint(currentPage);
		
	}
	

	private void selectPagePoint(int position) {
	//	Log.i(TAG, "hdb-----selectPagePoint--position:"+position);
		pageOne.setSelected(false);

		pageTwo.setSelected(false);

		if (position == 0) {
			pageOne.setSelected(true);
			setCurrentPageValue(0);
		}else if (position == 1) {
			pageTwo.setSelected(true);
			setCurrentPageValue(1);
		}
	}

	
	
	
	public int  getCurrentPageValue() {	
		int has_update_package = -1;
		ContentResolver contentResolver = getContentResolver();
		Uri uri = Uri.parse("content://com.action.sbmsetting/setting");
		Cursor query = contentResolver.query(uri , null, null, null, null);
		if (query != null && query.moveToFirst()) {
		    has_update_package= query.getInt(query.getColumnIndex("sp_app_current_page"));
		}else {
			Log.e(TAG, "--getCurrentPageValue--fail");
		}
		if (query != null) {
			query.close();
		}
		return has_update_package;
	}
	
	
	public void  setCurrentPageValue(int vlaue) {	
		ContentResolver contentResolver = getContentResolver();
		Uri uri = Uri.parse("content://com.action.sbmsetting/setting");
		ContentValues values = new ContentValues();
		values.put("sp_app_current_page", vlaue);
		int update = contentResolver.update(uri, values, null,null);
		if (update < 0) {
			Log.e(TAG, "--setCurrentPageValue--fail");
		}
	}
	
	
	private void initPagePoint(){
		if (pagerCount == 1) {
			pageOne.setVisibility(View.VISIBLE);
			pageTwo.setVisibility(View.GONE);
		}else if (pagerCount == 2) {
			pageOne.setVisibility(View.VISIBLE);
			pageTwo.setVisibility(View.VISIBLE);
		}

	}

	public void addPage(){
		pagerCount = pagerCount + 1;
		initPagePoint();
		fragments.add(new HomeFragment(dataPackageInfos,pagerCount-1, pagerCount,this));
		mFragmentAdapter.notifyDataSetChanged();
	}
	
	public Handler getHandler(){
		return mHandler;
	}
	public ImageView getDeleteView(){
		return ivDelete;
	}
	
	public ScrollViewPager getScrollViewPager(){
		return mViewPager;
	}
	
	private boolean isHasPerssion ;
	@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
        if (!Settings.canDrawOverlays(HomeActivity.this)){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 100);
        }else{
            initData();
            registerInstallReceiver();
            currentPage = getCurrentPageValue();
            if (currentPage == 0){
                mViewPager.setCurrentItem(0);
            }else if (currentPage == 1) {
                mViewPager.setCurrentItem(1);
            }
        }
		super.onResume();
	}

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            if (!Settings.canDrawOverlays(this)) {
                // SYSTEM_ALERT_WINDOW permission not granted...
                isHasPerssion = false;
                Toast.makeText(HomeActivity.this, "not granted", Toast.LENGTH_SHORT).show();
            }else{
                isHasPerssion = true ;
                Toast.makeText(HomeActivity.this, "11111", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
	protected void onPause() {
		super.onPause();
		unregisterInstallReceiver();
		mHandler.removeCallbacksAndMessages(null);
//		mViewPager.setAdapter(null);
//		mViewPager.setOnPageChangeListener(null);
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}
	
	private void getDateAppInfo() {
//		addAppInfoFromPackage("com.netflix.mediaclient"); //netflix
//		addAppInfoFromPackage("com.amazon.avod.thirdpartyclient");//amazon
//		addAppInfoFromPackage("com.google.android.youtube");  //youtube
//		addAppInfoFromPackage("com.starz.starzplay.android");   // starzplay
//		addAppInfoFromPackage("com.disney.disneyplus"); //disney
//		addAppInfoFromPackage("com.halfbrick.fruitninjafree"); //directv now
//		addAppInfoFromPackage("com.spotify.music"); //spotify
//		addAppInfoFromPackage("com.android.chrome");   //Chrome
//		addAppInfoFromPackage("in.startv.hotstar.lite");   //Hotstar
//		addAppInfoFromPackage("deezer.android.app");   //Deezer
//		addAppInfoFromPackage("com.apple.android.music");   //Apple Music
//		addAppInfoFromPackage("com.google.android.videos");   // Google Play Movies & TV
		int playStoreState = getPlayStoreState(getApplicationContext());
		if (playStoreState == 1) {
			addAppInfoFromPackage("com.android.vending");
		}
		List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
		for (int i = 0; i < installedPackages.size(); i++) {
			PackageInfo packageInfo = installedPackages.get(i);
			
			if (!isSystemApp(packageInfo.applicationInfo)) {
				Intent launchIntentForPackage = pm.getLaunchIntentForPackage(packageInfo.packageName);
				if (launchIntentForPackage != null) {
						dataPackageInfos.add(packageInfo);
					
				}
				
			}
		}
		
	}
	
	private void addAppInfoFromPackage(String packageName){
		try {
			PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
				dataPackageInfos.add(packageInfo);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private boolean isSystemApp(ApplicationInfo info) {
		return (info.flags & info.FLAG_SYSTEM) > 0;
	}
	
	public boolean getScrollStatus(){
		return isScrolling;
	}
	
	private ViewPager.OnPageChangeListener mPagerListener = new ViewPager.OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
//			Log.i(TAG, "hdb----onPageSelected----position:"+position);
			currentPotion = position;
			selectPagePoint(currentPotion);
		}
		
		@Override
		public void onPageScrolled(int position0, float position1, int position2) {
//			Log.i(TAG, "hdb---onPageScrolled---position0:"+position0+"  position1:"+position1+"  position2:"+position2);
			isScrolling = true;
			if (mOnViewPagerChanging != null) {
				mOnViewPagerChanging.onPagerChanging(isScrolling);
			}
			if (fragments.size() > currentPotion) {
			   HomeFragment hFragment = (HomeFragment) fragments.get(currentPotion);
			   if (hFragment != null) {
				 hFragment.setonPagerChanging(isScrolling);
			    }
			}
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
//			Log.i(TAG, "hdb----onPageScrollStateChanged----position:"+arg0);
		}
	};  

	private OnViewPagerChanging mOnViewPagerChanging;
	private ImageView pageOne;
	private ImageView pageTwo;
	public interface OnViewPagerChanging{
		void onPagerChanging(boolean changing);
	}
	public void setOnViewPagerChanging(OnViewPagerChanging mOnViewPagerChanging){
		this.mOnViewPagerChanging = mOnViewPagerChanging;
	}
	
	private String cureentInstallPackage;
	private class UninstallAndInstallAppReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent intent) {
			String dataString = intent.getDataString();
			String packageName = dataString.substring(8);
	//		Log.i(TAG, "hdb-----packageName:"+packageName);
			if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
				//do nothing
				Log.i(TAG, "hdb---ACTION_PACKAGE_REPLACED---"+intent.getPackage());
				if (packageName != null && packageName.equals(cureentInstallPackage)) {
					mHandler.removeMessages(REFRESH_DISPLAY_ADD);
				}
			}else if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
				Log.i(TAG, "hdb---ACTION_PACKAGE_ADDED---"+intent.getPackage());
				cureentInstallPackage = packageName;
				mHandler.sendEmptyMessageDelayed(REFRESH_DISPLAY_ADD, 3000);
				
			}else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
				Log.i(TAG, "hdb---ACTION_PACKAGE_REMOVED---"+intent.getPackage());
				refreshDisplayRemove(packageName);
			}
			
		}

	}
	
	
	private void refreshDisplayAdd(String packageName) {
//		Log.i(TAG, "hdb--------refreshDisplayAdd:"+packageName);
		try {
			PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
			boolean systemApp = isSystemApp(packageInfo.applicationInfo);
			if (!systemApp || "com.android.vending".equalsIgnoreCase(packageName)) {
				int size = fragments.size();
//				Log.i(TAG, "hdb--------has:"+dataPackageInfos.contains(packageInfo));
				if (packageInfo != null && getPackageNameIndex(dataPackageInfos, packageName) == -1) {
					boolean add = dataPackageInfos.add(packageInfo);
					if (dataPackageInfos.size() > ONE_PAGE_APP_SIZE && pagerCount == 1 ) {
						addPage();
					}
					for (int i = 0; i < pagerCount; i++) {
						HomeFragment hFragment = (HomeFragment) fragments.get(i);
						if (hFragment != null) {
							hFragment.refreshApp(dataPackageInfos,i);
						}
					}
					
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public int getPackageNameIndex(ArrayList<PackageInfo> list, String packageName){
		int position = -1;
		for (int i = 0; i < list.size(); i++) {
//			Log.i(TAG, "hdb------"+list.get(i).packageName+"  packageName:"+packageName);
			if(list.get(i).packageName.equals(packageName)){
				position = i;
				break;
			}
		}
		return position;
	}

	public void refreshDisplayRemove(String packageName) {
		int packageNameIndex = getPackageNameIndex(dataPackageInfos, packageName);
//		Log.i(TAG, "hdb--------refreshDisplayRemove-------packageNameIndex:"+packageNameIndex);
		if (packageNameIndex != -1) {
			dataPackageInfos.remove(packageNameIndex);
		
			if ((dataPackageInfos.size() <= ONE_PAGE_APP_SIZE) && pagerCount == 2){
				pagerCount = 1;
				fragments.remove(1);//delete pager
				
				mFragmentAdapter.notifyDataSetChanged();
				pageOne.setVisibility(View.VISIBLE);
				pageTwo.setVisibility(View.GONE);
			}
			for (int i = 0; i < pagerCount; i++) {
				HomeFragment hFragment = (HomeFragment) fragments.get(i);
				if (hFragment != null) {
					hFragment.refreshApp(dataPackageInfos,i);
				}
			}
			
		}
	}
	
	private int  getPlayStoreState(Context context) {	
		int playstoreState = -1;
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = Uri.parse("content://com.action.sbmsetting/setting");
		Cursor query = contentResolver.query(uri , null, null, null, null);
		if (query != null && query.moveToFirst()) {
			playstoreState= query.getInt(query.getColumnIndex("playstore_state"));
		}else {
			Log.e(TAG, "hdb--getPlayStoreState--fail");
		}
		if (query != null) {
			query.close();
		}
		return playstoreState;
	}
}