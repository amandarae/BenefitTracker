package ca.georgebrown.benefittracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Vision extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_vision, menu);
        return true;
    }
}
