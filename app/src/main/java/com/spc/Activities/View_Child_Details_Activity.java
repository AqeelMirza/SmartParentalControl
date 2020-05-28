package com.spc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.spc.Models.Location_Model;
import com.spc.R;
import com.spc.Utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class View_Child_Details_Activity extends AppCompatActivity {

    ArrayList<Location_Model> locationArrayList;
    Location_Model location_model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_child_details);

        TextView header = (TextView) findViewById(R.id.view_details_header);
        TextView call_logs_tv = (TextView) findViewById(R.id.view_calls);
        TextView msgs_tv = (TextView) findViewById(R.id.view_msgs);
        TextView loc_tv = (TextView) findViewById(R.id.view_location);

        final String childname = getIntent().getStringExtra("childname");
        final String childph = getIntent().getStringExtra("childph");
        final String parentph = getIntent().getStringExtra("parentph");

        header.setText("View Details of " + childname);

        call_logs_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(View_Child_Details_Activity.this, View_Activity.class);
                i.putExtra("isCall_log", true);
                i.putExtra("child", childph);
                i.putExtra("parent", parentph);
                startActivity(i);
            }
        });

        msgs_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(View_Child_Details_Activity.this, View_Activity.class);
                i.putExtra("isCall_log", false);
                i.putExtra("child", childph);
                i.putExtra("parent", parentph);
                startActivity(i);
            }
        });

        loc_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(View_Child_Details_Activity.this, "sdfsdfsdfdsf", Toast.LENGTH_SHORT).show();
                Get_location(parentph, childph);

            }
        });

    }

    void Get_location(String parent_num, String num) {


        //https://samplefcm-e1e34.firebaseio.com/users/9700884367/child_details/8801502038/loc_details/Location_List
        //String url = "https://smart-mobile-tracker.firebaseio.com//users/" + parent_num + "/child_details/" + num + "/loc_details/" + "Location_List" + ".json";
        String url = "https://smartparentalcontrol-529aa.firebaseio.com/users/" + parent_num + "/child_details/" + num + "/loc_details/" + "Location_List" + ".json";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response", response.toString());
                        pDialog.hide();
                        String latitude = "0.0";
                        String longitude = "0.0";
                        if (response == null) {
                            Toast.makeText(View_Child_Details_Activity.this, "Response Error", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                locationArrayList = new ArrayList<>();
                                JSONArray jsonArray = new JSONArray(response.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    location_model = new Location_Model();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    latitude = jsonObject.getString("latitude");
                                    longitude = jsonObject.getString("longitude");

                                    location_model.setLatitude(latitude);
                                    location_model.setLongitude(longitude);

                                    locationArrayList.add(location_model);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(View_Child_Details_Activity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                            }
                            double lat = Double.parseDouble(latitude);
                            double lon = Double.parseDouble(longitude);
                           /* String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lon);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps");
                            startActivity(intent);*/


                            String uri = "http://maps.google.com/maps?daddr=" + lat + "," + lon + " (" + "Child at" + ")";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps");
                            startActivity(intent);

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                // hide the progress dialog
                Toast.makeText(View_Child_Details_Activity.this, "Error or No Records found. ", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }
}
