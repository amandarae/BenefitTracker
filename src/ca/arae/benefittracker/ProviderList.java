package ca.arae.benefittracker;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ProviderList extends Activity {

   Boolean isInternetPresent = false;
   HasActiveConnection cd;
   GooglePlaces googlePlaces;
   MapsPlaceList nearPlaces;
   GetLocation gps;
   Button btnShowOnMap;
   ProgressDialog pDialog;
   ListView lv;
   ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
   String benefit;

   public static String KEY_REFERENCE = "reference"; 
   public static String KEY_NAME = "name"; 
   public static String KEY_VICINITY = "vicinity";

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
    
       Intent i = getIntent();
       benefit = i.getStringExtra("benefit");
       setContentView(R.layout.activity_provider_list);

       cd = new HasActiveConnection(getApplicationContext());

       isInternetPresent = cd.isConnectingToInternet();
       if (!isInternetPresent) {
    	   Toast.makeText(ProviderList.this,"Please connect to working Internet connection",Toast.LENGTH_LONG).show();
           return;
       }

       gps = new GetLocation(this);

       if (gps.canGetLocation()) {
           Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
       } else {
    	   Toast.makeText(ProviderList.this, "Couldn't get location information. Please enable GPS",Toast.LENGTH_LONG).show();
           return;
       }

       lv = (ListView) findViewById(R.id.list);
       btnShowOnMap = (Button) findViewById(R.id.btn_show_map);

       new LoadPlaces().execute();
       btnShowOnMap.setOnClickListener(new View.OnClickListener() {

           public void onClick(View arg0) {
               Intent i = new Intent(getApplicationContext(),
                       MapsActivity.class);
               
               i.putExtra("title", benefit);
               i.putExtra("user_latitude", Double.toString(gps.getLatitude()));
               i.putExtra("user_longitude", Double.toString(gps.getLongitude()));
               i.putExtra("near_places", nearPlaces);
               startActivity(i);
           }
       });

       lv.setOnItemClickListener(new OnItemClickListener() {

           public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

               String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
               Intent in = new Intent(getApplicationContext(),MapsPlaceDetail.class);
               in.putExtra(KEY_REFERENCE, reference);
               startActivity(in);
           }
       });
   }

   class LoadPlaces extends AsyncTask<String, String, String> {

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(ProviderList.this);
           pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(false);
           pDialog.show();
       }


       protected String doInBackground(String... args) {
           googlePlaces = new GooglePlaces();
           //set types via benefit
           String types="";
           if(benefit.equals("dental"))
        	   types = "dentist";
           else if(benefit.equals("professional services"))
        	   types="physiotherapist";
           else
        	   types = "doctor|pharmacy";
           
           try {
               double radius = 1000; 
               nearPlaces = googlePlaces.search(gps.getLatitude(),gps.getLongitude(), radius, types);
           } catch (Exception e) {
               e.printStackTrace();
           }
           return null;
       }

       protected void onPostExecute(String file_url) {

           pDialog.dismiss();
           runOnUiThread(new Runnable() {
               public void run() {
                  if (nearPlaces.results != null) {
                    String status = nearPlaces.status;
                    if(status.equals("OK")){
                           for (MapsPlace p : nearPlaces.results) {
                               HashMap<String, String> map = new HashMap<String, String>();
                               map.put(KEY_REFERENCE, p.reference);
                               map.put(KEY_NAME, p.name);
                               placesListItems.add(map);
                           }

                           ListAdapter adapter = new SimpleAdapter(ProviderList.this, placesListItems,
                                   R.layout.list_item,
                                   new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
                                           R.id.reference, R.id.name });
                           lv.setAdapter(adapter);
                       }
                   else if(status.equals("ZERO_RESULTS")){
                	   Toast.makeText(ProviderList.this,   "Sorry no places found.",Toast.LENGTH_LONG).show();
                   }
                   else if(status.equals("UNKNOWN_ERROR"))
                   {
                	   Toast.makeText(ProviderList.this, "Sorry unknown error occured.",Toast.LENGTH_LONG).show();
                   }
                   else if(status.equals("OVER_QUERY_LIMIT"))
                   {
                	   Toast.makeText(ProviderList.this,   "Sorry query limit to google places is reached",Toast.LENGTH_LONG).show();
                   }
                   else if(status.equals("REQUEST_DENIED"))
                   {
                	   Toast.makeText(ProviderList.this,    "Sorry error occured. Request is denied",Toast.LENGTH_LONG).show();
                   }
                   else if(status.equals("INVALID_REQUEST"))
                   {
                	   Toast.makeText(ProviderList.this, "Sorry error occured. Invalid Request",Toast.LENGTH_LONG).show();
                   }
                   else
                   {
                	   Toast.makeText(ProviderList.this, "Sorry error occured.",Toast.LENGTH_LONG).show();
                   }
               }
               }
           });

       }

   }

}