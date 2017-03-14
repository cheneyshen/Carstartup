package com.example.fshen.carloudy;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.TextView;
import com.example.fshen.carloudy.MyIntentService;

import java.util.Random;

public class MainActivity extends Activity {
    private message newmsg;
    private TextView txtView;
    private NotificationReceiver nReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView = (TextView) findViewById(R.id.textView);
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver,filter);

        //Intent intent = new Intent(this, MyIntentService.class);
        //intent.putExtra(MyIntentService.TITLE, "T1");
        //intent.putExtra(MyIntentService.CONTENT, "MSG1");
        //startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    public void buttonClicked(View v){

        if(v.getId() == R.id.btnCreateNotify){
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            Random rn=new Random();
            ncomp.setContentTitle("Text Notification "+ Math.abs(rn.nextInt()%100));
            ncomp.setContentText("Notification Content "+ Math.abs(rn.nextInt()%100));
            ncomp.setWhen(System.currentTimeMillis());
            ncomp.setTicker("Notification Listener Service Example");
            ncomp.setSmallIcon(R.mipmap.ic_launcher);
            ncomp.setAutoCancel(true);
            nManager.notify((int)System.currentTimeMillis(),ncomp.build());
            Intent i = new Intent("NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            i.putExtra("command","list");
            sendBroadcast(i);
        }
        else if(v.getId() == R.id.btnClearNotify){
            Intent i = new Intent("NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            i.putExtra("command","clearall");
            sendBroadcast(i);
        }
        else if(v.getId() ==  R.id.btnShowNotify) {

            Intent mapIntent = new Intent(this, MapsActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("NewMessage", newmsg);
            mapIntent.putExtras(mBundle);
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(mapIntent);

        }

    }

    class NotificationReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //String temp = intent.getStringExtra("notification_event") + "\n" + txtView.getText();
            //String temp = intent.getStringExtra("notification_event") + "\n";
            //txtView.setText(temp);
            newmsg = (message) intent.getSerializableExtra("NewMessage");
            txtView.setText(newmsg.getTitle()+ "\n" +newmsg.getContent()+ "\n" +newmsg.getTime()+ "\n");

            Intent intent1 = new Intent(context, MyIntentService.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("NewMessage", newmsg);
            intent1.putExtras(mBundle);
            startService(intent1);

        }
    }



}
