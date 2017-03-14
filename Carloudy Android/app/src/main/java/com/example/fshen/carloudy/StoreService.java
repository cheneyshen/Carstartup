package com.example.fshen.carloudy;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fshen on 17/2/25.
 */
public class StoreService extends Service {

    public final static  String TAG = "StoreService";
    public static int value;
    public final static String vString = "0";
    private static final int NOTIFICATION_REF = 1;

    private final IBinder binder = new MyBinder();
    @Override
    public void onCreate() {
        // TODO: Actions to perform when service is created.
        Log.i(TAG, "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Replace with service binding implementation.
        Log.i(TAG, "onBind");
        return binder;
    }

    public class MyBinder extends Binder {
        StoreService getService() {
            return StoreService.this;
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
    }
    /**
     * Listing 9-3: Overriding Service restart behavior
     */
    public int onStartCommand(Intent intent) {
        startBackgroundTask(intent);
        return Service.START_STICKY;
    }

    private void  startBackgroundTask(Intent intent) {
        // Start a background thread and begin the processing.
        backgroundExecution(intent);
    }

    /**
     * Listing 9-14: Moving processing to a background Thread
     */
    //This method is called on the main GUI thread.
    private void backgroundExecution(Intent intent) {
        // This moves the time consuming operation to a child thread.
        Thread thread = new Thread(null, doBackgroundThreadProcessing,
                "Background");
        thread.start();
    }

    //Runnable that executes the background processing method.
    private Runnable doBackgroundThreadProcessing = new Runnable() {
        public void run() {
            backgroundThreadProcessing();
        }
    };

    //Method which does some processing in the background.
    private void backgroundThreadProcessing() {
        // [ ... Time consuming operations ... ]

        // start timer task
        handler.post(doSavingMsg);

    }

    //Initialize a handler on the main thread.
    private Handler handler = new Handler();

    // Runnable that executes the updateGUI method.
    private Runnable doSavingMsg = new Runnable() {
        public void run() {
            saveMSG();
        }
    };
    private void saveMSG() {
        Toast.makeText(getApplicationContext(), "In saveMSG", Toast.LENGTH_SHORT).show();
        return;
    }
    /**
     * do some action
     */

}