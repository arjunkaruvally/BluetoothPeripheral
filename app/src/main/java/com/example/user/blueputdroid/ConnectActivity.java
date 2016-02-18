package com.example.user.blueputdroid;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ConnectActivity extends AppCompatActivity {

    BluetoothDevice mBluetoothDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        String text=b.get("deviceName").toString()+" "+b.get("deviceAddress").toString();
        TextView textView=(TextView)findViewById(R.id.deviceInfo);
        textView.setText(text);
    }
}
