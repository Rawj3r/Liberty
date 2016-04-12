package com.equidais.mybeacon.controller.main;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.equidais.mybeacon.R;

import com.equidais.mybeacon.controller.common.BaseActivity;
import com.sensoro.cloud.SensoroManager;


public class MainActivity extends BaseActivity implements ActionBar.OnNavigationListener{

    public static final int REQUEST_ENABLE_BT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconTest();

    }

    private void beaconTest(){
        final SensoroManager sensoroManager = SensoroManager.getInstance(this);

        if (sensoroManager.isBluetoothEnabled()){

        }else {
            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothIntent, REQUEST_ENABLE_BT);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }
}
