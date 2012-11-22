package ca.arae.benefittracker;

import ca.arae.benefittracker.R;

import com.google.android.maps.MapActivity;
import android.os.Bundle;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
 
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
 
public class MapsActivity extends MapActivity {

    MapsPlaceList nearPlaces;
    MapView mapView;
    List<Overlay> mapOverlays;
    AddItemizedOverlay itemizedOverlay;
    GeoPoint geoPoint;
    MapController mc;
 
    double latitude;
    double longitude;
    OverlayItem overlayitem;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        
 
        Intent i = getIntent();
        String page_title = i.getStringExtra("title");
        String user_latitude = i.getStringExtra("user_latitude");
        String user_longitude = i.getStringExtra("user_longitude");
        this.setTitle(page_title);
        nearPlaces = (MapsPlaceList) i.getSerializableExtra("near_places");
        
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mapOverlays = mapView.getOverlays();
        geoPoint = new GeoPoint((int) (Double.parseDouble(user_latitude) * 1E6), (int) (Double.parseDouble(user_longitude) * 1E6));

        Drawable drawable_user = this.getResources().getDrawable(R.drawable.mark_red);
        itemizedOverlay = new AddItemizedOverlay(drawable_user, this);
        overlayitem = new OverlayItem(geoPoint, "Your Location","You");
        itemizedOverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedOverlay);
        itemizedOverlay.populateNow();
 
        Drawable drawable = this.getResources().getDrawable(R.drawable.mark_blue);
        itemizedOverlay = new AddItemizedOverlay(drawable, this);
        mc = mapView.getController();
        int minLat = Integer.MAX_VALUE;
        int minLong = Integer.MAX_VALUE;
        int maxLat = Integer.MIN_VALUE;
        int maxLong = Integer.MIN_VALUE;
 
        if (nearPlaces.results != null) {
            for (MapsPlace place : nearPlaces.results) {
                latitude = place.geometry.location.lat; 
                longitude = place.geometry.location.lng;
                geoPoint = new GeoPoint((int) (latitude * 1E6),(int) (longitude * 1E6));
                overlayitem = new OverlayItem(geoPoint, place.name,place.vicinity);
                itemizedOverlay.addOverlay(overlayitem);
                minLat  = (int) Math.min( geoPoint.getLatitudeE6(), minLat );
                minLong = (int) Math.min( geoPoint.getLongitudeE6(), minLong);
                maxLat  = (int) Math.max( geoPoint.getLatitudeE6(), maxLat );
                maxLong = (int) Math.max( geoPoint.getLongitudeE6(), maxLong );
            }
            mapOverlays.add(itemizedOverlay);
            itemizedOverlay.populateNow();
        }
        mapView.getController().zoomToSpan(Math.abs( minLat - maxLat ), Math.abs( minLong - maxLong ));
        mc.animateTo(new GeoPoint((maxLat + minLat)/2, (maxLong + minLong)/2 ));
        mapView.postInvalidate();
 
    }
 
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
 
}