package com.equidais.mybeacon.controller.main;

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
 * Created by empirestate on 4/24/16.
 */
public class MonthController {

    private final String TAG = MonthController.class.getSimpleName();

    private MonthCallbackListener monthCallbackListener;
    private ApiClient apiClient;
    private Map<String, String> map = new HashMap<>();

    public MonthController(MonthCallbackListener listener){
        monthCallbackListener = listener;
        apiClient = new ApiClient();
    }

    public interface MonthCallbackListener{
        void onFetchStart();
        void onFetchProgress(DetailsModel detailsModel);
        void onFetchProgress(List<DetailsModel> detailsModelList);
        void onFetchComplete();
        void onFetchFailed();
    }

    public void getmonthdata(){



        map.put("username", LocalData.getmail().trim());

        apiClient.getApiInterface().getMData(map, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.e(TAG, "JSON : " + s);

                try{
                    JSONArray array = new JSONArray(s);
                    for (int i = 0; i <array.length(); i++){
                        JSONObject jsonObject = array.getJSONObject(i);

                        DetailsModel model = new DetailsModel.Builder()
                                .setMInTime(jsonObject.getString("timeLog_timeIN"))
                                .setMOutTime(jsonObject.getString("timeLog_timeOut"))
                                .build();
                        monthCallbackListener.onFetchProgress(model);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

                monthCallbackListener.onFetchComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error :: " + error.getMessage());
                monthCallbackListener.onFetchComplete();
            }
        });
    }

    public void gettotaldata(){
        Log.e("get total fragment", "is executing");
        map.put("username", LocalData.getmail().trim());
        apiClient.getApiInterface().GetAllData(map, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.e("Total controller", "JSON : " + s);

                try {
                    JSONArray array = new JSONArray(s);

                    for (int i = 0; i < array.length(); i++ ){
                        JSONObject object = array.getJSONObject(i);

                        DetailsModel model = new DetailsModel.Builder()
                                .setMInTime(object.getString("timeLog_timeIN"))
                                .setMOutTime(object.getString("timeLog_timeOut"))
                                .build();
                        monthCallbackListener.onFetchProgress(model);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                monthCallbackListener.onFetchComplete();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error :: " + error.getMessage());
                monthCallbackListener.onFetchComplete();
            }
        });
    }

}
