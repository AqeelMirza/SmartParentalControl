package com.spc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.spc.Models.Call_Logs;
import com.spc.Models.Messages;
import com.spc.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<Call_Logs> call_logsArrayList;
    Call_Logs call_logs;
    ArrayList<Messages> messagesArrayList;
    Messages messages;
    int resourceID;
    Boolean isCalllogs;

    public RecyclerViewAdapter(Context con, ArrayList<Messages> messagesArrayList, ArrayList<Call_Logs> child_details_list, int resource, Boolean isCalllogs) {
        this.context = con;
        this.call_logsArrayList = child_details_list;
        this.resourceID = resource;
        this.isCalllogs = isCalllogs;
        this.messagesArrayList = messagesArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(resourceID, null);
        ViewHolder rcv = new ViewHolder(layoutView);


        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isCalllogs) {

            Collections.reverse(call_logsArrayList);


                holder.phnum.setText(position + " - " + "Mobile Number : " + call_logsArrayList.get(position).getPhNumber());
                holder.calltype.setText(call_logsArrayList.get(position).getCallType());

                String time = call_logsArrayList.get(position).getCallDate();
                Date callDayTime = new Date(Long.valueOf(time));

                holder.calldate.setText("Call Date - " + callDayTime);
                holder.callduration.setText("Duration - " + call_logsArrayList.get(position).getCallDuration());

        } else {

            holder.mobile_num.setText(position + " - " + "Mobile Number : " + messagesArrayList.get(position).getMobile_num());
            holder.msg_body.setText(messagesArrayList.get(position).getMsg_body());

        }

    }

    @Override
    public int getItemCount() {
        return (isCalllogs) ? call_logsArrayList.size() : messagesArrayList.size();
        //  return call_logsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView phnum, calltype, calldate, callduration;

        TextView mobile_num, msg_body;

        public ViewHolder(View itemView) {
            super(itemView);

            phnum = (TextView) itemView.findViewById(R.id.call_log_phNum);
            calltype = (TextView) itemView.findViewById(R.id.call_log_calltype);
            calldate = (TextView) itemView.findViewById(R.id.call_log_time);
            callduration = (TextView) itemView.findViewById(R.id.call_log_duration);

            mobile_num = (TextView) itemView.findViewById(R.id.msgs_phnum);
            msg_body = (TextView) itemView.findViewById(R.id.msgs_body);
        }
    }
}
