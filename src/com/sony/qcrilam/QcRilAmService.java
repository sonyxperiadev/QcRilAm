package com.sony.qcrilam;

import android.app.Service;
import android.content.Intent;
import android.media.AudioSystem;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import vendor.qti.hardware.radio.am.V1_0.IQcRilAudio;
import vendor.qti.hardware.radio.am.V1_0.IQcRilAudioCallback;

public class QcRilAmService extends Service {
    private static final String TAG = "QcRilAm-Service";
    private static boolean isRunning = false;

    public static boolean isServiceRunning() {
        return isRunning;
    }

    private void addCallbackForSimSlot(int simSlotNo) {
        try {
            IQcRilAudio QcRilAudio = IQcRilAudio.getService("slot" + simSlotNo);
            if (QcRilAudio == null) {
                Log.e(TAG, "Could not get service instance for slot" + simSlotNo + ", failing");
            } else {
                QcRilAudio.setCallback(new IQcRilAudioCallback.Stub() {
                    public String getParameters(String keys) {
                        return AudioSystem.getParameters(keys);
                    }

                    public int setParameters(String keyValuePairs) {
                        return AudioSystem.setParameters(keyValuePairs);
                    }
                });
            }
        } catch(RemoteException exception) {
            Log.e(TAG, "RemoteException while trying to add callback for slot" + simSlotNo);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isRunning = true;
        int simCount = TelephonyManager.from(this).getSimCount();
        for (int simSlotNo = 1; simSlotNo <= simCount; simSlotNo++) {
            addCallbackForSimSlot(simSlotNo);
        }
    }

}
