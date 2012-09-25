package ca.georgebrown.benefittracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Benefit extends Activity implements OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benefit);
        Bundle extras=getIntent().getExtras();
        String extra = extras.getString("benefit");
        String curr_benefit = "benefit_" + extra;
        TextView title = (TextView) findViewById(R.id.benefit_title);
        String resource = (String) getResources().getText(getResources().getIdentifier(curr_benefit, "string", "ca.georgebrown.benefittracker"));
        title.setText(resource);
        
        findViewById(R.id.btn_add_transaction).setOnClickListener(this);
        findViewById(R.id.btn_find_provider).setOnClickListener(this);
    }


	public void onClick(View v) {
		if(v.getId() == R.id.btn_find_provider){
			startActivity(new Intent(this, MapsActivity.class));
		}else{
			TextView title = (TextView) findViewById(R.id.benefit_title);
			String benefit = (String) title.getText();
			Intent i = new Intent(this, AddTransaction.class); 
			i.putExtra("benefit", benefit);
			startActivity(i);
		}
	}
}
