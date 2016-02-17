package com.example.user.blueputdroid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.ReferenceQueue;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    int REQUEST_ENABLE_BT;
    BroadcastReceiver mReciever;

    public MainActivity()
    {
        REQUEST_ENABLE_BT=2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up bluetooth
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mReciever);
        Log.v("Activity","mReciver Unregistered");
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(resultCode==RESULT_OK)
        {
            Log.v("Bluetooth", "Bluetooth Enabled");
        }
        else
        {
            Log.v("Bluetooth","Bluetooth cancelled By user");
        }
    }

    public void onBtEnable(View view)
    {
        if(mBluetoothAdapter==null)
        {
            Toast.makeText(getApplicationContext(),"No Bluetooth Detected",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(!mBluetoothAdapter.isEnabled())
            {
                Intent enableBtIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
            }
        }
    }

    public void queryBtDevices(View view)
    {
        Set<BluetoothDevice> pairedDevices=mBluetoothAdapter.getBondedDevices();

        final ArrayAdapter<String> mArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        //If there are bonded devices
        if(pairedDevices.size()>0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                mArrayAdapter.add(device.getName()+"\n"+device.getAddress());
            }
        }

        final ListView list=(ListView)findViewById(R.id.pairedList);
        list.setAdapter(mArrayAdapter);

        mReciever=new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                //When Discovery finds a device
                if(BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    Log.v("BT","Device found");
                    //Get Device from the intent
                    BluetoothDevice fdevice=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //Add device to the arrayadapter
                    mArrayAdapter.add(fdevice.getName()+"\n"+fdevice.getAddress());
                    list.setAdapter(mArrayAdapter);

                }
                Log.v("BT","onrecieve");
            }
        };

        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReciever,filter); // Dont forget to unregister the reciever
    }
}
