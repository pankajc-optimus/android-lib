/**
 * @author optimus
 */
package android.custommapcomponent.utilities;

import android.view.MotionEvent;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * This class handles the touch events on the map.
 * 
 * @author optimus
 * 
 */
public class NewLocationMapTouchEventsOverlay extends Overlay {
	// Members
	private int x, y;
	private long start, stop;
	private int mTapDuration, mDrawableId;
	private String mTitle, mSnippet;

	public NewLocationMapTouchEventsOverlay(int tapDuration, String title,
			String snippet, int drawableId) {
		mTapDuration = tapDuration;
		mTitle = title;
		mSnippet = snippet;
		mDrawableId = drawableId;
	}

	/**
	 * This function captures the on Touch events on the map.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent e, final MapView map) {

		// Capture the time when user started touching the map
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			start = e.getEventTime();

		}

		// Capture the time when user has stopped touching the map.
		if (e.getAction() == MotionEvent.ACTION_UP) {
			stop = e.getEventTime();
			x = (int) e.getX();
			y = (int) e.getY();
		}

		// If the time is greater than 2 seconds for touching any location on
		// map for more than 2 seconds.

		if (stop - start > mTapDuration) {

			if (map.getOverlays().size() > 2) {
				// Remove the previous pinpoint
				map.getOverlays().remove(map.getOverlays().size() - 1);
			}
			// Place a pinpoint
			Utilities.placeAPinPointUsingCustomItemizedOverlay(map
					.getProjection().fromPixels(x, y).getLatitudeE6(), map
					.getProjection().fromPixels(x, y).getLongitudeE6(),
					mDrawableId, mTitle, mSnippet, null,
					(TapControlledMapWithRefreshSupportOnScrollingAndZoom) map,
					map.getContext());

			return true;
		}
		return false;
	}
}
