package ca.georgebrown.benefittracker;



import java.util.ArrayList;

import android.net.Uri;
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
        views.add(findViewById(R.id.btn_dl_pdf));
        
        for(int i=0;i<views.size();i++){
        	View v = views.get(i);
        	v.setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bf, menu);
        menu.findItem(R.id.about_bf).setIntent(
        		new Intent(this, About.class));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
    	super.onOptionsItemSelected(item); 
    	startActivity(item.getIntent());
    	return true;
    }

	public void onClick(View v) {
		Intent i = new Intent();
		String str = new String();
		if(v.getId() == R.id.btn_dl_pdf){
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://greenshield.ca/sites/student/SiteAssets/Booklets/GBC_27978_83_0912_FINAL.pdf")));
		}else{
			switch (v.getId()) {
			case R.id.btn_rx:
				str = "1";
				break;
			case R.id.btn_dental:
				str = "2";
				break;
			case R.id.btn_vision:
				str = "3";
				break;
			case R.id.btn_professional:
				str = "4";
				break;
			case R.id.btn_other:
				str = "5";
				break;
			}
			i = new Intent(this, Benefit.class); 
			i.putExtra("benefit", str);
			startActivity(i);
		}
	}
}
