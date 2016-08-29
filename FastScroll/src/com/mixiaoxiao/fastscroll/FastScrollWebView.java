package com.mixiaoxiao.fastscroll;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import com.mixiaoxiao.fastscroll.FastScrollDelegate.FastScrollable;


/**
 * https://github.com/Mixiaoxiao/FastScroll-Everywhere
 * FastScrollWebView
 * @author Mixiaoxiao
 * 2016-08-28
 */
public class FastScrollWebView extends WebView implements FastScrollable {

	private FastScrollDelegate mDelegate;

	public FastScrollWebView(Context context) {
		super(context);
		init(context);
	}

	public FastScrollWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FastScrollWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public FastScrollWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	private void init(Context context) {
		mDelegate = new FastScrollDelegate.Builder(this).build(this);
	}

	// //////////
	// Delegate//
	// //////////

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mDelegate.onInterceptTouchEvent(ev)) {
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mDelegate.onTouchEvent(event)) {
			// // Handle touchEvent by Delegate
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected boolean awakenScrollBars() {
		// Do NOT call super.awakenScrollBars()
		return mDelegate.awakenScrollBars();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		mDelegate.dispatchDraw(canvas);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mDelegate.onAttachedToWindow();
	}

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		if (mDelegate != null) {
			mDelegate.onVisibilityChanged(changedView, visibility);
		}

	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		mDelegate.onWindowVisibilityChanged(visibility);
	}

	// //////////////////////////
	// ViewInternalSuperMethods//
	// //////////////////////////

	@Override
	public int superComputeVerticalScrollExtent() {
		return super.computeVerticalScrollExtent();
	}

	@Override
	public int superComputeVerticalScrollOffset() {
		return super.computeVerticalScrollOffset();
	}

	@Override
	public int superComputeVerticalScrollRange() {
		return super.computeVerticalScrollRange();
	}

	@Override
	public void superOnTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
	}

	@Override
	public FastScrollDelegate getDelegate() {
		return mDelegate;
	}

	@Override
	public void setNewFastScrollDelegate(FastScrollDelegate newDelegate) {
		if (newDelegate == null) {
			throw new IllegalArgumentException("setNewFastScrollDelegate must NOT be NULL.");
		}
		mDelegate.onDetachedFromWindow();
		mDelegate = newDelegate;
		newDelegate.onAttachedToWindow();
	}

}
