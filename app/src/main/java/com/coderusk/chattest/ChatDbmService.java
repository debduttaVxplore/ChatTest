package com.coderusk.chattest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Queue;

public class ChatDbmService extends Service {
    public ChatDbmService() {
    }

    private Queue<Intent> tasks = new ArrayDeque();

    int mStartMode;

    IBinder mBinder;

    boolean mAllowRebind;

    @Override
    public void onCreate() {
        Log.d("buggingh","onCreate");
        startLooper();
    }

    boolean working = false;

    private void startLooper() {
        Log.d("buggingh","onStartCommand");
        if(!working)
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("buggingh","run");
                    working = true;
                    /******************************/
                    Intent intent = tasks.poll();
                    while (intent!=null)
                    {
                        Log.d("buggingh","intent!=null");
                        process(intent);
                        intent = tasks.poll();
                    }
                    /******************************/
                    working = false;
                }
            }).start();
        }

    }

    private void process(Intent intent) {
        if(intent!=null)
        {
            String action = intent.getStringExtra("action");
            if(action!=null)
            {
                switch (action)
                {
                    case "set_and_report":
                        setAndReport(intent);
                        break;
                    case "get":
                        get();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("buggingh","onStartCommand");
        tasks.add(intent);
        startLooper();
        return START_NOT_STICKY ;
    }

    private void setAndReport(Intent intent)
    {
        Log.d("buggingh","setAndReport");
        String value = intent.getStringExtra("value");
        boolean report = ChatDbm.set(this,value);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.coderusk.chat_dbm_action");
        broadcastIntent.putExtra("action","report_of_set");
        broadcastIntent.putExtra("report",report);
        sendBroadcast(broadcastIntent);
    }

    private void get()
    {
        Log.d("buggingh","gets");
        String value = ChatDbm.getLast(this);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.coderusk.chat_dbm_action");
        broadcastIntent.putExtra("action","get");
        broadcastIntent.putExtra("value",value);
        sendBroadcast(broadcastIntent);
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("buggingh","onBind");
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("buggingh","onUnbind");
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {
        Log.d("buggingh","onRebind");
    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        Log.d("buggingh","getonDestroy_service");
    }
}
