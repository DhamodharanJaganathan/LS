package com.dhamodharan.leadsquared.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.dhamodharan.leadsquared.Adapter.Main_Fav_Adapter;
import com.dhamodharan.leadsquared.Model.ModelVM.Article;
import com.dhamodharan.leadsquared.R;
import com.dhamodharan.leadsquared.Realm.RealmData;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Frag_Fav_News extends Fragment {

  //a list to store all the products
  private List<Article> fav_list;

  //the recyclerview
  private RecyclerView recyclerView;
  //private DataAdapter_1 adapter;

  private void printLog(String s) {
// display a message in Log File
    Log.d("LifeCycle:", s);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    printLog("onActivityCreated Called");
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View v = inflater.inflate(R.layout.fragment, container, false);
    printLog("onCreateView Called");

    recyclerView = v.findViewById(R.id.card_recycler_view);
    recyclerView.setHasFixedSize(true);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
    recyclerView.setLayoutManager(layoutManager);

    return v;
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    printLog("onViewCreated Called");

  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    printLog("onCreate Called");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    printLog("onDestroy Called");
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    printLog("onDestroyView Called");
  }

  @Override
  public void onDetach() {
    super.onDetach();
    printLog("onDetach Called");
  }

  @Override
  public void onPause() {
    super.onPause();
    printLog("onPause Called");
  }

  @Override
  public void onResume() {
    super.onResume();
    printLog("onResume Called");
  }

  @Override
  public void onStart() {
    super.onStart();
    printLog("onStart Called");
  }

  @Override
  public void onStop() {
    super.onStop();
    printLog("onStop Called");
  }


  private void updateListView() {

    Realm realm = Realm.getDefaultInstance();

    RealmResults<RealmData> results = realm.where(RealmData.class).findAll();

    fav_list = new ArrayList<>();
    for (RealmData realmData1 : results) {
      //Toast.makeText(getActivity(), realmData1.getTitle(), Toast.LENGTH_SHORT).show();
      fav_list.add(new Article(realmData1.getSource(), realmData1.getTitle(), realmData1.getContent
          (), realmData1.getImageurl(),realmData1.getUrl()));
    }

    Log.d("TAG", "onStart: " + fav_list);

    /*adapter = new DataAdapter_1(fav_list, getActivity());
    recyclerView.setAdapter(adapter);*/
    Collections.reverse(fav_list);

    Main_Fav_Adapter mainFavAdapter = new Main_Fav_Adapter(fav_list, getActivity());
    recyclerView.setAdapter(mainFavAdapter);
    mainFavAdapter.notifyDataSetChanged();
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser) {
      updateListView();
      if(fav_list.size() != 0) {
        Toast.makeText(getActivity(), "Long press to delete bookmark", Toast.LENGTH_SHORT).show();
      }
    }
  }

}

  /*@Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(
        R.layout.fragment_fav, container, false);
    return rootView;
  }*/

