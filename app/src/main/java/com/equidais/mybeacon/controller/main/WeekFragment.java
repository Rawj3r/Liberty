package com.equidais.mybeacon.controller.main;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.equidais.mybeacon.R;
import com.equidais.mybeacon.common.GlobalFunc;
import com.equidais.mybeacon.controller.JSONParser;
import com.equidais.mybeacon.controller.MainApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WeekFragment extends Fragment {

    private ProgressDialog dialog;
    private JSONParser jsonParser;
    private String usermail, avg_duration, last_d;
    private int week_num_visits;
    TextView visits_count, avg_dur, curr_visit_dur, last_date;
    boolean mIsFinish = false;
    public Context context = getContext();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("changeState");
        getActivity().registerReceiver(broadcastReceiver, filter);
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_week2, container, false);
        visits_count = (TextView)view.findViewById(R.id.txt_visitsw);
        avg_dur = (TextView)view.findViewById(R.id.txt_average_visit_duration);
        last_date = (TextView)view.findViewById(R.id.txt_last_visit_date);
        curr_visit_dur = (TextView)view.findViewById(R.id.txt_current_visit_duration);

        new GetVisits().execute();

        showState();


        return view;
    }



    public String loadSha(){
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Storedata", 0);
        usermail = sharedPreferences.getString("username", "");
        return usermail;
    }

    public void showState(){
        MainApplication application = (MainApplication)getActivity().getApplicationContext();
        if (application.mState == MainApplication.STATE_INIT){
            curr_visit_dur.setText("0");
        }else if ((application.mState == MainApplication.STATE_IN_ROOM) || (application.mState == MainApplication.STATE_OUT_ROOM_ENTER_DOOR)){
            curr_visit_dur.setText(GlobalFunc.getDurationFromInTime(application.mInTime));

        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showState();
            handler.sendEmptyMessageDelayed(0, 500);

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        mIsFinish = true;

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("changeState")){
                showState();
            }
        }
    };

    class GetVisits extends AsyncTask<String, String, JSONObject>{

        public final String DATA = "http://masscash.empirestate.co.za/gravity/getwVisitsAvgDuration.php";
        public JSONObject object;

        // before we start out background thread lets show a progress dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            jsonParser = new JSONParser();

            try {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", loadSha());

                object = jsonParser.makeHttpRequest(DATA, "GET", map);

                if (object != null){
                    Log.e("JSON RESULT", object.toString());
                    return object;
                }else {
                    Log.e("object", "is null");
                }
            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            int success;
            String message = "";

            if (dialog.isShowing()){
                dialog.dismiss();
            }



                ConnectivityManager manager =  (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = manager.getActiveNetworkInfo();
                boolean connected = info != null && info.isAvailable() && info.isConnectedOrConnecting();

                if (connected){
                    try{
                        JSONArray jsonArray = object.getJSONArray("data");
                        JSONObject object1 = jsonArray.getJSONObject(0);
                        week_num_visits = object1.getInt("VisitCount");
                        avg_duration = object1.getString("AvgVisitDuration");
                        last_d = object1.getString("LastVisitDate");
                        Log.e("test", ""+week_num_visits);

                    }catch (JSONException d){
                        d.printStackTrace();
                    }
                    if (info.getType() == ConnectivityManager.TYPE_WIMAX || info.getType() == ConnectivityManager.TYPE_MOBILE){

                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    //set title
                    builder.setTitle("Please retry");

                    //set dialog message
                    builder.setMessage("Please connect your device to the internet").setCancelable(true);
                    // create alert dialog
                    AlertDialog alertDialog = builder.create();
                    // display dialog alert
                    alertDialog.show();
                }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    visits_count.setText(Integer.toString(week_num_visits));
                    avg_dur.setText(avg_duration);
                    last_date.setText(last_d);
                }
            });

        }
    }

}
