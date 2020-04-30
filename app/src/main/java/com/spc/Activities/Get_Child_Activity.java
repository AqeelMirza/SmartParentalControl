package com.spc.Activities;

import android.Manifest;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Browser;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spc.Models.Call_Logs;
import com.spc.Models.Child;
import com.spc.Models.Location_Model;
import com.spc.Models.Messages;
import com.spc.R;
import com.spc.Utils.AppController;
import com.spc.Utils.GPSTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Get_Child_Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "Get_Child_Activity";
    private static final int URL_LOADER = 1;
    ArrayList<Call_Logs> call_logsArrayList;
    Call_Logs call_logs;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    Child child_model;
    Button get_sms_btn;
    Button get_loc_btn;
    Button get_browHist_btn;
    GPSTracker gps;

    ArrayList<Messages> messagesArrayList;
    Messages messages;
    ArrayList<Location_Model> locationArrayList;
    Location_Model location_model;
    LinearLayout getdetails_layout, checkdetails_layout;
    EditText parent_phone_et, child_phone_et;
    Button check_btn;

    String final_parentphone, final_childphone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        setContentView(R.layout.get_child_activity);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        calllogs_initialize();

        get_sms_btn = (Button) findViewById(R.id.btn_msgs);
        get_loc_btn = (Button) findViewById(R.id.btn_location);
        get_browHist_btn = (Button) findViewById(R.id.btn_brwhistory);

        check_btn = (Button) findViewById(R.id.check_parent_btn);
        getdetails_layout = (LinearLayout) findViewById(R.id.get_child_details_layout);
        checkdetails_layout = (LinearLayout) findViewById(R.id.check_child_layout);
        parent_phone_et = (EditText) findViewById(R.id.getchild_parentphone);
        child_phone_et = (EditText) findViewById(R.id.getchild_childphone);


        getdetails_layout.setVisibility(View.GONE);
        checkdetails_layout.setVisibility(View.VISIBLE);

        check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parent_num = parent_phone_et.getText().toString().trim();
                String child_num = child_phone_et.getText().toString().trim();

                checkParent(parent_num, child_num);

            }
        });


        get_loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                2);
                        return;
                    } else {
                        get_location();
                    }
                } else {
                    get_location();

                }


            }
        });

        get_sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_SMS)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_SMS},
                                3);
                        return;
                    } else {

                        fetchInbox();
                    }
                } else {
                    fetchInbox();
                }


            }
        });
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {

        Log.d(TAG, "onCreateLoader() >> loaderID : " + loaderID);

        switch (loaderID) {
            case 1:
                // Returns a new CursorLoader
                return new CursorLoader(
                        this,   // Parent activity context
                        CallLog.Calls.CONTENT_URI,        // Table to query
                        null,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );

            default:
                return new CursorLoader(
                        this,   // Parent activity context
                        CallLog.Calls.CONTENT_URI,        // Table to query
                        null,     // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );

        }

    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor managedCursor) {
        Log.d(TAG, "onLoadFinished()");

        StringBuilder sb = new StringBuilder();

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        call_logsArrayList = new ArrayList<>();
        while (managedCursor.moveToNext()) {
            call_logs = new Call_Logs();
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;

            int callTypeCode = Integer.parseInt(callType);
            switch (callTypeCode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "Outgoing";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "Incoming";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "Missed";
                    break;
            }
            call_logs.setPhNumber(phNumber);
            call_logs.setCallDate(callDate);
            call_logs.setCallType(dir);
            call_logs.setCallDuration(callDuration);

            call_logsArrayList.add(call_logs);
        }
        Log.e(TAG, "onLoadFinished: " + call_logsArrayList.size());


        HashMap<String, ArrayList<Call_Logs>> child_calls_map = new HashMap();
        child_calls_map.put("Call_Logs", call_logsArrayList);

        //child_model = new Child(name, phone, email, child_calls_map);

        //  mFirebaseDatabase.push().child(parent_name).child("child_details").child(name).setValue(name);
        mFirebaseDatabase.child(final_parentphone).child("child_details").child(final_childphone).child("call_details").child("Call_Logs").setValue(call_logsArrayList);


        Toast.makeText(Get_Child_Activity.this, "Child Call logs add successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }

    private void calllogs_initialize() {
        Log.d(TAG, "initialize()");

        Button btnCallLog = (Button) findViewById(R.id.btn_call_log);

        btnCallLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_CALL_LOG)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG},
                                1);
                        return;
                    } else {

                        Log.d(TAG, "initialize() >> initialise loader");
                        getLoaderManager().initLoader(URL_LOADER, null, Get_Child_Activity.this);

                    }
                } else {

                    Log.d(TAG, "initialize() >> initialise loader");
                    getLoaderManager().initLoader(URL_LOADER, null, Get_Child_Activity.this);

                }
            }
        });

    }

    public void fetchInbox() {
        messagesArrayList = new ArrayList();

        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);


        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            messages = new Messages();
            String address = cursor.getString(1);
            String body = cursor.getString(3);

            System.out.println("======&gt; Mobile number =&gt; " + address);
            System.out.println("=====&gt; SMS Text =&gt; " + body);

            messages.setMobile_num(address);
            messages.setMsg_body(body);

            messagesArrayList.add(messages);
        }

        HashMap<String, ArrayList<Messages>> child_msgs_map = new HashMap();
        child_msgs_map.put("Messages_List", messagesArrayList);


        mFirebaseDatabase.child(final_parentphone).child("child_details").child(final_childphone).child("msgs_details").child("Messages_List").setValue(messagesArrayList);


        Toast.makeText(Get_Child_Activity.this, "Child Messages add successfully", Toast.LENGTH_SHORT).show();


        Log.e("SMSLenght", String.valueOf(messagesArrayList.size()));


    }

    void get_location() {

        // create class object
        gps = new GPSTracker(Get_Child_Activity.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(Get_Child_Activity.this, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            location_model = new Location_Model();

            locationArrayList = new ArrayList<Location_Model>();

            location_model.setLatitude(String.valueOf(latitude));
            location_model.setLongitude(String.valueOf(longitude));
            locationArrayList.add(location_model);

            HashMap<String, ArrayList<Location_Model>> child_loc_map = new HashMap();
            child_loc_map.put("Location_List", locationArrayList);

            mFirebaseDatabase.child(final_parentphone).child("child_details").child(final_childphone).child("loc_details").child("Location_List").setValue(locationArrayList);

            Toast.makeText(Get_Child_Activity.this, "Child Location add successfully", Toast.LENGTH_SHORT).show();

            //Stop GPS Tracking
            gps.stopUsingGPS();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }

    void checkParent(final String parent_phone, final String child_phone) {

        // https://samplefcm-e1e34.firebaseio.com/users/996633/child_details/6464
       // String url = "https://smart-mobile-tracker.firebaseio.com//users/" + parent_phone + "/child_details/" + child_phone + ".json";
        String url = "https://smartparentalcontrol-529aa.firebaseio.com/users/" + parent_phone + "/child_details/" + child_phone + ".json";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();
                        if (response == null) {
                            Toast.makeText(Get_Child_Activity.this, "Child not found.Please check entered details.", Toast.LENGTH_SHORT).show();
                            getdetails_layout.setVisibility(View.GONE);
                            checkdetails_layout.setVisibility(View.VISIBLE);
                        } else {
                            final_parentphone = parent_phone;
                            final_childphone = child_phone;
                            getdetails_layout.setVisibility(View.VISIBLE);
                            checkdetails_layout.setVisibility(View.GONE);

                            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Number_Pref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("parent_phone", parent_phone);
                            editor.putString("child_phone", child_phone);
                            editor.commit();

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                Toast.makeText(Get_Child_Activity.this, "Please check entered values", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {
                    Toast.makeText(Get_Child_Activity.this, "READ_CALL_LOG permission denied", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case 2: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {
                    Toast.makeText(Get_Child_Activity.this, "ACCESS_FINE_LOCATION permission denied", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case 3: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {
                    Toast.makeText(Get_Child_Activity.this, "READ_SMS permission denied", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'switch' lines to check for other
            // permissions this app might request
        }

    }

}
