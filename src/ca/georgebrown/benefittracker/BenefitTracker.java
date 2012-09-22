package ca.georgebrown.benefittracker;



import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class BenefitTracker extends Activity implements OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bf);
        
        ArrayList<View> views = new ArrayList<View>();
        views.add(findViewById(R.id.btn_dental));
        views.add(findViewById(R.id.btn_vision));
        views.add(findViewById(R.id.btn_rx));
        views.add(findViewById(R.id.btn_other));
        views.add(findViewById(R.id.btn_professional));
        
        for(int i=0;i<views.size();i++){
        	View v = views.get(i);
        	v.setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bf, menu);
        menu.findItem(R.id.menu_settings).setIntent(
        		new Intent(this, Settings.class));
        menu.findItem(R.id.about_settings).setIntent(
        		new Intent(this, About.class));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected(item); 
    	startActivity(item.getIntent());
    	return true;
    }

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dental:
			Intent i = new Intent(this, Dental.class); startActivity(i);
		break;
		case R.id.btn_vision:
			i = new Intent(this, Vision.class); startActivity(i);
		break;
		case R.id.btn_rx:
			i = new Intent(this, Prescriptions.class); startActivity(i);
		break;
		case R.id.btn_professional:
			i = new Intent(this, Professional.class); startActivity(i);
		break;
		case R.id.btn_other:
			i = new Intent(this, Other.class); startActivity(i);
		break;
		}
		
	}
    
}
