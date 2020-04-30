package com.spc.Models;

public class Messages {

    public String mobile_num;
    public String msg_body;

    public Messages() {
    }

    public String getMobile_num() {
        return mobile_num;
    }

    public void setMobile_num(String mobile_num) {
        this.mobile_num = mobile_num;
    }

    public String getMsg_body() {
        return msg_body;
    }

    public void setMsg_body(String msg_body) {
        this.msg_body = msg_body;
    }

    public Messages(String mobile_num, String msg_body) {

        this.mobile_num = mobile_num;
        this.msg_body = msg_body;
    }


}
