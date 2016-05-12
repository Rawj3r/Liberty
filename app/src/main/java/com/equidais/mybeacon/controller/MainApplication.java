package com.equidais.mybeacon.controller;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import org.json.JSONObject;

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
    String username;
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
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("clockingapp", 0);
        username = sharedPreferences.getString("username", "");
        return username;
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

//        Log.e(TAG, "new beacon found");


        Log.e(TAG, "new beacon found");
        String beaconUUID = beacon.getProximityUUID();
        Log.e(TAG, beaconUUID + " new beacon found");

        if (!beaconUUID.equals("")) {
           if (mState == STATE_INIT){
               send();
           }
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



    public void send(){

        class sendD extends AsyncTask<String, String, JSONObject>{

            JSONParser jsonParser = new JSONParser();
            private static final String TAG_SUCCESS = "success";
            private static final String TAG_MESSAGE = "message";
            private final String REG_URL = "http://masscash.empirestate.co.za/GenyaApi/X/untitled.php";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                try {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("user_mail", loadSha());
                    hashMap.put("time_in", GlobalFunc.getStringParamDate(mInTime));

                    Log.e("request", "sending beacon time");
                    JSONObject jsonObject = jsonParser.makeHttpRequest(REG_URL, "POST", hashMap);
                    if (jsonObject != null){
                        Log.e("JSON result", jsonObject.toString());
                        return jsonObject;
                    }
                }catch (Exception e){

                }
                return null;
            }
        }
        new sendD().execute();
    }

    private void upateData(){

            Map<String, Object> map = new HashMap<>();
            map.put("user_mail", "m@mail.com");
            Log.e(TAG, loadSha());
           // map.put("uuid", beaconUDID);
           // map.put("deviceudid", GlobalFunc.getDeviceUDID(this));
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
