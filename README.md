FastScroll-Everywhere
===============

Add the fast-scroll feature to any scrollable views: RecyclerView, ScrollView, WebView, ListView, GridView, etc.

为任意可滑动的View添加快速滑动，是的，任意。

![FastScroll-Everywhere](https://raw.github.com/Mixiaoxiao/FastScroll-Everywhere/master/Screenshots/FastScroll-Everywhere.jpg) 

[GIF](https://raw.github.com/Mixiaoxiao/FastScroll-Everywhere/master/Screenshots/FastScroll-Everywhere.gif) 


Sample 
-----

[FastScroll-EverywhereSample.apk](https://raw.github.com/Mixiaoxiao/FastScroll-Everywhere/master/FastScroll-Everywhere-Sample.apk)


Usage 
-----

* Use `FastScrollRecyclerView` `FastScrollScrollView`  `FastScrollWebView`  `FastScrollListView` `FastScrollGridView`  to replace the original one.

* PopupIndicator
	```java
		FastScrollDelegate delegate = yourFastScrollRecyclerView.getDelegate();
		//To show PopupIndicator, you need to initIndicatorPopup first. There is no PopupIndicator by default, to avoid wasting memory.
		delegate.initIndicatorPopup(new IndicatorPopup.Builder(delegate));//create a default IndicatorPopup
		//Change the text in IndicatorPopup
		delegate.setIndicatorText("A"); //You had better to set only one char or use small textSize.
	```

* OnFastScrollListener
	```java
		FastScrollDelegate delegate = yourFastScrollRecyclerView.getDelegate();
		delegate.setOnFastScrollListener(new OnFastScrollListener() {
			@Override
			public void onFastScrolled(View view, FastScrollDelegate delegate, int touchDeltaY, int viewScrollDeltaY,
					float scrollPercent) {
				String indicator = "A";
				delegate.setIndicatorText(indicator);
			}

			@Override
			public void onFastScrollStart(View view, FastScrollDelegate delegate) {
			}

			@Override
			public void onFastScrollEnd(View view, FastScrollDelegate delegate) {
			}
		});
	```


Customisation
-----

* Thumb
	```java
		Drawable yourThumbDrawable = ...;
		FastScrollRecyclerView yourFastScrollRecyclerView =...; 
		boolean isDynamicHeight = true | false;
	
		//Change the constructed function of FastScrollRecyclerView(see the source code)
		mDelegate = new FastScrollDelegate.Builder(yourFastScrollRecyclerView)
			.width(20)//the width of thumb, in dp
			.height(32)//the height of thumb, in dp
			.thumbNormalColor(0x80808080)//normal color of default thumbDrawable
			.thumbPressedColor(0xff03a9f4)//pressed color of default thumbDrawable
			.thumbDrawable(yourThumbDrawable)//the thumbDrawable(with state_pressed)
			.dynamicHeight(isDynamicHeight)//if true, the thumbHeight is computed by visible-percent of view and min-height is thumbHeight you set.
			.build(yourFastScrollRecyclerView);
	
		//Or change the thumb style runtime
		yourFastScrollRecyclerView.getDelegate().setThumbDrawable(yourThumbDrawable);//set the thumbDrawable(with state_pressed)
		yourFastScrollRecyclerView.getDelegate().setThumbSize(20, 32);//set the thumb size, in dp
		yourFastScrollRecyclerView.getDelegate().setThumbDynamicHeight(isDynamicHeight);
	```

* PopupIndicator
	```java
		FastScrollDelegate delegate = yourFastScrollRecyclerView.getDelegate();
		delegate.initIndicatorPopup(new IndicatorPopup.Builder(delegate)
			.indicatorPopupColor(0xff03a9f4)//popup bubble color
			.indicatorPopupSize(72)//popup bubble size
			.indicatorTextSize(36)//text size, in dp
			.indicatorMarginRight(24)//margin right, in dp
			.indicatorPopupAnimationStyle(android.R.style.Animation_Toast)//animation style (parent is android:Animation, you should change windowEnter/ExitAnimation) 
			.build()
		);
	```	

Extension (FastScroll-Everywhere!)
-----

* If you want to add the fast-scroll feature to your `CustomScrollableView`, just copy the source code of `FastScrollRecyclerView`(or any `FastScrollXxxxView`) and change the super-class to your `CustomScrollableView`.
* Then, all done. All things are handled by `FastScrollDelegate`


Developed By
------------

Mixiaoxiao(谜小小) - <xiaochyechye@gmail.com> or <mixiaoxiaogogo@163.com>



License
-----------

    Copyright 2016 Mixiaoxiao

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
