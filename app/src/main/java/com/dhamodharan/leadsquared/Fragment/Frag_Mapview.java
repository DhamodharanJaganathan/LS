package com.dhamodharan.leadsquared.Fragment;

import android.Manifest;
import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.dhamodharan.leadsquared.Model.Model_WH;
import com.dhamodharan.leadsquared.Networking.RequestInterface;
import com.dhamodharan.leadsquared.R;
import com.dhamodharan.leadsquared.Utils.InternetConnectionChecker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Frag_Mapview extends Fragment implements
    OnMyLocationButtonClickListener,
    OnMyLocationClickListener,
    OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

  private LatLng latLng;
  private GoogleApiClient mGoogleApiClient;
  private GoogleMap mMap;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(
        R.layout.map_fragment, container, false);
    SupportMapFragment mapFragment =
        (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
    Objects.requireNonNull(mapFragment).getMapAsync(this);
    Toast.makeText(getActivity(), "Turn ON location", Toast.LENGTH_SHORT).show();
    return rootView;
  }


  @Override
  public void onMapReady(GoogleMap map) {
    mMap = map;
    mMap.getUiSettings().setZoomControlsEnabled(true);
    mMap.setOnMyLocationButtonClickListener(this);
    mMap.setOnMyLocationClickListener(this);
    enableMyLocation();
  }


  private void enableMyLocation() {
    if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
        Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      permission_check();

    } else if (mMap != null) {
      // Access to the location has been granted to the app.
      mMap.setMyLocationEnabled(true);
      if (InternetConnectionChecker.getInstance(getActivity()).isOnline()) {
        buildGoogleApiClient();
      } else {
        Toast.makeText(getActivity(),
            " No Internet Connection \n Connect with Internet & reopen app", Toast
                .LENGTH_SHORT).show();
      }


    }
  }

  @Override
  public boolean onMyLocationButtonClick() {
    Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
    // Return false so that we don't consume the event and the default behavior still occurs
    // (the camera animates to the user's current position).
    return false;
  }

  @Override
  public void onMyLocationClick(@NonNull Location location) {
    Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
  }


  private void permission_check() {
    Permissions
        .check(getActivity(), permission.ACCESS_FINE_LOCATION, null, new PermissionHandler() {
          @Override
          public void onGranted() {

            buildGoogleApiClient();
          }
        });
  }

  private void loadJSON(GoogleMap mMap) {

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    RequestInterface request = retrofit.create(RequestInterface.class);
    Call<Model_WH> call = request.getJSON_WH();
    call.enqueue(new Callback<Model_WH>() {
      @Override
      public void onResponse(@NonNull Call<Model_WH> call, @NonNull Response<Model_WH> response) {

        Model_WH jsonResponse = response.body();

        // Bangalore
        Marker Bangalore_1 = mMap.addMarker(new MarkerOptions()
            .position(latLng)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
            .snippet("Climate: " + Objects.requireNonNull(jsonResponse).getList().get(1).getMain()+ " Celicus")
            .title("you"));
        Bangalore_1.showInfoWindow();

        //move map camera
        float zoomLevel = (float) 5.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

        // Delhi
        final LatLng delhi = new LatLng(28.6687667, 77.2213735);
        Marker delhi_1 = mMap.addMarker(new MarkerOptions()
            .position(delhi)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            .snippet("Climate: " + jsonResponse.getList().get(4).getMain().getTemp() + " Celicus")
            .title("New Delhi"));
        delhi_1.showInfoWindow();

        // Mumbai
        final LatLng mum = new LatLng(19.0898311, 72.720712);
        Marker mumbai_1 = mMap.addMarker(new MarkerOptions()
            .position(mum)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
            .snippet("Climate: " + jsonResponse.getList().get(3).getMain().getTemp() + " Celicus")
            .title("Mumbai"));
        mumbai_1.showInfoWindow();

        // Chennai
        final LatLng chen = new LatLng(13.0868424, 80.2081556);
        Marker chennai_1 = mMap.addMarker(new MarkerOptions()
            .position(chen)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            .snippet("Climate: " + jsonResponse.getList().get(0).getMain().getTemp() + " Celicus")
            .title("Chennai"));
        chennai_1.showInfoWindow();

        // Kolkata
        final LatLng kol = new LatLng(22.5856538, 88.3409289);
        Marker Kolkata = mMap.addMarker(new MarkerOptions()
            .position(kol)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
            .snippet("Climate: " + jsonResponse.getList().get(2).getMain().getTemp() + " Celicus")
            .title("Kolkata"));
        Kolkata.showInfoWindow();

      }

      @Override
      public void onFailure(@NonNull Call<Model_WH> call, @NonNull Throwable t) {
        Log.d("Error", t.getMessage());
      }
    });
  }


  private void startTimer(GoogleMap mMap) {
    Timer timer = new Timer();
    TimerTask timerTask = new TimerTask() {

      @Override
      public void run() {
        if (getActivity() != null) {
          getActivity().runOnUiThread(() -> {
            loadJSON(mMap);
            Log.d("TAG", "run: " + "MAP RELOADED");
          });
        }
      }
    };
    timer.scheduleAtFixedRate(timerTask, 0, 600000); // every min it will update data
  }


  private synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getActivity()))
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
    mGoogleApiClient.connect();
  }

  @Override
  public void onConnected(Bundle bundle) {
    LocationRequest mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(1000);
    mLocationRequest.setFastestInterval(1000);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
        Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
      LocationServices.FusedLocationApi
          .requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }
  }

  @Override
  public void onConnectionSuspended(int i) {
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
  }

  @Override
  public void onLocationChanged(Location location) {
    //Place current location marker
    latLng = new LatLng(location.getLatitude(), location.getLongitude());
    loadJSON(mMap);
    startTimer(mMap);
  }


}// open weather api  infodhamo@gmail.com   9788263792