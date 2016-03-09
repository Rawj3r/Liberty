package com.equidais.mybeacon.controller;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.equidais.mybeacon.apiservice.ApiClient;
import com.equidais.mybeacon.common.GlobalConst;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.common.LocalData;
import com.equidais.mybeacon.controller.main.TransActivity;
import com.equidais.mybeacon.controller.service.MyService;
import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainApplication extends Application  implements BeaconManagerListener {
    private static final String TAG = MainApplication.class.getSimpleName();
    private SensoroManager sensoroManager;

    public static final int STATE_INIT = 0;
    public static final int STATE_ENTER_DOOR = 1;
    public static final int STATE_IN_ROOM = 2;
    public static final int STATE_OUT_ROOM_ENTER_DOOR = 3;
    public int mState = STATE_INIT;
    String mBeaconUDID = "";
    public Date mInTime;
    public Date mOutTime;
    String usermail;
    @Override
    public void onCreate() {
        super.onCreate();

        initSensoroSDK();

        /**
         * Start SDK in Service.
         */
        Intent intent = new Intent();
        intent.setClass(this, MyService.class);
        startService(intent);
    }

    public String loadSha(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Storedata", 0);
        usermail = sharedPreferences.getString("username", "");
        return usermail;
    }

    /**
     * Initial Sensoro SDK.
     */
    private void initSensoroSDK() {
        sensoroManager = SensoroManager.getInstance(getApplicationContext());
        sensoroManager.setCloudServiceEnable(true);
        sensoroManager.setBeaconManagerListener(this);
    }

    /**
     * Start Sensoro SDK.
     */
    public void startSensoroSDK() {
        mBeaconUDID = "";
        mState = STATE_INIT;
        Intent intent = new Intent("changeState");
        sendBroadcast(intent);
        try {
            sensoroManager.startService();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void stopSensoroSDK() {
        mBeaconUDID = "";
        mState = STATE_INIT;
        Intent intent = new Intent("changeState");
        sendBroadcast(intent);
        try {
            sensoroManager.stopService();

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    /**
     * Check whether bluetooth enabled.
     * @return
     */

    public boolean isBluetoothEnabled(){
        return sensoroManager.isBluetoothEnabled();
    }

    @Override
    public void onNewBeacon(Beacon beacon) {
        /**
         * Check whether SDK started in logs.
         */
        Log.e(TAG, beacon.getProximityUUID());

        String beaconUUID = beacon.getProximityUUID();

        if (beaconUUID.equals(mBeaconUDID)){
            if (mState == STATE_INIT){

                mState = STATE_ENTER_DOOR;

                upateData(beaconUUID);

            }else{
                mState = STATE_INIT;
            }
        }else{
            mState = STATE_INIT;
        }


    }

    @Override
    public void onGoneBeacon(Beacon beacon) {



        Log.e(TAG, "gone");
        String beaconUUID = beacon.getProximityUUID();

        Log.e(TAG, beaconUUID + " has disappeared");

        mState = STATE_INIT;


    }

    @Override
    public void onUpdateBeacon(ArrayList<Beacon> arrayList) {
        for (int i = 0; i<arrayList.size(); i++){
            Beacon beacon = arrayList.get(i);
            updateView(beacon);
        }
    }

    private void updateView(Beacon beacon) {
        if (beacon == null) {
            return;
        }
        DecimalFormat format = new DecimalFormat("#");
        String distance = format.format(beacon.getAccuracy() * 100);
        Log.e(beacon.getSerialNumber(), "" + distance + " cm");
    }

    private void upateData(String beaconUDID){

            Map<String, Object> map = new HashMap<>();
            map.put("usermail", loadSha());
            map.put("uuid", beaconUDID);
            map.put("deviceudid", GlobalFunc.getDeviceUDID(this));
            map.put("timein", GlobalFunc.getStringParamDate(mInTime));
            ApiClient.getApiClient().sendData(map, new Callback<Integer>() {
                @Override
                public void success(Integer integer, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

    }


}
