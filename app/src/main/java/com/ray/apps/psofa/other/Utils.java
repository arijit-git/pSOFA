package com.ray.apps.psofa.other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class Utils {


    private Context mContext = null;

    /**
     * Public constructor that takes mContext for later use
     */
    public Utils(Context con) {
        mContext = con;
    }


    //This is a method to Check if the device internet connection is currently on
    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }

    public static void showNoInternetToast(Context context)
    {
        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
