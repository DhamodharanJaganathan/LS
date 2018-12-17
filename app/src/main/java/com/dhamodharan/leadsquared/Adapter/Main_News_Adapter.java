package com.dhamodharan.leadsquared.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhamodharan.leadsquared.Adapter.Main_News_Adapter.ViewHolder;
import com.dhamodharan.leadsquared.Model.ModelVM.Article;
import com.dhamodharan.leadsquared.R;
import com.dhamodharan.leadsquared.Realm.RealmData;
import com.dhamodharan.leadsquared.Utils.GlideApp;
import io.realm.Realm;
import java.util.List;

public class Main_News_Adapter extends Adapter<ViewHolder> {

  FragmentActivity fragmentActivity;
  String Source = "Source: ";
  private List<Article> android;

  public Main_News_Adapter(List<Article> android,
      FragmentActivity fragmentActivity) {
    this.android = android;
    this.fragmentActivity = fragmentActivity;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.card_row, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    viewHolder.tvSource.setText(Source + android.get(i).getSource().getName());
    viewHolder.tvName.setText(android.get(i).getTitle());
    viewHolder.tvVersion.setText(android.get(i).getDescription());
    GlideApp.with(fragmentActivity)
        .load(android.get(i).getUrlToImage())
        .override(300, 200)
        .placeholder(R.drawable.ic_warning_black_24dp)
        .error(R.drawable.ic_warning_black_24dp)
        .into(viewHolder.imageView);

    viewHolder.tvBookmark.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        Realm realm = Realm.getDefaultInstance();

        RealmData user = realm.where(RealmData.class).equalTo("title", android.get(i).getTitle())
            .findFirst();

        if (user != null) {
          // Exists
          Toast.makeText(fragmentActivity, "Already added to favorites", Toast.LENGTH_SHORT).show();
        } else {
          realm.beginTransaction();
          RealmData realmData = realm.createObject(RealmData.class);
          realmData.setTitle(android.get(i).getTitle());
          realmData.setContent(android.get(i).getContent());
          realmData.setSource(android.get(i).getSource().getName());
          realmData.setImageurl(android.get(i).getUrlToImage());
          realmData.setUrl(android.get(i).getUrl());
          realm.commitTransaction();
          // Not exist
          Toast.makeText(fragmentActivity, "Added to favorites", Toast.LENGTH_SHORT).show();
        }

        /*RealmData realmData = realm.createObject(RealmData.class);
        realmData.setTitle(android.get(i).getTitle());
        realmData.setContent(android.get(i).getContent());
        realmData.setContent(android.get(i).getSource().getName());
        realmData.setImageurl(android.get(i).getUrlToImage());
        realm.commitTransaction();

        RealmResults<RealmData> results = realm.where(RealmData.class).findAll();

        for(RealmData realmData1 : results){
          Toast.makeText(fragmentActivity, realmData1.getTitle(), Toast.LENGTH_SHORT).show();
        }*/

        viewHolder.cardView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {

            Intent i1 = new Intent(Intent.ACTION_VIEW);
            i1.setData(Uri.parse(android.get(i).getUrl()));
            fragmentActivity.startActivity(i1);

          }
        });


      }
    });
  }


  @Override
  public int getItemCount() {
    return android.size();
  }


  static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_bookmark)
    TextView tvBookmark;
    @BindView(R.id.tv_source)
    TextView tvSource;

    @BindView(R.id.card_view)
    CardView cardView;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}

//  https://www.thecrazyprogrammer.com/2016/12/android-realm-database-tutorial.html

// android realm add data