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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.ref.ReferenceQueue;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    int REQUEST_ENABLE_BT;
    BroadcastReceiver mReciever;
    boolean BroadcastRegisterStatus;

    public MainActivity()
    {
        BroadcastRegisterStatus=false;
        REQUEST_ENABLE_BT=2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up bluetooth
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        //Setting up the Bluetooth Switch

        Switch mySwitch=(Switch)findViewById(R.id.blueSwitch);
        final Button myButton=(Button)findViewById(R.id.connectButton);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    myButton.setEnabled(true);
                    BtEnable();
                }
                else
                {
                    myButton.setEnabled(false);
                    BtDisable();
                }
            }
        });

        mySwitch.setChecked(mBluetoothAdapter.isEnabled());
        myButton.setEnabled(mBluetoothAdapter.isEnabled());
        if(mBluetoothAdapter.isEnabled())
        {
            BtEnable();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if(BroadcastRegisterStatus)
            unregisterReceiver(mReciever);  //Unregister the Bluetooth enabled request reciever
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(resultCode==RESULT_OK)
        {
            //Query Paired Devices
            queryBtDevices();
        }
    }

    public void BtDisable()
    {
        if(mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.disable();
            ArrayAdapter<String> mArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
            Spinner spinner=(Spinner)findViewById(R.id.pairedSpinner);
            spinner.setAdapter(mArrayAdapter);
            ListView list=(ListView)findViewById(R.id.pairedList);
            list.setAdapter(mArrayAdapter);

        }
    }
    public void BtEnable()
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
            else
            {
                queryBtDevices();
            }
        }
    }

    public void queryBtDevices()
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
        final Spinner spinner=(Spinner)findViewById(R.id.pairedSpinner);
        list.setAdapter(mArrayAdapter);
        spinner.setAdapter(mArrayAdapter);

        if(!BroadcastRegisterStatus)
        {

            mReciever=new BroadcastReceiver()
             {
                @Override
                 public void onReceive(Context context, Intent intent)
                {
                    String action=intent.getAction();
                    //When Discovery finds a device
                    if(BluetoothDevice.ACTION_FOUND.equals(action))
                    {
                        BluetoothDevice fdevice=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        //Add device to the arrayadapter
                        mArrayAdapter.add(fdevice.getName()+"\n"+fdevice.getAddress());
                        list.setAdapter(mArrayAdapter);
                        spinner.setAdapter(mArrayAdapter);
                    }
                }
            };

            //Register Broadcast Reciever

            IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReciever,filter); // Dont forget to unregister the reciever
            BroadcastRegisterStatus=true;
        }
    }

    public void startConnectActivity(View view)
    {
        Spinner spinner=(Spinner)findViewById(R.id.pairedSpinner);
        String dev=spinner.getSelectedItem().toString();
        String[] devinfo=dev.split("\n");

        Bundle b=new Bundle();

        b.putString("deviceName",devinfo[0]);
        b.putString("deviceAddress",devinfo[1]);

        Intent intent=new Intent(this,ConnectActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }
}
