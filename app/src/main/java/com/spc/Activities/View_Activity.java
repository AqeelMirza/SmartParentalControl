package com.spc.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.spc.Adapters.RecyclerViewAdapter;
import com.spc.Models.Call_Logs;
import com.spc.Models.Messages;
import com.spc.R;
import com.spc.Utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class View_Activity extends AppCompatActivity {
    Boolean isCalllogs;
    String childph;
    String parentph;
    ArrayList<Call_Logs> call_logsArrayList;
    Call_Logs call_logs;
    ArrayList<Messages> messagesArrayList;
    Messages messages;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_layout);

        isCalllogs = getIntent().getBooleanExtra("isCall_log", true);
        childph = getIntent().getStringExtra("child");
        parentph = getIntent().getStringExtra("parent");

        recyclerView = (RecyclerView) findViewById(R.id.recView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(View_Activity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (isCalllogs) {
            Get_Call_logs(parentph, childph);
        } else {
            Get_Msgs(parentph, childph);
        }


    }


    //retriving call-logs
    void Get_Call_logs(String parent_num, String num) {

        //https://samplefcm-e1e34.firebaseio.com/users/9700884367/child_details/8801502038/call_details/Call_Logs.json
        //String url = "https://smart-mobile-tracker.firebaseio.com//users/" + parent_num + "/child_details/" + num + "/call_details/" + "Call_Logs" + ".json";
        String url = "https://smartparentalcontrol-529aa.firebaseio.com/users/" + parent_num + "/child_details/" + num + "/call_details/" + "Call_Logs" + ".json";

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
                        String childname = null;
                        if (response == null) {
                            Toast.makeText(View_Activity.this, "Response Error", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                call_logsArrayList = new ArrayList<>();
                                JSONArray jsonArray = new JSONArray(response.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    call_logs = new Call_Logs();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String phnum = jsonObject.getString("phNumber");
                                    String calltype = "-";
                                    try {
                                        calltype = jsonObject.getString("callType");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String duration = jsonObject.getString("callDuration");
                                    String calltime = jsonObject.getString("callDate");

                                    call_logs.setPhNumber(phnum);
                                    call_logs.setCallType(calltype);
                                    call_logs.setCallDate(calltime);
                                    call_logs.setCallDuration(duration);

                                    call_logsArrayList.add(call_logs);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(View_Activity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                            }
                            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(View_Activity.this, messagesArrayList, call_logsArrayList, R.layout.view_call_log_items, isCalllogs);
                            recyclerView.setAdapter(recyclerViewAdapter);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                // hide the progress dialog
                Toast.makeText(View_Activity.this, "Error or No Records found. ", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }


    //Retriving Messages
    void Get_Msgs(String parent_num, String num) {


        //https://samplefcm-e1e34.firebaseio.com/users/9700884367/child_details/8801502038/msgs_details/Messages_List
        //String url = "https://smart-mobile-tracker.firebaseio.com//users/" + parent_num + "/child_details/" + num + "/msgs_details/" + "Messages_List" + ".json";
        String url = "https://smartparentalcontrol-529aa.firebaseio.com/users/" + parent_num + "/child_details/" + num + "/msgs_details/" + "Messages_List" + ".json";

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
                        String childname = null;
                        if (response == null) {
                            Toast.makeText(View_Activity.this, "Response Error", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                messagesArrayList = new ArrayList<>();
                                JSONArray jsonArray = new JSONArray(response.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    messages = new Messages();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String phnum = jsonObject.getString("mobile_num");
                                    String body = jsonObject.getString("msg_body");


                                    messages.setMobile_num(phnum);
                                    messages.setMsg_body(body);

                                    messagesArrayList.add(messages);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(View_Activity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                            }
                            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(View_Activity.this, messagesArrayList, call_logsArrayList, R.layout.view_messges_items, isCalllogs);
                            recyclerView.setAdapter(recyclerViewAdapter);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                // hide the progress dialog
                Toast.makeText(View_Activity.this, "Error or No Records Found. ", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }


}
