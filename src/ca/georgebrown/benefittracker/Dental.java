package ca.georgebrown.benefittracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Dental extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dental);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dental, menu);
        return true;
    }
}
