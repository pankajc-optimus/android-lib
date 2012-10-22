/**
 * @author optimus
 */
package android.custommapcomponent.utilities;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.readystatesoftware.maps.TapControlledMapView;

/**
 * This class is for TapControlledMapWithRefreshSupportOnScrollingAndZoom which extends
 * com.google.android.maps.MapView. It handles the Map zoom and pan change
 * events.
 * 
 */
public class TapControlledMapWithRefreshSupportOnScrollingAndZoom extends TapControlledMapView {

	/*
	 * Listener definition for OnChangeListener
	 */
	public interface OnChangeListener {
		public void onChange(MapView view, GeoPoint newCenter,
				GeoPoint oldCenter, int newZoom, int oldZoom);
	}

	// Members
	private TapControlledMapWithRefreshSupportOnScrollingAndZoom mThis;

	private long mEventsTimeout = 3000;
	private boolean mIsTouched = false;
	private GeoPoint mLastCenterPosition;
	private int mLastZoomLevel;

	private TapControlledMapWithRefreshSupportOnScrollingAndZoom.OnChangeListener mChangeListener = null;

	// Runnable definition
	private Runnable mOnChangeTask = new Runnable() {
		@Override
		public void run() {
			if (mChangeListener != null)
				mChangeListener.onChange(mThis, getMapCenter(),
						mLastCenterPosition, getZoomLevel(), mLastZoomLevel);
			mLastCenterPosition = getMapCenter();
			mLastZoomLevel = getZoomLevel();
		}
	};

	/*
	 * Constructors to initialize the MapView.
	 */
	public TapControlledMapWithRefreshSupportOnScrollingAndZoom(Context context, String apiKey) {
		super(context, apiKey);
		initialize();
	}

	public TapControlledMapWithRefreshSupportOnScrollingAndZoom(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public TapControlledMapWithRefreshSupportOnScrollingAndZoom(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	// Initialize the members
	private void initialize() {
		mThis = this;
		mLastCenterPosition = this.getMapCenter();
		mLastZoomLevel = this.getZoomLevel();
	}

	// Setter Method for OnChangeListener
	public void setOnChangeListener(TapControlledMapWithRefreshSupportOnScrollingAndZoom.OnChangeListener l) {
		mChangeListener = l;
	}

	/*
	 * Event Handler for Map touch event (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.maps.MapView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Set touch internal
		mIsTouched = (ev.getAction() != MotionEvent.ACTION_UP);

		return super.onTouchEvent(ev);
	}

	/*
	 * This method captures scroll events and uses them to pan the map.
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.maps.MapView#computeScroll()
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();

		// Check for change
		if (isSpanChange() || isZoomChange()) {
			// If computeScroll called before timer counts down we should drop
			// it and
			// start counter over again
			resetMapChangeTimer();
		}
	}

	/*
	 * This method resets the timer
	 */
	private void resetMapChangeTimer() {
		TapControlledMapWithRefreshSupportOnScrollingAndZoom.this.removeCallbacks(mOnChangeTask);
		TapControlledMapWithRefreshSupportOnScrollingAndZoom.this.postDelayed(mOnChangeTask, mEventsTimeout);
	}

	/*
	 * This method captures the map span change.
	 */
	private boolean isSpanChange() {
		return !mIsTouched && !getMapCenter().equals(mLastCenterPosition);
	}

	/*
	 * This method captures the map zoom change.
	 */
	private boolean isZoomChange() {
		return (getZoomLevel() != mLastZoomLevel);
	}

}
