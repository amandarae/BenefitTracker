package ca.arae.benefittracker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import ca.arae.benefittracker.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AddTransaction extends Activity {
	private TextView transAmount;
	private String amount;
	private String bId;
	private String copayDb;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        Bundle extras=getIntent().getExtras();
        String curr_benefit = extras.getString("benefit");
        bId = extras.get("id") + "";
        TextView title = (TextView) findViewById(R.id.add_benefit);
        TextView copay = (TextView) findViewById(R.id.copay_percentage);
        title.setText(curr_benefit);
        copayDb = extras.getInt("copay") +"";
        copay.setText(extras.get("copay") + "%");
        Button addTransaction = (Button)this.findViewById(R.id.btn_update_transactions);
        addTransaction.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				transAmount = (TextView) findViewById(R.id.txt_add_amount);
				saveTransaction();
			}
		});
    }
    
    protected void saveTransaction(){
    	amount = transAmount.getText().toString();
    	Pattern pattern = Pattern.compile("^\\d+[.]?\\d{0,2}+");
        Matcher matcher = pattern.matcher(amount);
    	if(matcher.matches()){
    		if(!copayDb.equals("0")){
    			float insurancePay = (100 - Integer.parseInt(copayDb));
    			insurancePay = insurancePay / 100;
    			float fAmnt = Float.parseFloat(amount) * insurancePay;
    			DecimalFormat df = new DecimalFormat("#.##");
    			amount = df.format(fAmnt);
    		}
    		new SaveTransactionAmount(this).execute(amount, bId);
    	}
    	else{
    		Toast.makeText(getApplicationContext(),"Please enter a numerical value above 1, with 0 to 2 decimal points",Toast.LENGTH_LONG).show();
    	}
		
    }

    private class SaveTransactionAmount extends AsyncTask<String, Void, String> {
    	private Activity mContext;

    	public SaveTransactionAmount(Activity activity) {
    		mContext = activity;
    	}

		@Override
    	protected String doInBackground(String... params) {
    		float transAmount = Float.parseFloat(params[0]);
    		int bId = Integer.parseInt(params[1]);
        	//use current date to tag transaction
    		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        	Date tdate = new Date();
        	String date = dateFormat.format(tdate);
        	
    		JSONObject jsonUrl = new JSONObject();
    		try {
    			
    			jsonUrl.put("BenefitId",bId);
    			jsonUrl.put("Cost", transAmount);
    			jsonUrl.put("InputDate", date);
    		} catch (JSONException e) {
    			Log.e("AddTransaction","Error creating JSON object: " + e.getMessage());
    		}
    		Log.i("AddTransaction", "JSON: " + jsonUrl.toString());

    		HttpURLConnection urlConnection = null;
    		try {
    			URL url = new URL(Constants.kAddTransactionUrl);
    			urlConnection = (HttpURLConnection) url.openConnection();
    			urlConnection.setDoOutput(true);
    			urlConnection.setDoInput(true);
    			urlConnection.setRequestMethod("POST");
    			urlConnection.addRequestProperty("Content-Type","application/json");
    			urlConnection.addRequestProperty("ACCEPT", "application/json");
    			urlConnection.addRequestProperty("X-ZUMO-APPLICATION",Constants.kMobileServiceAppId);

    			DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
    			wr.writeBytes(jsonUrl.toString());
    			wr.flush();
    			wr.close();

    			int response = urlConnection.getResponseCode();
    			InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
    			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    			StringBuilder stringBuilderResult = new StringBuilder();
    			String line;
    			while ((line = bufferedReader.readLine()) != null) {
    				stringBuilderResult.append(line);
    			}
    			if (response == 201)
    				return "SUCCESS";
    			return "FAIL";

    		} catch (IOException e) {
    			Log.e("AddTransaction", "IO Exeception: " + e.getMessage());
    			e.printStackTrace();
    			return "IOERROR";
    		} finally {
    			urlConnection.disconnect();
    		}
    	}

    	@Override
    	protected void onPostExecute(String status) {
    		if (status.equals("SUCCESS")) {
    			Toast.makeText(getApplicationContext(),"Transaction Successfully Added", Toast.LENGTH_SHORT).show();
    			mContext.finishActivity(1);
    			Intent i = new Intent(getApplicationContext(),Benefit.class);
    			i.putExtra("benefit",bId);
    			startActivity(i);
    		} else {
    			Toast.makeText(getApplicationContext(),"There was an error saving the transaction: " + status,Toast.LENGTH_SHORT).show();
    		}
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_transaction, menu);
        menu.findItem(R.id.menu_help).setIntent(new Intent(this, Help.class));
        return true;
    }
}
