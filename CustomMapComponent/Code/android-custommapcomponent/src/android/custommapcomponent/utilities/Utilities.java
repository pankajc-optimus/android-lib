/**
 * @author Optimus
 */
package android.custommapcomponent.utilities;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.maps.TapControlledMapView;

import android.content.Context;
import android.custommapcomponent.mapviewballoons.example.custom.CustomItemizedOverlay;
import android.custommapcomponent.mapviewballoons.example.custom.CustomOverlayItem;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * 
 * Class Utilities have methods which provides some common services
 * 
 */
public class Utilities {
	protected static String mTowers;
	public static CustomItemizedOverlay<OverlayItem> mItemizedOverlay;

	/**
	 * Gets the current location as returned by the GPS
	 * 
	 * @param mLocManager
	 *            LocationManager
	 * @return GeoPoint indicating the current location as provided by the
	 *         location manager service.
	 * @throws NullPointerException
	 */
	public static GeoPoint getCurrentLocationUsingGPS(
			LocationManager mLocManager) throws NullPointerException {

		// Set up a new criteria
		Criteria currCriteria = new Criteria();

		// Get the current location according to the GPS
		mTowers = mLocManager.getBestProvider(currCriteria, false);
		Location cLocation = mLocManager.getLastKnownLocation(mTowers);

		// Create and return the corresponding geopoint for this current
		// location
		return new GeoPoint((int) (cLocation.getLatitude() * 1E6),
				(int) (cLocation.getLongitude() * 1E6));

	}

	/**
	 * Places a pinpoint
	 * 
	 * @param latitude
	 *            Latitude of the location where pin point has to be placed
	 * @param longitude
	 *            Longitude of the location where pinpoint has to be placed
	 * @param drawableId
	 *            The id of the drawable which will be used to locate the
	 *            drawable indicating the pinpoint
	 * @param title1ForCustomOverlay
	 *            The title for the pinpoint
	 * @param title2ForCustomOverlay
	 *            The snippet for the pinpoint
	 * @param imageUrlForCustomOverlay
	 *            The image URL of an image which will be displayed in the pop
	 *            up when the pin point is tapped
	 * @param tapControlledMapView
	 *            The object of the tapControlledMapView object which will
	 *            provide the feature of dismissal of the pop up when the
	 *            background is tapped
	 * @param current
	 *            The current context
	 * @throws NullPointerException
	 * @throws NumberFormatException
	 */
	public static void placeAPinPointUsingCustomItemizedOverlay(
			double latitude, double longitude, int drawableId,
			String title1ForCustomOverlay, String title2ForCustomOverlay,
			String imageUrlForCustomOverlay,
			TapControlledMapView tapControlledMapView, Context current)
			throws NullPointerException, NumberFormatException {

		// Initialize a geopoint for location where pin point is to be
		// added.
		GeoPoint point = new GeoPoint((int) latitude, (int) longitude);

		// Set the drawable for the pinpoint
		Drawable pinpoint = current.getResources().getDrawable(drawableId);

		// Hide any previous balloon pop ups
		if (mItemizedOverlay != null) {
			mItemizedOverlay.hideAllBalloons();
		}

		// Initialize the object for pinpoint
		CustomOverlayItem mOverlayItem = new CustomOverlayItem(point,
				title1ForCustomOverlay, title2ForCustomOverlay,
				imageUrlForCustomOverlay);

		mItemizedOverlay = new CustomItemizedOverlay<OverlayItem>(pinpoint,
				tapControlledMapView);

		// Sets the offset as a distance to be kept between the drawable for
		// pinpoint and the pop up
		// opened when this pinpoint is tapped
		mItemizedOverlay.setBalloonBottomOffset(pinpoint.getIntrinsicHeight());

		// Add the current location pin point object to the map.
		mItemizedOverlay.addOverlay(mOverlayItem);
		tapControlledMapView.getOverlays().add(mItemizedOverlay);

	}
}
