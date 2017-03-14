package com.example.fshen.carloudy;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.Random;

import static java.security.AccessController.getContext;

/**
 * Created by fshen on 17/2/25.
 */
public class NLService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;
    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.i(TAG, "onListenerConnected()");

    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Log.i(TAG,"**********  onNotificationPosted");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new  Intent("NOTIFICATION_LISTENER_EXAMPLE");
        Notification notification = sbn.getNotification();

        Bundle extras = notification.extras;
        if (extras != null) {
            // 获取通知标题
            final message newmsg=new message();
            String title = extras.getString(Notification.EXTRA_TITLE, "");
            newmsg.setTitle(title);
            // 获取通知内容
            String content = extras.getString(Notification.EXTRA_TEXT, "");
            newmsg.setContent(content);
            String time = String.valueOf(System.currentTimeMillis());
            newmsg.setTime(time);
            Random rn=new Random();
            String lat=String.valueOf(41.838598 + rn.nextInt()%10);
            newmsg.setLatitude(lat);
            String lot=String.valueOf(-87.627383 + rn.nextInt()%10);
            newmsg.setLongitude(lot);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("NewMessage", newmsg);
            i.putExtras(mBundle);
            //i.putExtra("notification_event",newmsg.getTitle()+ "\n"+ newmsg.getContent()+"\n" + newmsg.getTime()+"\n" + lat+ "\n"+lot);
            sendBroadcast(i);
            Intent mapIntent = new Intent(this, MapsActivity.class);
            mapIntent.putExtras(mBundle);
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mapIntent);
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
        //Intent i = new  Intent("NOTIFICATION_LISTENER_EXAMPLE");
        //i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");

        //sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
        }
    }

}
