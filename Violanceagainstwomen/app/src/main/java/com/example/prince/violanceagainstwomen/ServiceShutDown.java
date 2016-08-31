package com.example.prince.violanceagainstwomen;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by SABBIR on 4/7/2016.
 */
public class ServiceShutDown extends BroadcastReceiver {
    Utility utility;
    Context context;
    private boolean sent = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        utility = new Utility(context);

        if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            new Thread() {
                public void run() {
                    if (sent != true) {
                        {
                            //senddSMS();
                            utility.smsSend("01816692597","halum");
                        }
                    } else {
                        return;
                    }
                    try {
                        Log.v("SOS_APP", "delaying shutdown");
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.run();
        }
        utility.makeToast(444);
        Log.e("...", "....");
    }

    void senddSMS() {
        SmsManager smsManager = SmsManager.getDefault();
        Intent intent = new Intent("ssss");
        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0,
                intent, 0);
        context.registerReceiver(
                new ServiceShutDown(),
                new IntentFilter("ssss"));
        smsManager.sendTextMessage("01816692597", null, "Here goes the content of the SMS", sentIntent, null);

    }
}

