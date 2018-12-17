package com.dhamodharan.leadsquared.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.dhamodharan.leadsquared.Adapter.Main_News_Adapter;
import com.dhamodharan.leadsquared.Model.ModelVM;
import com.dhamodharan.leadsquared.Networking.RequestInterface;
import com.dhamodharan.leadsquared.R;
import com.dhamodharan.leadsquared.Utils.Demo_point;
import com.dhamodharan.leadsquared.Utils.InternetConnectionChecker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class

Frag_Main_News extends Fragment {

  private RecyclerView recyclerView;
  private Main_News_Adapter adapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(
        R.layout.fragment, container, false);
    initViews(rootView);
    return rootView;
  }

  private void initViews(View rootView) {
    recyclerView = (RecyclerView) rootView.findViewById(R.id.card_recycler_view);
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView
        .getContext());
    recyclerView.setLayoutManager(layoutManager);
    if (InternetConnectionChecker.getInstance(getActivity()).isOnline()) {
      Toast.makeText(getActivity(), "Loading", Toast.LENGTH_SHORT).show();
      loadJSON();
    } else {
      Toast.makeText(getActivity(), " No Internet Connection \n Connect with Internet & reopen app",
          Toast.LENGTH_SHORT).show();
    }
  }

  private void loadJSON() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://newsapi.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    RequestInterface request = retrofit.create(RequestInterface.class);
    Call<ModelVM> call = request.getJSON
        ("v2/top-headlines?sources=" + Demo_point.value
            + "&apiKey=f20c7f924a5f4b1384407a0105013d92");
    call.enqueue(new Callback<ModelVM>() {
      @Override
      public void onResponse(Call<ModelVM> call, Response<ModelVM> response) {
        ModelVM jsonResponse = response.body();
        adapter = new Main_News_Adapter(jsonResponse.getArticles(), getActivity());
        recyclerView.setAdapter(adapter);
      }

      @Override
      public void onFailure(Call<ModelVM> call, Throwable t) {
        Log.d("Error", t.getMessage());
      }
    });
  }
}

//https://newsapi.org/v2/top-headlines?sources=google-news&apiKey=f20c7f924a5f4b1384407a0105013d92