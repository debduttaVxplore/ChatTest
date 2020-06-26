package com.coderusk.chattest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ChatDbmReceiver extends BroadcastReceiver {

    public interface ChatDbmCallback
    {
        void reportOfSet(boolean report);
        void get(String value);
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
}
