package com.sony.qcrilam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "QcRilAm-BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
            Log.w(TAG, "BOOT_COMPLETE NULL intent");
            return;
        }
        if (QcRilAmService.isServiceRunning()) {
            Log.d(TAG, "Service is already running");
        } else {
            intent.setClass(context, QcRilAmService.class);
            context.startService(new Intent(context, QcRilAmService.class));
        }
    }
}
