package com.coderusk.chattest;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.UUID;

class ChatManager {

    private static ChatManager instance = null;

    public interface ResponseCallback
    {
        void onResponse(String response);
    }



    private ChatManager() {
    }

    public static ChatManager get()
    {
        if(instance==null)
        {
            instance = new ChatManager();
        }
        return instance;
    }

    public void save(Context context,String value) {
        Intent intent = new Intent(context, ChatDbmService.class);
        intent.putExtra("action","set_and_report");
        intent.putExtra("value",value);
        context.startService(intent);
    }

    public void requestGet(Context context) {
        Intent intent = new Intent(context, ChatDbmService.class);
        intent.putExtra("action","get");
        context.startService(intent);
    }

    public void fetch(Context context,ResponseCallback callback) {
        String uid = UUID.randomUUID().toString();
        ChatDbmReceiver.addTempCallback(uid, callback);
        Intent intent = new Intent(context, ChatDbmService.class);
        intent.putExtra("action","fetch");
        intent.putExtra("callback_uid",uid);
        context.startService(intent);
    }
}
