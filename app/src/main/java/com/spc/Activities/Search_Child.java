package com.spc.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.spc.R;
import com.spc.Utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;


public class Search_Child extends AppCompatActivity {
    String parentphone;
    EditText child_phnum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_child);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search_Child.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        parentphone = getIntent().getStringExtra("parentphone");

        child_phnum = (EditText) findViewById(R.id.input_childname);


        Button search_btn = (Button) findViewById(R.id.search_child_btn);
        Button add_child = (Button) findViewById(R.id.add_child_btn);

        add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Search_Child.this, Child_Details_Activity.class);
                in.putExtra("addchild", true);
                startActivity(in);
            }
        });


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String num = child_phnum.getText().toString();
                getChildDetails(parentphone, num);

            }
        });


    }

    void getChildDetails(final String parent_num, final String num) {

//https://samplefcm-e1e34.firebaseio.com/users/Aqeel Mirza/child_details
        //String url = "https://smart-mobile-tracker.firebaseio.com//users/" + parent_num + "/child_details/" + num + ".json";
        String url = "https://smartparentalcontrol-529aa.firebaseio.com/users/" + parent_num + "/child_details/" + num + ".json";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        pDialog.hide();
                        String childname = null;
                        if (response == null) {
                            Toast.makeText(Search_Child.this, "Username does not exist,Please check your details", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                childname = jsonObject.getString("child_name");

                                Intent in = new Intent(Search_Child.this, View_Child_Details_Activity.class);
                                in.putExtra("childname", childname);
                                in.putExtra("childph", num);
                                in.putExtra("parentph", parent_num);

                                startActivity(in);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Search_Child.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                // hide the progress dialog
                Toast.makeText(Search_Child.this, "Please check entered values", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
