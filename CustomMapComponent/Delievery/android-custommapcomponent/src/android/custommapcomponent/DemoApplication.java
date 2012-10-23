/**
 * Application covering all of the use cases for the custommapcomponent module
 * @author optimus
 */
package android.custommapcomponent;

import android.content.Context;
import android.custommapcomponent.utilities.TapControlledMapWithRefreshSupportOnScrollingAndZoom;
import android.custommapcomponent.utilities.Utilities;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.custommapcomponent.utilities.NewLocationMapTouchEventsOverlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.readystatesoftware.maps.OnSingleTapListener;

/**
 * 
 * DemoApplication contains the sample code for custommapcomponent module. The
 * purpose of this sample code is to cover all of the use cases for this
 * custommapcomponent module.In order to use this sample code please first go
 * through the library project provided in this git URL
 * https://github.com/jgilfelt/android-mapviewballoons.git. Clone the project
 * from here and import it in eclipse. You can run it to understand its usage
 * practical wise. Also please have a look on the implementation part and it
 * will consume less effort as the project is easy and simple to understand.
 * Once done this then you are all set to utilize this sample code. The code
 * here uses customItemizedOverlay object which is provided by the project
 * referenced earlier. The customItemizedOverlay is used here to draw pinpoints
 * and the CustomBallonOverlayView.java class is used to inflate our own custom
 * views on the previous view. Also the map used here is the object of
 * TapControlledMapWithRefreshSupportOnScrollingAndZoom class present in package
 * Utilities. This map has listeners which can indicate whether the map is
 * stable after the user has finished scrolling or zooming. Also there is a
 * listener which can indicate whether the map has been single tapped.
 * 
 */
public class DemoApplication extends MapActivity {
	protected static TapControlledMapWithRefreshSupportOnScrollingAndZoom tapControlledMapViewWithRefreshSupportOnScrollingAndZoom;
	static LocationManager mLocManager;
	double latitude, longitude;
	static android.custommapcomponent.utilities.NewLocationMapTouchEventsOverlay mTocuhEvents;
	protected static GeoPoint currentLocation;

	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);

		// Initialize the map
		setContentView(R.layout.main_example3);
		tapControlledMapViewWithRefreshSupportOnScrollingAndZoom = (TapControlledMapWithRefreshSupportOnScrollingAndZoom) findViewById(R.id.mapview);
		tapControlledMapViewWithRefreshSupportOnScrollingAndZoom
				.setBuiltInZoomControls(true);

		// Set listener which dismiss balloon upon single tap of MapView (iOS
		// behavior)
		tapControlledMapViewWithRefreshSupportOnScrollingAndZoom
				.setOnSingleTapListener(new OnSingleTapListener() {

					@Override
					public boolean onSingleTap(MotionEvent e) {
						Utilities.mItemizedOverlay.hideAllBalloons();
						return false;
					}
				});

		// Set listener which indicates the stability after scrolling and zoom
		// events and logs the
		// changed location
		tapControlledMapViewWithRefreshSupportOnScrollingAndZoom
				.setOnChangeListener(new TapControlledMapWithRefreshSupportOnScrollingAndZoom.OnChangeListener() {

					@Override
					public void onChange(MapView view, GeoPoint newCenter,
							GeoPoint oldCenter, int newZoom, int oldZoom) {
						Log.i("Old Center",
								"Latitude" + oldCenter.getLatitudeE6()
										+ "Longitude"
										+ oldCenter.getLongitudeE6());
						Log.i("New center",
								"Latitude" + newCenter.getLatitudeE6()
										+ "Longitude"
										+ newCenter.getLongitudeE6());
						Log.i("Old zoom", "OldZoomLevel" + oldZoom);
						Log.i("New zoom", "NewZoomLevel" + newZoom);
						// TODO Auto-generated method stub

					}
				});
		initialize();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * Does initialization of various variables and services and places a
	 * pinpoint on the user's default location
	 */
	public void initialize() {
		// Initialize the service which tracks the location returned by the GPS
		mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Get the current location
		currentLocation = Utilities.getCurrentLocationUsingGPS(mLocManager);

		// Place a custom pinpoint at the user's default location
		Utilities.placeAPinPointUsingCustomItemizedOverlay(
				currentLocation.getLatitudeE6(),
				currentLocation.getLongitudeE6(), R.drawable.icon_check,
				"This is a test", "Again this is a test", null,
				tapControlledMapViewWithRefreshSupportOnScrollingAndZoom,
				DemoApplication.this);

		// Add the touch event handlers
		mTocuhEvents = new NewLocationMapTouchEventsOverlay(1500,
				"Test to place pinpoint", "Pinpoint placed", R.drawable.flag);
		tapControlledMapViewWithRefreshSupportOnScrollingAndZoom.getOverlays()
				.add(mTocuhEvents);
	}

	/**
	 * Moves the map from current location to user's default location
	 * 
	 * @param v
	 */
	public static void animateToDefaultLocation(View v) {
		tapControlledMapViewWithRefreshSupportOnScrollingAndZoom
				.getController().animateTo(currentLocation);
	}

}
