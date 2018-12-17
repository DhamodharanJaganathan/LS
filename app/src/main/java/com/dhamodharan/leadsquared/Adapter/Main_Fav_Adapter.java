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
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dhamodharan.leadsquared.Adapter.Main_Fav_Adapter.ViewHolder;
import com.dhamodharan.leadsquared.Model.ModelVM.Article;
import com.dhamodharan.leadsquared.R;
import com.dhamodharan.leadsquared.Realm.RealmData;
import com.dhamodharan.leadsquared.Utils.GlideApp;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.List;

public class Main_Fav_Adapter extends Adapter<ViewHolder> {

  List<Article> fav_list;
  FragmentActivity activity;

  public Main_Fav_Adapter(List<Article> fav_list, FragmentActivity activity) {
    this.fav_list = fav_list;
    this.activity = activity;
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.card_row, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    viewHolder.tvBookmark.setVisibility(View.GONE);
    //viewHolder.tvVersion.setMaxLines(5);
    viewHolder.tvSource.setText(fav_list.get(i).getContent());
    viewHolder.tvName.setText(fav_list.get(i).getTitle());
    viewHolder.tvVersion.setText(fav_list.get(i).getDescription());
    GlideApp.with(activity)
        .load(fav_list.get(i).getUrlToImage())
        .override(300, 200)
        .placeholder(R.drawable.ic_warning_black_24dp)
        .error(R.drawable.ic_warning_black_24dp)
        .into(viewHolder.imageView);

    viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent i1 = new Intent(Intent.ACTION_VIEW);
        i1.setData(Uri.parse(fav_list.get(i).getUrl()));
        activity.startActivity(i1);

      }
    });


    viewHolder.cardView.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {

        Realm realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
          @Override
          public void execute(Realm realm) {

            RealmResults<RealmData> rows = realm.where(RealmData.class)
                .equalTo("title", fav_list.get(i)
                    .getTitle())
                .findAll();
            rows.deleteAllFromRealm();
            removeAt(i);
            Toast.makeText(activity, "Removed", Toast.LENGTH_SHORT).show();
          }
        });

        return true;
      }
    });

  }

  @Override
  public int getItemCount() {
    return fav_list.size();
  }


  static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.tv_source)
    TextView tvSource;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_bookmark)
    TextView tvBookmark;

    @BindView(R.id.card_view)
    CardView cardView;


    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }


  public void removeAt(int position) {
    fav_list.remove(position);
    notifyItemRemoved(position);
    notifyItemRangeChanged(position, fav_list.size());
  }


}
