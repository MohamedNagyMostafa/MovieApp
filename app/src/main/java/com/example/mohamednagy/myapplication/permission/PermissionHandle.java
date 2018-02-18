package com.example.mohamednagy.myapplication.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Mohamed Nagy on 2/17/2018.
 */

public class PermissionHandle {

    public static final String ACCESS_INTERNET_PERMISSION = Manifest.permission.INTERNET;
    public static final String ACCESS_NETWORK_STATE_PERMISSION = Manifest.permission.ACCESS_NETWORK_STATE;
    public static final int REQUEST_CODE = 1;

    public static Boolean checkPermission(String permission, Context context){
        return (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED);
    }

}