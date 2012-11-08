package ca.arae.benefittracker;

import java.text.NumberFormat;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Benefit extends Activity implements OnClickListener, ServiceResultReceiver.Receiver{

	private ServiceResultReceiver mReceiver;
	private JSONObject[] mBenefits;
	private JSONObject[] mTransactions;
	private int copay;
	private int benefitId;
	private double amountDb;
	private TextView amount;
	private NumberFormat formatter; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new ServiceResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Bundle extras=getIntent().getExtras();
        String extra = extras.getString("benefit");
        startBTFetchService(extra);
        setContentView(R.layout.activity_benefit);
        findViewById(R.id.btn_add_transaction).setOnClickListener(this);
        findViewById(R.id.btn_find_provider).setOnClickListener(this);
    }


	public void onClick(View v) {
		if(v.getId() == R.id.btn_find_provider){
			startActivity(new Intent(this, MapsActivity.class));
		}else{
			TextView title = (TextView) findViewById(R.id.benefit_title);
			String benefit = (String) title.getText();
			//pass benefit name, benefitId & copay
			Intent i = new Intent(this, AddTransaction.class); 
			i.putExtra("benefit", benefit);
			i.putExtra("copay", copay);
			i.putExtra("id", benefitId);
			startActivity(i);
		}
	}
	
	private void startBTFetchService(String benefitId) {
		final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null,getApplicationContext(), BTFetchService.class);
		serviceIntent.putExtra(BTFetchService.RECEIVER_KEY, mReceiver);
		serviceIntent.putExtra(BTFetchService.COMMAND_KEY,BTFetchService.PERFORM_SERVICE_ACTIVITY);
		serviceIntent.putExtra("bfilter", "?$filter=(id%20eq%20" + benefitId + ")");
		serviceIntent.putExtra("tfilter", "?$filter=(BenefitId%20eq%20" + benefitId + ")");
		startService(serviceIntent);
	}
	
	public void onReceiveResult(int resultCode, Bundle resultBundle) {
		switch (resultCode) {
		case BTFetchService.STATUS_RUNNING:
			break;
		case BTFetchService.STATUS_SUCCESS:
			boolean wasSuccess = resultBundle.getBoolean(BTFetchService.SERVICE_WAS_SUCCESS_KEY);
			if (wasSuccess) {
				if( resultBundle.getSerializable("benefits") != null){
					mBenefits = (JSONObject[]) resultBundle.getSerializable("benefits");
					amount = (TextView) findViewById(R.id.amount_remaining);
					//check for null
					if(mBenefits != null){
						try {
							benefitId = (Integer) mBenefits[0].get("id");
							copay = (Integer) mBenefits[0].get("Copay");							
							amountDb = (Integer) mBenefits[0].get("Allotted");
							formatter = NumberFormat.getCurrencyInstance();
							String name = (String) mBenefits[0].get("Name");
							TextView title = (TextView) findViewById(R.id.benefit_title);
					        title.setText(name);
					                
						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),"JSON error - allotted",Toast.LENGTH_LONG).show();
						}
					}else{
						Toast.makeText(getApplicationContext(),"Null",Toast.LENGTH_LONG).show();
					}
				}
				if( resultBundle.getSerializable("transactions") != null){
					mTransactions = (JSONObject[]) resultBundle.getSerializable("transactions");
					TableLayout table = (TableLayout)findViewById(R.id.table);
					//check for null
					if(mTransactions != null){
						try {
							for (int i = 0; i < mTransactions.length; i++) {
								TableRow tr = new TableRow(this);
								tr.setLayoutParams(new LayoutParams( LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
								TextView textview = new TextView(this);
								textview.setText(mTransactions[i].getString("InputDate") + " - $" + mTransactions[i].getDouble("Cost") + " ");
								tr.addView(textview);
								table.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
								amountDb -=  mTransactions[i].getDouble("Cost");
							}
						}catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),"JSON error - transactions",Toast.LENGTH_LONG).show();
						}
					}
				}
				if(amountDb <= 0){
					amount.setText("$0.00");
				}else{
					amount.setText(formatter.format(amountDb));
				}
			} else {
				Toast.makeText(getApplicationContext(),"There was an error fetching the URL data.  Please try again later.",Toast.LENGTH_LONG).show();
			}
			break;
		case BTFetchService.STATUS_FINISHED:
			break;
		case BTFetchService.STATUS_ERROR:
			Toast.makeText(getApplicationContext(),"There was an error fetching the Todo data." + "Please try again later.", Toast.LENGTH_LONG).show();
			break;
		}
	}

}
