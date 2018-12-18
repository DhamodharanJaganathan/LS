package com.dhamodharan.leadsquared.Fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dhamodharan.leadsquared.R;

public class TabFragment extends Fragment {

  private  TabLayout tabLayout;
  private  ViewPager viewPager;
  private static final int int_items = 2;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    //this inflates out tab layout file.
    View x =  inflater.inflate(R.layout.tab_fragment_layout,container,false);
    // set up stuff.
    tabLayout = x.findViewById(R.id.tabs);
    viewPager = x.findViewById(R.id.viewpager);

    // create a new adapter for our pageViewer. This adapters returns child fragments as per the positon of the page Viewer.
    viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

    // this is a workaround
    tabLayout.post(() -> {
      //provide the viewPager to TabLayout.
      tabLayout.setupWithViewPager(viewPager);
    });
    //to preload the adjacent tabs. This makes transition smooth.
    //viewPager.setOffscreenPageLimit(2);

    return x;
  }

  class MyAdapter extends FragmentPagerAdapter{

    MyAdapter(FragmentManager fm) {
      super(fm);
    }

    //return the fragment with respect to page position.
    @Override
    public Fragment getItem(int position)
    {
      switch (position){
        case 0 : return new Frag_Main_News();
        case 1 : return new Frag_Fav_News();
       }
      return null;
    }

    @Override
    public int getCount() {

      return int_items;

    }

    //This method returns the title of the tab according to the position.
    @Override
    public CharSequence getPageTitle(int position) {

      switch (position){
        case 0 :
          return "News";
        case 1 :
          return "Favourites";
      }
      return null;
    }
  }
}