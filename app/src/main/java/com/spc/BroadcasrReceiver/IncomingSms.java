package com.spc.BroadcasrReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.widget.Toast;
import android.os.Build;


public class IncomingSms extends BroadcastReceiver {

    private Bundle bundle;
    private SmsMessage currentSMS;
    private String message;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdu_Objects = (Object[]) bundle.get("pdus");
                if (pdu_Objects != null) {

                    for (Object aObject : pdu_Objects) {
                        currentSMS = getIncomingMessage(aObject, bundle);
                        String senderNo = currentSMS.getDisplayOriginatingAddress();
                        message = currentSMS.getDisplayMessageBody();

                        // Toast.makeText(context, "senderNum: " + senderNo + " :\n message: " + message, Toast.LENGTH_LONG).show();

                        /*Intent i = new Intent(context, MainActivity.class);
                        i.putExtra("msgContent", message);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);*/

                        SharedPreferences prefs = context.getSharedPreferences("Number_Pref", 0);
                        String parent_phone = prefs.getString("parent_phone", null);

                        if (parent_phone != null && PhoneNumberUtils.compare(context, senderNo, parent_phone)) {
                            {
                                profilechange(context, message);
                            }
                        }
                    }
                    this.abortBroadcast();
                    // End of loop
                }
            }
        } // bundle null
    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSMS;
    }

    public void profilechange(Context context, String message) {
        if (message == null) {
        } else {
            AudioManager mobilemode = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  ");

            if (message.equalsIgnoreCase("general")) {
                mobilemode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            } else if (message.equalsIgnoreCase("silent")) {
                mobilemode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if (message.equalsIgnoreCase("vibrate")) {
                mobilemode.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }


        }
    }

}
