package com.coderusk.chattest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText etInput;
    private Button btSet;
    private Button btGet;
    private TextView textView;
    //////////////////////////
    ChatManager chatManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChatDbmReceiver.bindCallBack(new ChatDbmReceiver.ChatDbmCallback() {
            @Override
            public void reportOfSet(boolean report) {
                onReportOfSet(report);
            }

            @Override
            public void get(String value) {
                onGet(value);
            }
        });

        chatManager = ChatManager.get();

        etInput = (EditText)findViewById( R.id.et_input );
        btSet = (Button)findViewById( R.id.bt_set );
        btGet = (Button)findViewById( R.id.bt_get );
        textView = (TextView)findViewById( R.id.textView );

        TestViews testViews = findViewById(R.id.test_views);
        ArrayList<String> data = getData();
        testViews.setData(data);

        btSet.setOnClickListener(v -> save());

        btGet.setOnClickListener(v -> get());
    }

    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        for(int i=0;i<1000;++i)
        {
            data.add(i+"th item=lorem ipsum");
        }
        return data;
    }

    private void onGet(String value) {
        String text = textView.getText().toString();
        textView.setText(value+"\n"+text);
    }

    private void get() {
        Log.d("buggingh","get");
        chatManager.requestGet(this);
    }

    private void onReportOfSet(boolean report) {
        String text = textView.getText().toString();
        textView.setText((report?"true":"false")+"\n"+text);
    }

    private void save() {
        Log.d("buggingh","set");
        String value = etInput.getText().toString();
        chatManager.save(this,value);
    }
    /****************************************/
    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("buggingh","onPostResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("buggingh","onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("buggingh","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("buggingh","onDestroy");
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("buggingh","onPause");
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("buggingh","onResume");
    }
}