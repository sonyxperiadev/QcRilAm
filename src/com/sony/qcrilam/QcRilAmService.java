package com.sony.qcrilam;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.SubscriptionManager;
import android.util.Log;
import vendor.qti.hardware.radio.am.V1_0.IQcRilAudio;
import vendor.qti.hardware.radio.am.V1_0.IQcRilAudioCallback;

public class QcRilAmService extends Service {
    private static final String TAG = "QcRilAm-Service";
    private static boolean isRunning = false;

    public static boolean isServiceRunning() {
        return isRunning;
    }

    private void addCallbackForSimSlot(final int simSlotNo, final AudioManager audioManager) {
        try {
            IQcRilAudio QcRilAudio = IQcRilAudio.getService("slot" + simSlotNo);
            if (QcRilAudio == null) {
                Log.e(TAG, "Could not get service instance for slot" + simSlotNo + ", failing");
            } else {
                QcRilAudio.setCallback(new IQcRilAudioCallback.Stub() {
                    public String getParameters(String keys) {
                        return audioManager.getParameters(keys);
                    }

                    public int setParameters(String keyValuePairs) {
                        /* return */ audioManager.setParameters(keyValuePairs);
                        // AudioManager.setParameters does not check nor return
                        // the value coming from AudioSystem.setParameters.
                        // Assume there was no error:
                        return 0;
                    }
                });
            }
        } catch (RemoteException exception) {
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
        int simCount = SubscriptionManager.from(this).getActiveSubscriptionInfoCountMax();
        Log.i(TAG, "Device has " + simCount + " sim slots");
        final AudioManager audioManager = getSystemService(AudioManager.class);
        if (audioManager == null)
            throw new RuntimeException("Can't get audiomanager!");
        for (int simSlotNo = 1; simSlotNo <= simCount; simSlotNo++) {
            addCallbackForSimSlot(simSlotNo, audioManager);
        }
    }

}
