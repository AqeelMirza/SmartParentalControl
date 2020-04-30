package com.spc.BroadcasrReceiver;

import android.app.*;
import android.content.*;
import android.os.*;

public class ServiceActivity extends Service {
	
	private boolean isRunning;
    private Context context;
	private Thread backgroundThread;
 
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
 
    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }
 
    private Runnable myTask = new Runnable() {
        public void run() {
            // Do something here
            stopSelf();
        }
    };
 
    @Override
    public void onDestroy() { 
        this.isRunning = false;
    }
 
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }
 
}
	
	

