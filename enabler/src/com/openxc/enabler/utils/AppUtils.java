package com.openxc.enabler.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

public class AppUtils {

    private static String TAG = "AppUtils";

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Could not get application version name.", e);
        }
        return versionName;
    }

    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Could not get application version code.", e);
        }
        return versionCode;
    }
}
