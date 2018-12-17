package com.dhamodharan.leadsquared.Networking;

import com.dhamodharan.leadsquared.Model.ModelVM;
import com.dhamodharan.leadsquared.Model.Model_WH;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RequestInterface {

 /* @GET("v2/top-headlines?sources=google-news&apiKey=f20c7f924a5f4b1384407a0105013d92")
  Call<ModelVM> getJSON();*/

  @GET
  Call<ModelVM> getJSON(@Url String url);


  @GET("data/2.5/group?id=1264527,1277333,1275004,1275339,1261481&units=metric&appid=59a78b1fc0beacb90a6197bd75d3eb93")
  Call<Model_WH> getJSON_WH();

}