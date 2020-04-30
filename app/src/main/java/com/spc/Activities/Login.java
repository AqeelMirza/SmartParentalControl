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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spc.R;
import com.spc.Utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    String TAG = "Login";
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static String parent_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

// Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        final EditText phone = (EditText) findViewById(R.id.login_phone);
        final EditText password = (EditText) findViewById(R.id.login_password);
        final Button loginbtn = (Button) findViewById(R.id.login_btn);
        Button newUser_btn = (Button) findViewById(R.id.login_newuser_btn);

        newUser_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Login.this, MainActivity_DB.class);
                startActivity(in);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonestr = phone.getText().toString();
                String password_str = password.getText().toString().trim();

                loginService(phonestr, password_str);

             /*   // Read from the database
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        User value = dataSnapshot.getValue(User.class);
                        Log.d(TAG, "Value is: " + value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });*/


            }
        });

    }

    void loginService(String phone, final String password) {


        //String url = "https://smart-mobile-tracker.firebaseio.com//users/" + phone + ".json";
        String url = "https://smartparentalcontrol-529aa.firebaseio.com/users/" + phone + ".json";

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

                            Toast.makeText(Login.this, "Username does not exist,Please check your details", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());

                                String parent_password = jsonObject.getString("parent_password");
                                if (password.equalsIgnoreCase(parent_password)) {
                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    parent_phone = jsonObject.getString("parent_phone");

                                    Intent i = new Intent(Login.this, Search_Child.class);
                                    i.putExtra("parentphone", parent_phone);
                                    startActivity(i);


                                } else {
                                    Toast.makeText(Login.this, "Password incorrect", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                Toast.makeText(Login.this, "Please check entered values", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
}
