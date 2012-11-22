package ca.arae.benefittracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
 
public class MapsPlaceDetail<PlaceDetails> extends Activity {

    Boolean isInternetPresent = false;
    HasActiveConnection cd;
    GooglePlaces googlePlaces;
    MapsPlaceDetails placeDetails;
    ProgressDialog pDialog;
 
    public static String KEY_REFERENCE = "reference"; // id of the place
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_place_detail);
 
        Intent i = getIntent();
 
        // Place referece id
        String reference = i.getStringExtra(KEY_REFERENCE);
 
        // Calling a Async Background thread
        new LoadSinglePlaceDetails().execute(reference);
    }
 
    class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsPlaceDetail.this);
            pDialog.setMessage("Loading profile ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        protected String doInBackground(String... args) {
            String reference = args[0];
            googlePlaces = new GooglePlaces();
            try {
                placeDetails = googlePlaces.getPlaceDetails(reference);
 
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
 
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if(placeDetails != null){
                        String status = placeDetails.status;

                        if(status.equals("OK")){
                            if (placeDetails.result != null) {
                                String name = placeDetails.result.name;
                                String address = placeDetails.result.formatted_address;
                                String phone = placeDetails.result.formatted_phone_number;
                                String latitude = Double.toString(placeDetails.result.geometry.location.lat);
                                String longitude = Double.toString(placeDetails.result.geometry.location.lng);
 
                                Log.d("Place ", name + address + phone + latitude + longitude);
 
                                TextView lbl_name = (TextView) findViewById(R.id.name);
                                TextView lbl_address = (TextView) findViewById(R.id.address);
                                TextView lbl_phone = (TextView) findViewById(R.id.phone);
                                TextView lbl_location = (TextView) findViewById(R.id.location);
 
                                //check for nulls
                                name = name == null ? "Not present" : name; 
                                address = address == null ? "Not present" : address;
                                phone = phone == null ? "Not present" : phone;
                                latitude = latitude == null ? "Not present" : latitude;
                                longitude = longitude == null ? "Not present" : longitude;
 
                                lbl_name.setText(name);
                                lbl_address.setText(address);
                                lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
                                lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
                            }
                        }
                        else if(status.equals("ZERO_RESULTS")){
                        	Toast.makeText(MapsPlaceDetail.this,"No place found",Toast.LENGTH_LONG).show();
                        }
                        else if(status.equals("OVER_QUERY_LIMIT"))
                        {
                        	Toast.makeText(MapsPlaceDetail.this,"Error: Query limit to Google Places has been reached",Toast.LENGTH_LONG).show();
                        }
                        else if(status.equals("REQUEST_DENIED"))
                        {
                        	Toast.makeText(MapsPlaceDetail.this,"Error: Request Denied",Toast.LENGTH_LONG).show();
                        }
                        else if(status.equals("INVALID_REQUEST"))
                        {
                        	Toast.makeText(MapsPlaceDetail.this,"Error: Invalid Request",Toast.LENGTH_LONG).show();
                        }
                    }else{
                    	Toast.makeText(MapsPlaceDetail.this,"Error Occured",Toast.LENGTH_LONG).show();
                    }
 
                }
            });
 
        }
 
    }
 
}
