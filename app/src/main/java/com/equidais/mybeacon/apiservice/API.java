package com.equidais.mybeacon.apiservice;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by empirestate on 4/21/16.
 */
public interface API {

    @GET("/details.php")
    void getDetails(Callback<String> details);

}
