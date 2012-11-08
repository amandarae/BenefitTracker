package ca.arae.benefittracker;

import ca.arae.benefittracker.R;

import com.google.android.maps.MapActivity;
import android.os.Bundle;

public class MapsActivity extends MapActivity {

	 @Override
	    public void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_provider);
	    }
	 
	    @Override
	    protected boolean isRouteDisplayed() {
	        return false;
	    }
}
