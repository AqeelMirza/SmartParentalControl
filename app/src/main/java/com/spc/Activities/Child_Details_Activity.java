package com.spc.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spc.Models.Call_Logs;
import com.spc.Models.Child;
import com.spc.Models.Location_Model;
import com.spc.Models.Messages;
import com.spc.Models.Parent;
import com.spc.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Child_Details_Activity extends AppCompatActivity {
    EditText child_name, child_phone, child_email;
    Button create_btn, add_btn;
    Child child_model;
    Parent parent_model;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    String name, parent_name, parent_password;
    String phone;
    String email;
    String parent_phone;
    ArrayList<Call_Logs> call_logsArrayList;
    Call_Logs call_logs;
    ArrayList<Messages> messagesArrayList;
    Messages messages;
    ArrayList<Location_Model> locationArrayList;
    Location_Model location;
    SharedPreferences.Editor editor;
    Boolean addchild = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.child_details);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        //getting the value from previous fragment
        addchild = getIntent().getBooleanExtra("addchild", false);
        //initialising the SharedPref
        editor = getSharedPreferences("isParentCreated_Pref", MODE_PRIVATE).edit();


        call_logs = new Call_Logs();
        messages = new Messages();
        location = new Location_Model();

        call_logsArrayList = new ArrayList<>();
        messagesArrayList = new ArrayList<>();
        locationArrayList = new ArrayList<>();

        child_name = (EditText) findViewById(R.id.child_name);
        child_email = (EditText) findViewById(R.id.child_email);
        child_phone = (EditText) findViewById(R.id.child_phone);

        create_btn = (Button) findViewById(R.id.create_btn);
        add_btn = (Button) findViewById(R.id.add_btn);


        if (addchild) {
            parent_phone = Login.parent_phone;
            add_btn.setVisibility(View.VISIBLE);
            create_btn.setVisibility(View.GONE);
        } else {
            parent_name = getIntent().getStringExtra("parentname");
            parent_phone = getIntent().getStringExtra("parentphone");
            parent_password = getIntent().getStringExtra("parentpassword");
            add_btn.setVisibility(View.GONE);
            create_btn.setVisibility(View.VISIBLE);
        }


        //Create new child when button pressed
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = child_name.getText().toString().trim();
                phone = child_phone.getText().toString().trim();
                email = child_email.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    Toast.makeText(Child_Details_Activity.this, "Please check the details and try again.", Toast.LENGTH_SHORT).show();

                } else {

                    HashMap<String, ArrayList<Call_Logs>> child_calls_map = new HashMap();
                    child_calls_map.put("Call_Logs", call_logsArrayList);

                    HashMap<String, ArrayList<Messages>> child_msgs_map = new HashMap();
                    child_msgs_map.put("Messages_List", messagesArrayList);

                    HashMap<String, ArrayList<Location_Model>> child_loc_map = new HashMap();
                    child_loc_map.put("Location_List", locationArrayList);


                    child_model = new Child(name, phone, email, child_calls_map, child_msgs_map, child_loc_map);

                    HashMap<String, Child> child_map = new HashMap();
                    child_map.put(phone, child_model);

                    parent_model = new Parent(parent_name, parent_phone, parent_password, child_map);

                    mFirebaseDatabase.child(parent_phone).setValue(parent_model);

                    Toast.makeText(Child_Details_Activity.this, "parent created successfully", Toast.LENGTH_SHORT).show();

                    editor.putBoolean("isParent", true);
                    editor.putString("parent_name", parent_name);
                    editor.putString("parent_phone", parent_phone);
                    editor.commit();

                    Intent i = new Intent(Child_Details_Activity.this, Login.class);
                    startActivity(i);
                }
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = child_name.getText().toString().trim();
                phone = child_phone.getText().toString().trim();
                email = child_email.getText().toString().trim();

                //child_model = new Child(name, phone, email);
                HashMap<String, ArrayList<Call_Logs>> child_calls_map = new HashMap();
                child_calls_map.put("Call_Logs", call_logsArrayList);

                HashMap<String, ArrayList<Messages>> child_msgs_map = new HashMap();
                child_msgs_map.put("Messages_List", messagesArrayList);

                HashMap<String, ArrayList<Location_Model>> child_loc_map = new HashMap();
                child_loc_map.put("Location_List", locationArrayList);


                child_model = new Child(name, phone, email, child_calls_map, child_msgs_map, child_loc_map);

                //  mFirebaseDatabase.push().child(parent_name).child("child_details").child(name).setValue(name);
                mFirebaseDatabase.child(parent_phone).child("child_details").child(phone).setValue(child_model);

                Toast.makeText(Child_Details_Activity.this, "Child add successfully", Toast.LENGTH_SHORT).show();

                Intent in = new Intent(Child_Details_Activity.this, Search_Child.class);
                in.putExtra("parentphone", parent_phone);
                startActivity(in);


            }
        });
    }
}
