package com.dhamodharan.leadsquared.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import java.util.Objects;

public class InternetConnectionChecker {

    @SuppressLint("StaticFieldLeak")
    private static final InternetConnectionChecker instance = new InternetConnectionChecker();
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private boolean connected = false;

    public static InternetConnectionChecker getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
            }
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
}