package com.coderusk.chattest;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayDeque;
import java.util.Queue;

class ChatManager {

    private static ChatManager instance = null;

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
}
