package com.example.user.blueputdroid;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ConnectActivity extends AppCompatActivity {

    private final String TAG="ConnectActivity";
    private BluetoothDevice _BluetoothDevice;
    private BluetoothAdapter _BluetoothAdapter;
    private String _DeviceName;
    private String _DeviceAddress;
    private BluetoothUtility _BluetoothUtility;
    private boolean _ConnectionStatus;

    private static TextView stateText;
    private static TextView messageText;
    private static Button sendMessageButton;
    private static Button connectButton;

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
        textView.setText(_DeviceName + "\n" + _DeviceAddress);
        stateText=(TextView)findViewById(R.id.stateText);
        messageText=(TextView)findViewById(R.id.messageText);
        sendMessageButton=(Button)findViewById(R.id.sendMessageButton);
        sendMessageButton.setEnabled(false);
        connectButton=(Button)findViewById(R.id.connectServerButton);
        _BluetoothUtility=new BluetoothUtility(mHandler);
        _BluetoothUtility.start();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
//        _BluetoothUtility.connect(_BluetoothDevice);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

//        _BluetoothUtility.start();
    }

//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy()
//    {
//        super.onDestroy();
//    }

    public void restartRequest()
    {
        this.finish();
        Intent intent=getIntent();
        startActivity(intent);
    }

    public void connectServer(View view)
    {
        _BluetoothUtility.connect(_BluetoothDevice);
    }

    public void sendMessage(View view)
    {
        EditText editText=(EditText)findViewById(R.id.messageToSend);
        String msg=editText.getText().toString();
        _BluetoothUtility.write(msg.getBytes(Charset.defaultCharset()));
    }

    public void vlcPlayPause(View view)
    {
        String msg="32";
        _BluetoothUtility.write(msg.getBytes(Charset.defaultCharset()));
    }

    public void vlcVolumeUp(View view)
    {
        String msg="175";
        _BluetoothUtility.write(msg.getBytes(Charset.defaultCharset()));
    }

    public void vlcVolumeDown(View view)
    {
        String msg="174";
        _BluetoothUtility.write(msg.getBytes(Charset.defaultCharset()));

    }

    private Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg)
        {
            Integer what=msg.what;
//            stateText.setText(what.toString());
//            messageText.setText(msg.obj.toString());

            switch(what)
            {
                case Constants.MESSAGE_STATE_CHANGE:
                    String res="";
                    switch (msg.arg1)
                    {
                        case BluetoothUtility.STATE_CONNECTED:
                            res="CONNECTED";
                            sendMessageButton.setEnabled(true);
                            connectButton.setEnabled(false);
                            break;
                        case BluetoothUtility.STATE_CONNECTING:
                            res="CONNECTING...";
                            break;
                        case BluetoothUtility.STATE_ERROR:
                            res="ERROR!!!!";
                            break;
                        case BluetoothUtility.STATE_DISCONNECTED:
                            sendMessageButton.setEnabled(false);
                            connectButton.setEnabled(true);
                            res="DISCONNECTED";
                            restartRequest();
                            break;
                        case BluetoothUtility.STATE_LISTEN:
                            res="LISTENING....";
                            break;
                        default:
                            res="WAITING.....";
                            break;
                    }
                    stateText.setText(res);
                    break;
                default:
                    String res1=msg.obj.toString();
                    messageText.setText(res1);
                    break;
            }
        }

    };
}
