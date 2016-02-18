package com.example.user.blueputdroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ConnectActivity extends AppCompatActivity {

    private final String TAG="ConnectActivity";
    private BluetoothDevice _BluetoothDevice;
    private BluetoothAdapter _BluetoothAdapter;
    private String _DeviceName;
    private String _DeviceAddress;
    private BluetoothUtility _BluetoothUtility;
    private boolean _ConnectionStatus;

    public ConnectActivity()
    {
        _DeviceName="";
        _DeviceAddress="";
        _ConnectionStatus=false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        _DeviceName=b.getString("deviceName");
        _DeviceAddress=b.getString("deviceAddress");
        _BluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        _BluetoothDevice=_BluetoothAdapter.getRemoteDevice(_DeviceAddress);

        if(_BluetoothDevice==null)
        {
            Log.v(TAG,"Bluetooth Device Null");
        }

        TextView textView=(TextView)findViewById(R.id.deviceInfo);
        textView.setText(_DeviceName+"\n"+_DeviceAddress);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        _BluetoothUtility=new BluetoothUtility();
        _BluetoothUtility.connect(_BluetoothDevice);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        _BluetoothUtility.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
