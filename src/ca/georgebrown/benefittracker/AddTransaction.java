package ca.georgebrown.benefittracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AddTransaction extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        Bundle extras=getIntent().getExtras();
        String curr_benefit = extras.getString("benefit");
        TextView title = (TextView) findViewById(R.id.add_benefit);
        title.setText(curr_benefit);
        Button addTransaction = (Button)this.findViewById(R.id.btn_update_transactions);
        addTransaction.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// kill activity for now
				finish();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_transaction, menu);
        menu.findItem(R.id.menu_help).setIntent(
        		new Intent(this, Help.class));
        return true;
    }
}
