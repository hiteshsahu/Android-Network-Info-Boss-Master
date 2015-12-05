/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.serveroverload.networkmaster.ui;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.serveroverload.networkmaster.R;

// TODO: Auto-generated Javadoc
/**
 * The Class PagerSlidingTabStrip.
 */
public class PagerSlidingTabStrip extends HorizontalScrollView {

	/**
	 * The Interface IconTabProvider.
	 */
	public interface IconTabProvider {

		/**
		 * Gets the page icon res id.
		 *
		 * @param position
		 *            the position
		 * @return the page icon res id
		 */
		public int getPageIconResId(int position);
	}

	/** The Constant ATTRS. */
	// @formatter:off
	private static final int[] ATTRS = new int[] { android.R.attr.textSize,
			android.R.attr.textColor };
	// @formatter:on

	/** The default tab layout params. */
	private LinearLayout.LayoutParams defaultTabLayoutParams;

	/** The expanded tab layout params. */
	private LinearLayout.LayoutParams expandedTabLayoutParams;

	/** The page listener. */
	private final PageListener pageListener = new PageListener();

	/** The delegate page listener. */
	public OnPageChangeListener delegatePageListener;

	/** The tabs container. */
	private LinearLayout tabsContainer;

	/** The pager. */
	private ViewPager pager;

	/** The tab count. */
	private int tabCount;

	/** The current position. */
	private int currentPosition = 0;

	/** The current position offset. */
	private float currentPositionOffset = 0f;

	/** The rect paint. */
	private Paint rectPaint;

	/** The divider paint. */
	private Paint dividerPaint;

	/** The indicator color. */
	private int indicatorColor = 0xFF666666;

	/** The underline color. */
	private int underlineColor = 0x1A000000;

	/** The divider color. */
	private int dividerColor = 0x1A000000;

	/** The should expand. */
	private boolean shouldExpand = false;

	/** The text all caps. */
	private boolean textAllCaps = true;

	/** The scroll offset. */
	private int scrollOffset = 52;

	/** The indicator height. */
	private int indicatorHeight = 8;

	/** The underline height. */
	private int underlineHeight = 2;

	/** The divider padding. */
	private int dividerPadding = 12;

	/** The tab padding. */
	private int tabPadding = 24;

	/** The divider width. */
	private int dividerWidth = 1;

	/** The tab text size. */
	private int tabTextSize = 12;

	/** The tab text color. */
	private int tabTextColor = 0xFF666666;

	/** The tab typeface. */
	private Typeface tabTypeface = null;

	/** The tab typeface style. */
	private int tabTypefaceStyle = Typeface.BOLD;

	/** The last scroll x. */
	private int lastScrollX = 0;

	/** The tab background res id. */
	private int tabBackgroundResId = R.drawable.background_tabs;

	/** The locale. */
	private Locale locale;

	/**
	 * Instantiates a new pager sliding tab strip.
	 *
	 * @param context
	 *            the context
	 */
	public PagerSlidingTabStrip(Context context) {
		this(context, null);
	}

	/**
	 * Instantiates a new pager sliding tab strip.
	 *
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * Instantiates a new pager sliding tab strip.
	 *
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 * @param defStyle
	 *            the def style
	 */
	public PagerSlidingTabStrip(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

		setFillViewport(true);
		setWillNotDraw(false);

		tabsContainer = new LinearLayout(context);
		tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		tabsContainer.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(tabsContainer);

		DisplayMetrics dm = getResources().getDisplayMetrics();

		scrollOffset = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
		indicatorHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
		underlineHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
		dividerPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
		tabPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
		dividerWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
		tabTextSize = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

		// get system attrs (android:textSize and android:textColor)

		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

		tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
		tabTextColor = a.getColor(1, tabTextColor);

		a.recycle();

		// get custom attrs

		a = context.obtainStyledAttributes(attrs,
				R.styleable.PagerSlidingTabStrip);

		indicatorColor = a.getColor(
				R.styleable.PagerSlidingTabStrip_pstsIndicatorColor,
				indicatorColor);
		underlineColor = a.getColor(
				R.styleable.PagerSlidingTabStrip_pstsUnderlineColor,
				underlineColor);
		dividerColor = a
				.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor,
						dividerColor);
		indicatorHeight = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight,
				indicatorHeight);
		underlineHeight = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight,
				underlineHeight);
		dividerPadding = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsDividerPadding,
				dividerPadding);
		tabPadding = a.getDimensionPixelSize(
				R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight,
				tabPadding);
		tabBackgroundResId = a.getResourceId(
				R.styleable.PagerSlidingTabStrip_pstsTabBackground,
				tabBackgroundResId);
		shouldExpand = a
				.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand,
						shouldExpand);
		scrollOffset = a
				.getDimensionPixelSize(
						R.styleable.PagerSlidingTabStrip_pstsScrollOffset,
						scrollOffset);
		textAllCaps = a.getBoolean(
				R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);

		a.recycle();

		rectPaint = new Paint();
		rectPaint.setAntiAlias(true);
		rectPaint.setStyle(Style.FILL);

		dividerPaint = new Paint();
		dividerPaint.setAntiAlias(true);
		dividerPaint.setStrokeWidth(dividerWidth);

		defaultTabLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		expandedTabLayoutParams = new LinearLayout.LayoutParams(0,
				LayoutParams.MATCH_PARENT, 1.0f);

		if (locale == null) {
			locale = getResources().getConfiguration().locale;
		}
	}

	/**
	 * Sets the view pager.
	 *
	 * @param pager
	 *            the new view pager
	 */
	public void setViewPager(ViewPager pager) {
		this.pager = pager;

		if (pager.getAdapter() == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}

		pager.setOnPageChangeListener(pageListener);

		notifyDataSetChanged();
	}

	/**
	 * Sets the on page change listener.
	 *
	 * @param listener
	 *            the new on page change listener
	 */
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.delegatePageListener = listener;
	}

	/**
	 * Notify data set changed.
	 */
	public void notifyDataSetChanged() {

		tabsContainer.removeAllViews();

		tabCount = pager.getAdapter().getCount();

		for (int i = 0; i < tabCount; i++) {

			if (pager.getAdapter() instanceof IconTabProvider) {
				addIconTab(i,
						((IconTabProvider) pager.getAdapter())
								.getPageIconResId(i));
			} else {
				addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
			}

		}

		updateTabStyles();

		getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {

						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
							getViewTreeObserver().removeGlobalOnLayoutListener(
									this);
						} else {
							getViewTreeObserver().removeOnGlobalLayoutListener(
									this);
						}

						currentPosition = pager.getCurrentItem();
						scrollToChild(currentPosition, 0);
					}
				});

	}

	/**
	 * Adds the text tab.
	 *
	 * @param position
	 *            the position
	 * @param title
	 *            the title
	 */
	private void addTextTab(final int position, String title) {

		TextView tab = new TextView(getContext());
		tab.setText(title);
		tab.setGravity(Gravity.CENTER);
		tab.setSingleLine();

		addTab(position, tab);
	}

	/**
	 * Adds the icon tab.
	 *
	 * @param position
	 *            the position
	 * @param resId
	 *            the res id
	 */
	private void addIconTab(final int position, int resId) {

		ImageButton tab = new ImageButton(getContext());
		tab.setImageResource(resId);

		addTab(position, tab);

	}

	/**
	 * Adds the tab.
	 *
	 * @param position
	 *            the position
	 * @param tab
	 *            the tab
	 */
	private void addTab(final int position, View tab) {
		tab.setFocusable(true);
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pager.setCurrentItem(position);
			}
		});

		tab.setPadding(tabPadding, 0, tabPadding, 0);
		tabsContainer
				.addView(tab, position, shouldExpand ? expandedTabLayoutParams
						: defaultTabLayoutParams);
	}

	/**
	 * Update tab styles.
	 */
	private void updateTabStyles() {

		for (int i = 0; i < tabCount; i++) {

			View v = tabsContainer.getChildAt(i);

			v.setBackgroundResource(tabBackgroundResId);

			if (v instanceof TextView) {

				TextView tab = (TextView) v;
				tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
				tab.setTypeface(tabTypeface, tabTypefaceStyle);
				tab.setTextColor(tabTextColor);

				// setAllCaps() is only available from API 14, so the upper case
				// is made manually if we are on a
				// pre-ICS-build
				if (textAllCaps) {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
						tab.setAllCaps(true);
					} else {
						tab.setText(tab.getText().toString()
								.toUpperCase(locale));
					}
				}
			}
		}

	}

	/**
	 * Scroll to child.
	 *
	 * @param position
	 *            the position
	 * @param offset
	 *            the offset
	 */
	private void scrollToChild(int position, int offset) {

		if (tabCount == 0) {
			return;
		}

		int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

		if (position > 0 || offset > 0) {
			newScrollX -= scrollOffset;
		}

		if (newScrollX != lastScrollX) {
			lastScrollX = newScrollX;
			scrollTo(newScrollX, 0);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (isInEditMode() || tabCount == 0) {
			return;
		}

		final int height = getHeight();

		// draw indicator line

		rectPaint.setColor(indicatorColor);

		// default: line below current tab
		View currentTab = tabsContainer.getChildAt(currentPosition);
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();

		// if there is an offset, start interpolating left and right coordinates
		// between current and next tab
		if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

			View nextTab = tabsContainer.getChildAt(currentPosition + 1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();

			lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
					* lineLeft);
			lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
					* lineRight);
		}

		canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height,
				rectPaint);

		// draw underline

		rectPaint.setColor(underlineColor);
		canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(),
				height, rectPaint);

		// draw divider

		dividerPaint.setColor(dividerColor);
		for (int i = 0; i < tabCount - 1; i++) {
			View tab = tabsContainer.getChildAt(i);
			canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(),
					height - dividerPadding, dividerPaint);
		}
	}

	/**
	 * The listener interface for receiving page events. The class that is
	 * interested in processing a page event implements this interface, and the
	 * object created with that class is registered with a component using the
	 * component's <code>addPageListener<code> method. When
	 * the page event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see PageEvent
	 */
	private class PageListener implements OnPageChangeListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
		 * (int, float, int)
		 */
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

			currentPosition = position;
			currentPositionOffset = positionOffset;

			if (null != tabsContainer.getChildAt(position))
				scrollToChild(position, (int) (positionOffset * tabsContainer
						.getChildAt(position).getWidth()));

			invalidate();

			if (delegatePageListener != null) {
				delegatePageListener.onPageScrolled(position, positionOffset,
						positionOffsetPixels);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
		 * onPageScrollStateChanged(int)
		 */
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				scrollToChild(pager.getCurrentItem(), 0);
			}

			if (delegatePageListener != null) {
				delegatePageListener.onPageScrollStateChanged(state);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
		 * (int)
		 */
		@Override
		public void onPageSelected(int position) {
			if (delegatePageListener != null) {
				delegatePageListener.onPageSelected(position);
			}
		}

	}

	/**
	 * Sets the indicator color.
	 *
	 * @param indicatorColor
	 *            the new indicator color
	 */
	public void setIndicatorColor(int indicatorColor) {
		this.indicatorColor = indicatorColor;
		invalidate();
	}

	/**
	 * Sets the indicator color resource.
	 *
	 * @param resId
	 *            the new indicator color resource
	 */
	public void setIndicatorColorResource(int resId) {
		this.indicatorColor = getResources().getColor(resId);
		invalidate();
	}

	/**
	 * Gets the indicator color.
	 *
	 * @return the indicator color
	 */
	public int getIndicatorColor() {
		return this.indicatorColor;
	}

	/**
	 * Sets the indicator height.
	 *
	 * @param indicatorLineHeightPx
	 *            the new indicator height
	 */
	public void setIndicatorHeight(int indicatorLineHeightPx) {
		this.indicatorHeight = indicatorLineHeightPx;
		invalidate();
	}

	/**
	 * Gets the indicator height.
	 *
	 * @return the indicator height
	 */
	public int getIndicatorHeight() {
		return indicatorHeight;
	}

	/**
	 * Sets the underline color.
	 *
	 * @param underlineColor
	 *            the new underline color
	 */
	public void setUnderlineColor(int underlineColor) {
		this.underlineColor = underlineColor;
		invalidate();
	}

	/**
	 * Sets the underline color resource.
	 *
	 * @param resId
	 *            the new underline color resource
	 */
	public void setUnderlineColorResource(int resId) {
		this.underlineColor = getResources().getColor(resId);
		invalidate();
	}

	/**
	 * Gets the underline color.
	 *
	 * @return the underline color
	 */
	public int getUnderlineColor() {
		return underlineColor;
	}

	/**
	 * Sets the divider color.
	 *
	 * @param dividerColor
	 *            the new divider color
	 */
	public void setDividerColor(int dividerColor) {
		this.dividerColor = dividerColor;
		invalidate();
	}

	/**
	 * Sets the divider color resource.
	 *
	 * @param resId
	 *            the new divider color resource
	 */
	public void setDividerColorResource(int resId) {
		this.dividerColor = getResources().getColor(resId);
		invalidate();
	}

	/**
	 * Gets the divider color.
	 *
	 * @return the divider color
	 */
	public int getDividerColor() {
		return dividerColor;
	}

	/**
	 * Sets the underline height.
	 *
	 * @param underlineHeightPx
	 *            the new underline height
	 */
	public void setUnderlineHeight(int underlineHeightPx) {
		this.underlineHeight = underlineHeightPx;
		invalidate();
	}

	/**
	 * Gets the underline height.
	 *
	 * @return the underline height
	 */
	public int getUnderlineHeight() {
		return underlineHeight;
	}

	/**
	 * Sets the divider padding.
	 *
	 * @param dividerPaddingPx
	 *            the new divider padding
	 */
	public void setDividerPadding(int dividerPaddingPx) {
		this.dividerPadding = dividerPaddingPx;
		invalidate();
	}

	/**
	 * Gets the divider padding.
	 *
	 * @return the divider padding
	 */
	public int getDividerPadding() {
		return dividerPadding;
	}

	/**
	 * Sets the scroll offset.
	 *
	 * @param scrollOffsetPx
	 *            the new scroll offset
	 */
	public void setScrollOffset(int scrollOffsetPx) {
		this.scrollOffset = scrollOffsetPx;
		invalidate();
	}

	/**
	 * Gets the scroll offset.
	 *
	 * @return the scroll offset
	 */
	public int getScrollOffset() {
		return scrollOffset;
	}

	/**
	 * Sets the should expand.
	 *
	 * @param shouldExpand
	 *            the new should expand
	 */
	public void setShouldExpand(boolean shouldExpand) {
		this.shouldExpand = shouldExpand;
		requestLayout();
	}

	/**
	 * Gets the should expand.
	 *
	 * @return the should expand
	 */
	public boolean getShouldExpand() {
		return shouldExpand;
	}

	/**
	 * Checks if is text all caps.
	 *
	 * @return true, if is text all caps
	 */
	public boolean isTextAllCaps() {
		return textAllCaps;
	}

	/**
	 * Sets the all caps.
	 *
	 * @param textAllCaps
	 *            the new all caps
	 */
	public void setAllCaps(boolean textAllCaps) {
		this.textAllCaps = textAllCaps;
	}

	/**
	 * Sets the text size.
	 *
	 * @param textSizePx
	 *            the new text size
	 */
	public void setTextSize(int textSizePx) {
		this.tabTextSize = textSizePx;
		updateTabStyles();
	}

	/**
	 * Gets the text size.
	 *
	 * @return the text size
	 */
	public int getTextSize() {
		return tabTextSize;
	}

	/**
	 * Sets the text color.
	 *
	 * @param textColor
	 *            the new text color
	 */
	public void setTextColor(int textColor) {
		this.tabTextColor = textColor;
		updateTabStyles();
	}

	/**
	 * Sets the text color resource.
	 *
	 * @param resId
	 *            the new text color resource
	 */
	public void setTextColorResource(int resId) {
		this.tabTextColor = getResources().getColor(resId);
		updateTabStyles();
	}

	/**
	 * Gets the text color.
	 *
	 * @return the text color
	 */
	public int getTextColor() {
		return tabTextColor;
	}

	/**
	 * Sets the typeface.
	 *
	 * @param typeface
	 *            the typeface
	 * @param style
	 *            the style
	 */
	public void setTypeface(Typeface typeface, int style) {
		this.tabTypeface = typeface;
		this.tabTypefaceStyle = style;
		updateTabStyles();
	}

	/**
	 * Sets the tab background.
	 *
	 * @param resId
	 *            the new tab background
	 */
	public void setTabBackground(int resId) {
		this.tabBackgroundResId = resId;
	}

	/**
	 * Gets the tab background.
	 *
	 * @return the tab background
	 */
	public int getTabBackground() {
		return tabBackgroundResId;
	}

	/**
	 * Sets the tab padding left right.
	 *
	 * @param paddingPx
	 *            the new tab padding left right
	 */
	public void setTabPaddingLeftRight(int paddingPx) {
		this.tabPadding = paddingPx;
		updateTabStyles();
	}

	/**
	 * Gets the tab padding left right.
	 *
	 * @return the tab padding left right
	 */
	public int getTabPaddingLeftRight() {
		return tabPadding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.HorizontalScrollView#onRestoreInstanceState(android.os
	 * .Parcelable)
	 */
	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		currentPosition = savedState.currentPosition;
		requestLayout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.HorizontalScrollView#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPosition = currentPosition;
		return savedState;
	}

	/**
	 * The Class SavedState.
	 */
	static class SavedState extends BaseSavedState {

		/** The current position. */
		int currentPosition;

		/**
		 * Instantiates a new saved state.
		 *
		 * @param superState
		 *            the super state
		 */
		public SavedState(Parcelable superState) {
			super(superState);
		}

		/**
		 * Instantiates a new saved state.
		 *
		 * @param in
		 *            the in
		 */
		private SavedState(Parcel in) {
			super(in);
			currentPosition = in.readInt();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.AbsSavedState#writeToParcel(android.os.Parcel, int)
		 */
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPosition);
		}

		/** The Constant CREATOR. */
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

}
