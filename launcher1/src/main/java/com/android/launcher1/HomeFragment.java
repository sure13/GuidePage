package com.android.launcher1;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HomeFragment extends BaseFragment{

	private static final String TAG = "HomeFragment";
	private DragGridView mGridView;
	private MyAdapter mAdapter;
	private ArrayList<PackageInfo> packageInfos = null;
	private ArrayList<PackageInfo> cureentInfos = null;
	private int currentPage;
	private int totlePage;
	private HomeActivity hActivity;
	private PackageManager pm;
	private static final int AUDIOSOURCE_ANDROID = 1;

	public HomeFragment(ArrayList<PackageInfo> dataPackageInfos, int i, int pagerCount, HomeActivity homeActivity) {
		packageInfos = dataPackageInfos;
		currentPage = i;
		totlePage = pagerCount;
		hActivity = homeActivity;
	}

	@Override
	public int setResouce() {
		return R.layout.view_home;
	}
	
	@Override
	public void initView() {
		mGridView = (DragGridView) rootView.findViewById(R.id.gv_date_app);
		
	}
	
	
	@Override
	public void initdate() {
		cureentInfos = new ArrayList<PackageInfo>();
		pm = hActivity.getPackageManager();
		if (currentPage == (totlePage - 1)) {//zhe last page
			for (int i = currentPage * 12; i < packageInfos.size(); i++) {
				cureentInfos.add(packageInfos.get(i));
			}
		}else {
			for (int i = 0; i < 12; i++) {
				int index = i + 12*currentPage;
				cureentInfos.add(packageInfos.get(index));
			}
		}
	}
	
	@Override
	public void initListener() {
		mAdapter = new MyAdapter();
		mGridView.setAdapter(mAdapter);
		
		mGridView.setOnDragListener(mOnDragListener);
		
		mGridView.setOnItemClickListener(mOnItemClickListener);
		/*hActivity.setOnViewPagerChanging(new OnViewPagerChanging() {
			
			@Override
			public void onPagerChanging(boolean changing) {
				//mGridView.setViewPagerScrolling(changing);
				
				
			}
		});*/
	}
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return cureentInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHodler hodler;
			if (convertView == null) {
				hodler = new ViewHodler();
				convertView = View.inflate(hActivity, R.layout.bottom_item, null);
				hodler.ivIcon = (ImageView) convertView.findViewById(R.id.iv_item_icon);
				hodler.tvName = (TextView) convertView.findViewById(R.id.tv_item_name);
				convertView.setTag(hodler);
			}else {
				hodler = (ViewHodler) convertView.getTag();
			}
			
		//	hodler.ivIcon.setPadding(60, 15, 40,10);
			hodler.ivIcon.setPressed(false);
			hodler.ivIcon.setImageDrawable(cureentInfos.get(position).applicationInfo.loadIcon(pm));
			String title = (String) cureentInfos.get(position).applicationInfo.loadLabel(pm);
			if (title.length() > 12 && title.startsWith("Google")) {
				title = title.substring(7);
			}
			hodler.tvName.setText(title);
			
			return convertView;
		}
	}
	
	private class ViewHodler{
		ImageView ivIcon;
		TextView tvName;
	}
	
  private boolean isSystemApp;
	
	private void isSystemByPackageName(String packageName){
		try{
			PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
			isSystemApp=isSystemApp(packageInfo.applicationInfo);
		}catch(NameNotFoundException e){
		  e.printStackTrace();
		}
		
	}
	
	private boolean isSystemApp(ApplicationInfo info) {
		return (info.flags & info.FLAG_SYSTEM) > 0;
	}
	
	
	private DragGridView.OnDragListener mOnDragListener = new DragGridView.OnDragListener() {
		
		@Override
		public void onDrag(boolean isDrag, int position,boolean delete) {
			if (isDrag) {
				if (position >=0 && position < cureentInfos.size()) {

					isSystemByPackageName(cureentInfos.get(position).packageName);
					if (isSystemApp) {
						hActivity.getDeleteView().setVisibility(View.GONE);
					}else {
						hActivity.getDeleteView().setVisibility(View.VISIBLE);
					}
				}
				
				hActivity.getScrollViewPager().setCanScroll(false);
				//hActivity.getHandler().sendEmptyMessageDelayed(hActivity.CHANGE_SCROLL_CAN, 300);
			}else {
				hActivity.getDeleteView().setVisibility(View.GONE);
				
				hActivity.getScrollViewPager().setCanScroll(true);
		//		System.out.println("position: "+position);
				isSystemByPackageName(cureentInfos.get(position).packageName);
		/*		if (isSystemApp) {
					delete=false;
				}else {
					delete=true;
				}*/
				if (isSystemApp) {
					//nothing
				}else{
				if (delete) {
					
					String packageName = cureentInfos.get(position).packageName;
					if (packageName != null) {
						uninstallApp(packageName);
						deletePosition = position;
					}
				}
			}
		 }
			
		}
	};
	
	private int deletePosition = -1;
	
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			String packageName = cureentInfos.get(position).packageName;
			if ("com.rockchip.wfd".equals(packageName)) {
				startActivity(new Intent(Settings.ACTION_CAST_SETTINGS).addCategory(Intent.CATEGORY_DEFAULT));
			}else {
				startActivityformPackageName(packageName);
			}
			
//			hActivity.sendBroadcast(new Intent("com.action.intent.stopdvd"));
		}
	};
	
	
	/**
	 * open application from packageName
	 */
	private void startActivityformPackageName(String pkg) {
		Intent launchIntentForPackage = pm.getLaunchIntentForPackage(pkg);
		if (launchIntentForPackage != null) {
			sendStopMusicBrodcast();
			launchIntentForPackage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(launchIntentForPackage);
			//isSuportActionManager
			//ActionManager.setDuallinkEnable(4);
		}else {
			Toast.makeText(hActivity, "this app not install !", Toast.LENGTH_SHORT).show();
		}

	}
	
	
	private void sendStopMusicBrodcast(){
		   Intent intent = new Intent();  //鍙戦�佸仠姝㈤煶涔愮殑骞挎挱
		   intent.setAction("com.action.stopmusic");
		   hActivity.sendBroadcast(intent);
	   }
	/**
	 * uninstall application
	 * @param packageName
	 */
	private void uninstallApp(String packageName) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + packageName));
		startActivity(intent);
	}
	@Override
    public void onPause() {
//     Log.i(TAG, "hdb----onPause---removeLog:");
	   if (mGridView != null) {
			   mGridView.onStopDrag();
	   }
	   super.onPause();
   }

	public void setonPagerChanging(boolean isScrolling) {
		if (mGridView != null) {
			mGridView.setViewPagerScrolling(isScrolling);
		}
	}

	public void addApplication(PackageInfo packageInfo) {
		if (cureentInfos != null && mAdapter != null) {
//          Log.i(TAG, "hdb---addApplication");
			if (cureentInfos.size() < ONE_PAGE_APP_SIZE) {
				cureentInfos.add(packageInfo);
				mAdapter.notifyDataSetChanged();
			}else {
				hActivity.addPage();
			}
			
		}
	}

	public int removeApplication() {
		int position = -1;
		if (cureentInfos != null && mAdapter != null && (deletePosition != -1)) {
//          Log.i(TAG, "hdb---removeApplication");
			cureentInfos.remove(deletePosition);
			mAdapter.notifyDataSetChanged();
			position = deletePosition;
			deletePosition = -1;
		}
		return position;
	}

	private static final int ONE_PAGE_APP_SIZE  = 12;
	public void refreshApp(ArrayList<PackageInfo> dataPackageInfos,int pageIndex) {
//		Log.i(TAG, "hdb---refreshApp---dataPackageInfos:"+dataPackageInfos.size()+"  pageIndex:"+pageIndex);
//		Log.i(TAG, "hdb---refreshApp---rootView:"+rootView+"  mAdapter:"+mAdapter+"  mGridView:"+mGridView);
		
		if (cureentInfos != null) {
			cureentInfos.clear();
			packageInfos = dataPackageInfos;
			if (packageInfos.size() > ONE_PAGE_APP_SIZE) {
				totlePage = 2;
			}else {
				totlePage = 1;
			}
//			Log.i(TAG, "hdb---refreshApp---totlePage:"+totlePage+"  pageIndex:"+pageIndex);
			if (pageIndex == (totlePage - 1)) {//zhe last page
				for (int i = pageIndex * 12; i < packageInfos.size(); i++) {
//					Log.i(TAG, "hdb---refreshApp---i:"+i+"  pageIndex:"+pageIndex);
					cureentInfos.add(packageInfos.get(i));
				}
			}else {
				for (int i = 0; i < 12; i++) {
					int index = i + 12*pageIndex;
					cureentInfos.add(packageInfos.get(index));
				}
			}
			if (mAdapter != null) {
				mAdapter.notifyDataSetChanged();
			}
			
		}
	}
	
}
