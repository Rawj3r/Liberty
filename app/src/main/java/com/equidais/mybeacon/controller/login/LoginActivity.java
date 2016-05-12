package com.equidais.mybeacon.controller.login;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.equidais.mybeacon.R;
import com.equidais.mybeacon.apiservice.ApiClient;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.common.LocalData;
import com.equidais.mybeacon.controller.JSONParser;
import com.equidais.mybeacon.controller.common.BaseActivity;
import com.equidais.mybeacon.controller.main.MainActivity;
import com.equidais.mybeacon.controller.register.Register;
import com.equidais.mybeacon.model.LoginResult;
import com.google.gson.JsonIOException;
import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    public static final int REQUEST_ENABLE_BT = 3;
    EditText mEditEmail;
    EditText mEditPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEditEmail = (EditText)findViewById(R.id.edit_email);
        mEditPassword = (EditText)findViewById(R.id.edit_password);
        findViewById(R.id.btn_login).setOnClickListener(this);

        if (!loadSha().equals("")){

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public String loadSha(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("clockingapp", 0);
        String usermail = sharedPreferences.getString("username", "");
        return usermail;
    }


    public void newAct(View view){
        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
    }

    private void beatconTest(){
        final SensoroManager sensoroManager = SensoroManager.getInstance(this);
        /**
         * Check whether the Bluetooth is on
         **/
        if (sensoroManager.isBluetoothEnabled()) {
            /**
             * Enable cloud service (upload sensor data, including battery status, UMM, etc.). Without setup, it keeps in closed status as default.
             **/
            sensoroManager.setCloudServiceEnable(true);
            /**
             * Enable SDK service
             **/
            try {
               sensoroManager.startService();
            } catch (Exception e) {
                e.printStackTrace(); // Fetch abnormal info
            }
            sensoroManager.addBroadcastKey("0117C5393A7A");
            BeaconManagerListener beaconManagerListener = new BeaconManagerListener() {
                @Override
                public void onUpdateBeacon(ArrayList<Beacon> beacons) {
                    // Refresh sensor info
                    for (int i = 0; i<beacons.size(); i++){
                        Beacon beacon = beacons.get(i);
                        updateView(beacon);
                    }
                }
                @Override
                public void onNewBeacon(Beacon beacon) {
                    // New sensor found
                    if (beacon.getSerialNumber().equals("0117C5393A7A")){
                        Toast.makeText(LoginActivity.this, "find beacon", Toast.LENGTH_SHORT).show();
                        // Yunzi with SN "0117C5456A36" enters the range
                    }
                }
                @Override
                public void onGoneBeacon(Beacon beacon) {
                    System.out.println("Out of range......");
//

                }
            };
            sensoroManager.setBeaconManagerListener(beaconManagerListener);
        }else{
            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothIntent, REQUEST_ENABLE_BT);
        }

    }


    private void updateView(Beacon beacon) {
        if (beacon == null) {
            return;
        }
        DecimalFormat format = new DecimalFormat("#");
        String distance = format.format(beacon.getAccuracy() * 100);
        System.out.println("" + distance + " cm");
//        ((TextView)findViewById(R.id.txt_test)).setText("" + distance + " cm");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login){
            final String email = mEditEmail.getText().toString();
            final String password = mEditPassword.getText().toString();
            if (email.equals("")){
                GlobalFunc.showAlertDialog(LoginActivity.this, getResources().getString(R.string.warning),
                        getResources().getString(R.string.alert_input_email));
                return;
            }
            if (password.equals("")){
                GlobalFunc.showAlertDialog(LoginActivity.this, getResources().getString(R.string.warning),
                        getResources().getString(R.string.alert_input_password));
                return;
            }




            class Login extends AsyncTask<String, String, JSONObject>{

                JSONParser jsonParser = new JSONParser();

                ProgressDialog progressDialog;

//                String LOGIN_URL = "http://masscash.empirestate.co.za/GenyaApi/X/index.php";
                String LOGIN = "http://masscash.empirestate.co.za/GenyaApi/X/login.php";

                private static final String TAG_SUCCESS = "success";
                private static final String TAG_MESSAGE = "message";

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Accessing");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                }

                @Override
                protected JSONObject doInBackground(String... params) {

                    try {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("user_Email", email);
                        hashMap.put("user_Password", password);

                        Log.d("request", "sending request");

                        JSONObject jsonObject = jsonParser.makeHttpRequest(LOGIN, "POST", hashMap);


                        if (jsonObject != null){
                            Log.d("JSON result", jsonObject.toString());
                            return jsonObject;
                        }
                    }catch (JsonIOException e){
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    super.onPostExecute(jsonObject);

                    int success = 0;
                    String message = "";

                    if (progressDialog != null){
                        progressDialog.dismiss();
                    }

                    if (jsonObject != null){


                        try {
                            Toast.makeText(LoginActivity.this, jsonObject.getString(TAG_MESSAGE).toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.e("Test " , jsonObject.toString());

                            success = jsonObject.getInt(TAG_SUCCESS);
                            message = jsonObject.getString(TAG_MESSAGE);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    if (success == 1){
                        Log.d("Success", message);
                        SharedPreferences sp = getSharedPreferences("clockingapp", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("username", email);
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Log.d("Failure", message);
                    }
                }
            }

            new Login().execute();

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}