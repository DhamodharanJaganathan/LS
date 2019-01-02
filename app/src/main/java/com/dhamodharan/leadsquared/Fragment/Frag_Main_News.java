package com.dhamodharan.leadsquared.Fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import java.util.Objects;
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
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(
        R.layout.fragment, container, false);
    initViews(rootView);
    return rootView;
  }

  private void initViews(View rootView) {
    recyclerView = rootView.findViewById(R.id.card_recycler_view);
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView
        .getContext());
    recyclerView.setLayoutManager(layoutManager);
    if (InternetConnectionChecker.getInstance(Objects.requireNonNull(getActivity())).isOnline()) {
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
      public void onResponse(@NonNull Call<ModelVM> call, @NonNull Response<ModelVM> response) {
        ModelVM jsonResponse = response.body();
        adapter = new Main_News_Adapter(Objects.requireNonNull(jsonResponse).getArticles(), getActivity());
        recyclerView.setAdapter(adapter);
      }

      @Override
      public void onFailure(@NonNull Call<ModelVM> call, @NonNull Throwable t) {
        Log.d("Error", t.getMessage());
      }
    });
  }
}

//https://newsapi.org/v2/top-headlines?sources=google-news&apiKey=f20c7f924a5f4b1384407a0105013d92