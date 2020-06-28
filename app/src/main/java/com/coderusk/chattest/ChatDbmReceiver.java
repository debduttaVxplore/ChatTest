package com.coderusk.chattest;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

public class ChatDbmReceiver extends BroadcastReceiver {

    public interface ChatDbmCallback
    {
        void reportOfSet(boolean report);
        void get(String value);
    }

    private static HashMap<String, ChatManager.ResponseCallback> tempCallbacks = new HashMap<>();

    public static void addTempCallback(String key, ChatManager.ResponseCallback callback)
    {
        tempCallbacks.put(key,callback);
    }

    private static ChatDbmCallback callback = null;

    public static void bindCallBack(ChatDbmCallback inputCallBack)
    {
        callback = inputCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if(!intentAction.equals("com.coderusk.chat_dbm_action")){return;}
        String action = intent.getStringExtra("action");
        if(action!=null)
        {
            switch (action)
            {
                case "report_of_set":
                    onReportOfSet(intent);
                    break;
                case "get":
                    onGet(intent);
                    break;
                case "fetch":
                    onFetch(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private void onReportOfSet(Intent intent) {
        boolean report = intent.getBooleanExtra("report",false);
        if(callback!=null)
        {
            callback.reportOfSet(report);
        }
    }

    private void onGet(Intent intent) {
        String value = intent.getStringExtra("value");
        if(callback!=null)
        {
            callback.get(value);
        }
    }

    private void onFetch(Intent intent) {
        String value = intent.getStringExtra("value");
        String uid = intent.getStringExtra("callback_uid");
        if(tempCallbacks!=null)
        {
            if(tempCallbacks.containsKey(uid))
            {
                ChatManager.ResponseCallback callback = tempCallbacks.remove(uid);
                if(callback!=null)
                {
                    callback.onResponse(value);
                }
            }
        }
    }
}
