package com.equidais.mybeacon.controller.common;


import android.util.Log;

import com.equidais.mybeacon.apiservice.ApiClient;
import com.equidais.mybeacon.common.LocalData;

import com.equidais.mybeacon.model.DetailsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by empirestate on 4/21/16.
 */
public class Controller{

    private final String TAG = Controller.class.getSimpleName();
    private HomeCallBackListener homeCallBackListener;
    private ApiClient apiClient;



    public Controller(HomeCallBackListener listener){
        homeCallBackListener = listener;
        apiClient = new ApiClient();
    }



    public void startFetching(){


        Map<String, String> map = new HashMap<>();

        map.put("username", LocalData.getmail().trim());
        apiClient.getApiInterface().getDetails(map, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.e(TAG, "JSON : " + s);

                try{
                    JSONArray array = new JSONArray(s);
                    for (int i =0; i < array.length(); i++){
                        JSONObject jsonObject = array.getJSONObject(i);

                        DetailsModel model = new DetailsModel.Builder()
                                .setMInTime(jsonObject.getString("timeLog_timeIN"))
                                .setMOutTime(jsonObject.getString("timeLog_timeOut"))
                                .build();
                        homeCallBackListener.onFetchProgress(model);

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                homeCallBackListener.onFetchComplete();

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error :: " + error.getMessage());
                homeCallBackListener.onFetchComplete();
            }
        });
    }



    public interface HomeCallBackListener{
        void onFetchStart();
        void onFetchProgress(DetailsModel detailsModel);
        void onFetchProgress(List<DetailsModel> detailsModelList);
        void onFetchComplete();
        void onFetchFailed();
    }

}
