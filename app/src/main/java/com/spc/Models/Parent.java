package com.spc.Models;

import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.HashMap;


public class Parent {

    String parent_name;
    String parent_phone;
    String parent_password;
    HashMap<String, Child> child_details = new HashMap();

    public Parent(String parent_name, String parent_phone, String parent_password, HashMap<String, Child> child_details) {
        this.parent_name = parent_name;
        this.parent_phone = parent_phone;
        this.child_details = child_details;
        this.parent_password = parent_password;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getParent_phone() {
        return parent_phone;
    }

    public void setParent_phone(String parent_phone) {
        this.parent_phone = parent_phone;
    }

    public String getParent_password() {
        return parent_password;
    }

    public void setParent_password(String parent_password) {
        this.parent_password = parent_password;
    }

    public HashMap<String, Child> getChild_details() {
        return child_details;
    }

    public void setChild_details(HashMap<String, Child> child_details) {
        this.child_details = child_details;
    }

    public Parent() {

    }
}
