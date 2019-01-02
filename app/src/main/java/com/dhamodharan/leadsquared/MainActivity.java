package com.dhamodharan.leadsquared;

import android.content.res.Configuration;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import com.dhamodharan.leadsquared.Fragment.Frag_Mapview;
import com.dhamodharan.leadsquared.Fragment.TabFragment;
import com.dhamodharan.leadsquared.Utils.Demo_point;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

  private DrawerLayout drawerLayout;
  private Toolbar toolbar;
  private NavigationView navigationView;
  private ActionBarDrawerToggle drawerToggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Realm
    Realm.init(this);

    initializeStuff();

    // since, NoActionBar was defined in theme, we set toolbar as our action bar.
    setSupportActionBar(toolbar);

    //this basically defines on click on each menu item.
    setUpNavigationView(navigationView);

    //This is for the Hamburger icon.
    drawerToggle = setupDrawerToggle();
    drawerLayout.addDrawerListener(drawerToggle);

    //Inflate the first fragment,this is like home fragment before user selects anything.
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.frameContent,new TabFragment()).commit();
    navigationView.setCheckedItem(R.id.nav_india);
    setTitle("India");

  }

  private void initializeStuff(){
    drawerLayout = findViewById(R.id.drawerLayout);
    toolbar = findViewById(R.id.toolbar);
    navigationView = findViewById(R.id.navigationDrawer);
  }

  /**
   * Inflate the fragment according to item clicked in navigation drawer.
   */
  private void setUpNavigationView(final NavigationView navigationView) {
    navigationView.setNavigationItemSelectedListener(
        menuItem -> {
          //replace the current fragment with the new fragment.
          Fragment selectedFragment = selectDrawerItem(menuItem);
          FragmentManager fragmentManager = getSupportFragmentManager();
          fragmentManager.beginTransaction().replace(R.id.frameContent, selectedFragment).commit();
          // the current menu item is highlighted in navigation tray.
          navigationView.setCheckedItem(menuItem.getItemId());
          setTitle(menuItem.getTitle());
          //close the drawer when user selects a nav item.
          drawerLayout.closeDrawers();
          return true;
        });
  }

  /**
   * This method returns the fragment according to navigation item selected.
   */
  private Fragment selectDrawerItem(MenuItem menuItem){
    Fragment fragment = null;
    switch(menuItem.getItemId()) {
      case R.id.nav_india:
        fragment = new TabFragment();
        Demo_point.value="google-news-in";
        break;
      case R.id.nav_news:
        fragment = new TabFragment();
        Demo_point.value="google-news";
        break;
      case R.id.nav_tech:
        fragment = new TabFragment();
        Demo_point.value="the-verge";
        break;
      case R.id.nav_sport:
        fragment = new TabFragment();
        Demo_point.value="espn-cric-info";
        break;
      case R.id.nav_weather:
        fragment = new Frag_Mapview();
        break;
    }
    return fragment;
  }

  /**
   * This is to setup our Toggle icon. The strings R.string.drawer_open and R.string.drawer close, are for accessibility (generally audio for visually impaired)
   * use only. It is now showed on the screen. While the remaining parameters are required initialize the toggle.
   */
  private ActionBarDrawerToggle setupDrawerToggle() {
    return new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.drawer_open,R.string.drawer_close);
  }

  /**
   * This makes sure that the action bar home button that is the toggle button, opens or closes the drawer when tapped.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    drawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    drawerToggle.onConfigurationChanged(newConfig);
  }
}