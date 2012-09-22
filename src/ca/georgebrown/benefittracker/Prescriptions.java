package ca.georgebrown.benefittracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Prescriptions extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescriptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_prescriptions, menu);
        return true;
    }
}
