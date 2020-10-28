package com.mixiaoxiao.fastscroll.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mixiaoxiao.fastscroll.FastScrollDelegate;
import com.mixiaoxiao.fastscroll.FastScrollDelegate.IndicatorPopup;
import com.mixiaoxiao.fastscroll.FastScrollDelegate.OnFastScrollListener;
import com.mixiaoxiao.fastscroll.FastScrollGridView;
import com.mixiaoxiao.fastscroll.FastScrollListView;
import com.mixiaoxiao.fastscroll.FastScrollRecyclerView;
import com.mixiaoxiao.fastscroll.FastScrollScrollView;
import com.mixiaoxiao.fastscroll.FastScrollWebView;
import com.mixiaoxiao.recyclerview.decoration.LinearDividerItemDecoration;
import com.mixiaoxiao.recyclerview.quickadapter.BaseQuickAdapter;
import com.mixiaoxiao.recyclerview.quickadapter.QuickAdapterInterface.SectionQuickAdapterCallback;
import com.mixiaoxiao.recyclerview.quickadapter.QuickViewHolder;
import com.mixiaoxiao.recyclerview.quickadapter.SectionQuickAdapter;

public class MainActivity extends Activity {

	FastScrollRecyclerView recyclerView;
	FastScrollWebView webView;
	FastScrollListView listView;
	FastScrollScrollView scrollView;
	FastScrollGridView gridView;
	View currentVisibleView;
	
	OnFastScrollListener percentFastScrollListener = new OnFastScrollListener() {
		@Override
		public void onFastScrolled(View view, FastScrollDelegate delegate, int touchDeltaY, int viewScrollDeltaY,
				float scrollPercent) {
			int percent = (int) (scrollPercent * 100);
			delegate.setIndicatorText(percent + "%" ); 
			Log.d("OnFastScrollListener", "onFastScrolled touchDeltaY->" + touchDeltaY + " viewScrollDeltaY->" + viewScrollDeltaY + " scrollPercent->" + scrollPercent);
		}
		
		@Override
		public void onFastScrollStart(View view, FastScrollDelegate delegate) {
			toast(view.getClass().getSimpleName() +  " onFastScrollStart");
			Log.d("OnFastScrollListener", "onFastScrollStart");
		}
		
		@Override
		public void onFastScrollEnd(View view, FastScrollDelegate delegate) {
			toast(view.getClass().getSimpleName() +   " onFastScrollEnd");
			Log.d("OnFastScrollListener", "onFastScrollEnd");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/** Customisation Sample
		Drawable yourThumbDrawable = ...;
		FastScrollRecyclerView yourFastScrollRecyclerView =...; 
		boolean isDynamicHeight = true | false;
		//In the constructed function of FastScrollRecyclerView(see the source code)
		mDelegate = new FastScrollDelegate.Builder(yourFastScrollRecyclerView)
			.width(20)//the width of thumb, in dp
			.height(32)//the height of thumb, in dp
			.thumbNormalColor(0x80808080)//normal color of default thumbDrawable
			.thumbPressedColor(0xff03a9f4)//pressed color of default thumbDrawable
			.thumbDrawable(yourThumbDrawable)//the thumbDrawable(with state_pressed)
			.dynamicHeight(isDynamicHeight)//if true, the thumbHeight is computed by visisle-percent of view and min-height is thumbHeight you set.
			.build(yourFastScrollRecyclerView);
		//Or change the thumb style using apis
		yourFastScrollRecyclerView.getDelegate().setThumbDrawable(yourThumbDrawable);//set the thumbDrawable(with state_pressed)
		yourFastScrollRecyclerView.getDelegate().setThumbSize(20, 32);//set the thumb size, in dp
		yourFastScrollRecyclerView.getDelegate().setThumbDynamicHeight(isDynamicHeight);
		*/
		/** Customisation Sample
		FastScrollDelegate delegate = yourFastScrollRecyclerView.getDelegate();
		delegate.initIndicatorPopup(new IndicatorPopup.Builder(delegate)
			.indicatorPopupColor(0xff03a9f4)//popup bubble color
			.indicatorPopupSize(72)//popup bubble size
			.indicatorTextSize(36)//text size, in dp
			.indicatorMarginRight(24)//margin right, in dp
			.indicatorPopupAnimationStyle(android.R.style.Animation_Toast)//animation style (parent is android:Animation, you should change windowEnter/ExitAnimation) 
			.build()
		);
		**/
		
		//RecyclerView, Default Thumb, with IndicatorPopup
		recyclerView = (FastScrollRecyclerView) findViewById(R.id.fastScrollRecyclerView1);
		recyclerView.getDelegate().initIndicatorPopup(new IndicatorPopup.Builder(recyclerView.getDelegate()).build());
		currentVisibleView = recyclerView;
		
		//WebView, Custom Thumb
		webView = (FastScrollWebView) findViewById(R.id.fastScrollWebView1);
		webView.loadUrl("https://github.com/Mixiaoxiao/SmoothCompoundButton");
		webView.getDelegate().setThumbDrawable(ContextCompat.getDrawable(this, R.drawable.webview_thumb));
		webView.getDelegate().setThumbDynamicHeight(false);
		webView.getDelegate().setThumbSize(32, 32);
		
		//ScrollView Custom Thumb, with IndicatorPopup
		scrollView = (FastScrollScrollView) findViewById(R.id.fastScrollScrollView1);
		scrollView.getDelegate().initIndicatorPopup(new IndicatorPopup.Builder(recyclerView.getDelegate()).indicatorTextSize(20).build());
		scrollView.getDelegate().setThumbDrawable(ContextCompat.getDrawable(this, R.drawable.scrollview_thumb));
		scrollView.getDelegate().setThumbDynamicHeight(false);
		scrollView.getDelegate().setThumbSize(48, 48);
		scrollView.getDelegate().setOnFastScrollListener(percentFastScrollListener);
		
		//ListView, with IndicatorPopup
		listView = (FastScrollListView) findViewById(R.id.fastScrollListView1);
		listView.getDelegate().initIndicatorPopup(new IndicatorPopup.Builder(recyclerView.getDelegate()).indicatorTextSize(20).build());
		listView.getDelegate().setOnFastScrollListener(percentFastScrollListener);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listitem_sample , Cheeses.sCheeseStrings);
		listView.setAdapter(adapter);
		
		gridView = (FastScrollGridView) findViewById(R.id.fastScrollGridView1);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.listitem_sample , Cheeses.sCheeseStrings);
		gridView.setAdapter(adapter1);
		initRecyclerView();
		RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				View targetVisibleView = null;
				switch (checkedId) {
				case R.id.button_recyclerview:
					targetVisibleView = recyclerView;
					break;
				case R.id.button_scrollview: 
					targetVisibleView = scrollView;
					break;
				case R.id.button_webview:
					targetVisibleView = webView;
					break;
				case R.id.button_listview:
					targetVisibleView = listView;
					break;
				case R.id.button_gridview:
					targetVisibleView = gridView;
					break;
				}
				if(targetVisibleView != currentVisibleView){
					currentVisibleView.setVisibility(View.GONE);
					targetVisibleView.setVisibility(View.VISIBLE);
					currentVisibleView = targetVisibleView;
				}
			}
		};
		((RadioGroup)findViewById(R.id.radiogroup)).setOnCheckedChangeListener(listener);
		
	}

	private void initRecyclerView() {
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new LinearDividerItemDecoration(this));
		HashSet<Character> set = new HashSet<Character>();
		for (String cheese : Cheeses.sCheeseStrings) {
			set.add(cheese.charAt(0));
		}
		final int sectionCount = set.size();
		SectionQuickAdapterCallback<String> callback = new SectionQuickAdapterCallback<String>() {
			@Override
			public void onBindQuickViewHolderTypeSection(SectionQuickAdapter<String> adapter, QuickViewHolder holder,
					long sectionId) {
				int id = (int) sectionId;
				char a = (char) id;
				holder.setText(0, "" + a);
			}

			@Override
			public int[] getVariableViewsTypeSection(SectionQuickAdapter<String> adapter, QuickViewHolder holder) {
				return new int[] { R.id.section_title };
			}

			@Override
			public long getItemSectionId(SectionQuickAdapter<String> adapter, int dataPosition) {
				return adapter.getItemData(dataPosition).charAt(0);
			}

			@Override
			public int getItemSectionCount(SectionQuickAdapter<String> adapter) {
				return sectionCount;
			}

			@Override
			public int[] getVariableViewsTypeData(BaseQuickAdapter<String> adapter, QuickViewHolder holder) {
				return new int[] { R.id.item_sample_title };
			}

			@Override
			public void onBindQuickViewHolderTypeData(BaseQuickAdapter<String> adapter, QuickViewHolder holder,
					String itemData, int dataPosition) {
				holder.setText(0, itemData);
			}

			@Override
			public void onItemClick(BaseQuickAdapter<String> adapter, QuickViewHolder holder, int dataPosition,
					String itemData) {
				toast("onItemClick dataPosition->" + dataPosition + "\nitemData->" + itemData);
			}

			@Override
			public void onSectionItemClick(SectionQuickAdapter<String> adapter, QuickViewHolder holder,
					long itemSectionId) {
				int id = (int) itemSectionId;
				char a = (char) id;
				toast("onSectionItemClick itemSectionId->" + itemSectionId + "\nsectionName->" + a);

			}

		};
		final SectionQuickAdapter<String> adapter = new SectionQuickAdapter<String>(this, R.layout.recycleritem_sample,
				R.layout.recycleritem_sample_section, new ArrayList<String>(Arrays.asList(Cheeses.sCheeseStrings)),
				callback);
		recyclerView.setAdapter(adapter);
		recyclerView.getDelegate().setOnFastScrollListener(new OnFastScrollListener() {
			@Override
			public void onFastScrolled(View view, FastScrollDelegate delegate, int touchDeltaY, int viewScrollDeltaY,
					float scrollPercent) {
				View child = recyclerView.getChildAt(0);
				int adapterPosition = recyclerView.getChildAdapterPosition(child);
				long sectionId = adapter.getItemSectionId(adapter.convertItemDataPosition(adapterPosition));
				char c = (char) sectionId;
				delegate.setIndicatorText(c + "");
			}

			@Override
			public void onFastScrollStart(View view, FastScrollDelegate delegate) {
			}

			@Override
			public void onFastScrollEnd(View view, FastScrollDelegate delegate) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final int id = item.getItemId();
		if (id == R.id.action_github) {
			final String url = "https://github.com/Mixiaoxiao";
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			startActivity(Intent.createChooser(intent, url));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void toast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
