package ca.georgebrown.benefittracker;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_transaction, menu);
        return true;
    }
}
