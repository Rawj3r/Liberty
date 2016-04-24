package com.equidais.mybeacon.apiservice;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.equidais.mybeacon.common.GlobalConst;
import com.equidais.mybeacon.controller.common.Constant;
import com.equidais.mybeacon.controller.common.StringDesirializer;
import com.equidais.mybeacon.model.LoginResult;
import com.equidais.mybeacon.model.MessageResult;
import com.equidais.mybeacon.model.VisitEntriesResult;
import com.equidais.mybeacon.model.VisitSummaryResult;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;


public class ApiClient extends AppCompatActivity{

    public static final String API_ROOT = GlobalConst.API_ROOT ;
    private static ApiInterface myBeaconService;
    String username;


    public static ApiInterface getApiClient() {
        if (myBeaconService == null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_ROOT)
                    .setClient(new OkClient(okHttpClient))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            myBeaconService = restAdapter.create(ApiInterface.class);
        }

        return myBeaconService;
    }

    private ApiInterface apiInterface;
    public ApiInterface getApiInterface(){
        if (apiInterface == null){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(String.class, new StringDesirializer());
            apiInterface = new RestAdapter.Builder()
                    .setEndpoint(Constant.BASE_URL)
                    .setConverter(new GsonConverter(gsonBuilder.create()))
                    .build()
                    .create(ApiInterface.class);
        }
        return apiInterface;
    }

    public String loadSha(){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Storedata", 0);
        username = sharedPreferences.getString("username", "");
        return username;
    }


    public interface ApiInterface {


        //------------Login----------------------
        @POST("/index.php")
        public void login(@Body Map<String, String> options, Callback<LoginResult> cb);

        //------------GetVisitSummary----------------------
        @POST("/spSummary.php")
        public void getVisitSummary(@Body Map<String, Object> options, Callback<List<VisitSummaryResult>> cb);

        //------------GetVisitEntries----------------------
        @POST("/spGetGymVisitDates.php")
        public void getVisitEntries(@Body Map<String, Object> options, Callback<List<VisitEntriesResult>> cb);

        //------------GetVisitEntries----------------------
        @POST("/spGetGymMessages.php")
        public void getMessageList(@Body Map<String, Object> options, Callback<List<MessageResult>> cb);

        //------------GetVisitEntries----------------------
        @POST("/spAddGymVisitFeedback.php")
        public void addGymVisitFeedback(@Body Map<String, Object> options, Callback<Integer> cb);

        //------------AddPersonGymVisit----------------------
        @POST("/untitled.php")
        public void sendData(@Body Map<String, Object> options, Callback<Integer> cb);

        //------------Register Push id----------------------
        @POST("/spRegisterPushID.php")
        public void registerPushId(@Body Map<String, Object> options, Callback<Response> cb);

        //----------------- Update personGymVisit ------------
        @POST("/spUpdatePersonGymVisit.php")
        public void upDatePersonGymVisit(@Body Map<String, Object> options, Callback<Integer> cb);

        @FormUrlEncoded
        @POST("/GetData.php")
        void getDetails(@FieldMap Map<String, String> map, Callback<String> details);

        @FormUrlEncoded
        @POST("/GetMData.php")
        void getMData(@FieldMap Map<String, String> map, Callback<String> callback);

        @FormUrlEncoded
        @POST("/GetAllData.php")
        void GetAllData(@FieldMap Map<String, String> map, Callback<String> callback);
    }
}

