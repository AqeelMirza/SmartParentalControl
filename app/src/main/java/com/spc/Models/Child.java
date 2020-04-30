package com.spc.Models;

import android.telecom.Call;

import java.util.ArrayList;
import java.util.HashMap;


public class Child {

    public String child_name;
    public String child_phoneNum;
    public String child_email;
    public HashMap<String, ArrayList<Call_Logs>> call_details = new HashMap();
    public HashMap<String, ArrayList<Messages>> msgs_details = new HashMap();
    public HashMap<String, ArrayList<Location_Model>> loc_details = new HashMap();

    public Child(String child_name, String child_phoneNum, String child_email, HashMap<String, ArrayList<Call_Logs>> call_details, HashMap<String, ArrayList<Messages>> msgs_details, HashMap<String, ArrayList<Location_Model>> loc_details) {
        this.child_name = child_name;
        this.child_phoneNum = child_phoneNum;
        this.child_email = child_email;
        this.call_details = call_details;
        this.msgs_details = msgs_details;
        this.loc_details = loc_details;
    }

    public HashMap<String, ArrayList<Messages>> getMsgs_details() {

        return msgs_details;
    }

    public void setMsgs_details(HashMap<String, ArrayList<Messages>> msgs_details) {
        this.msgs_details = msgs_details;
    }

    public HashMap<String, ArrayList<Location_Model>> getLoc_details() {
        return loc_details;
    }

    public void setLoc_details(HashMap<String, ArrayList<Location_Model>> loc_details) {
        this.loc_details = loc_details;
    }

    public Child() {
    }

    public Child(String child_name, String child_phoneNum, String child_email, HashMap<String, ArrayList<Call_Logs>> call_details) {
        this.child_name = child_name;
        this.child_phoneNum = child_phoneNum;
        this.child_email = child_email;
        this.call_details = call_details;
    }

    public String getChild_name() {

        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getChild_phoneNum() {
        return child_phoneNum;
    }

    public void setChild_phoneNum(String child_phoneNum) {
        this.child_phoneNum = child_phoneNum;
    }

    public String getChild_email() {
        return child_email;
    }

    public void setChild_email(String child_email) {
        this.child_email = child_email;
    }

    public HashMap<String, ArrayList<Call_Logs>> getCall_details() {
        return call_details;
    }

    public void setCall_details(HashMap<String, ArrayList<Call_Logs>> call_details) {
        this.call_details = call_details;
    }

    public Child(String child_name, String child_phoneNum, String child_email) {

        this.child_name = child_name;
        this.child_phoneNum = child_phoneNum;
        this.child_email = child_email;
    }
}
