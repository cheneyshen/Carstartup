package com.example.fshen.carloudy;

/**
 * Created by fshen on 17/2/25.
 */
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MyIntentService extends IntentService {

    public final static String TAG = "SAVE_MESSAGE";
    public final static String TITLE = "TITLE";
    public final static String CONTENT = "CONTENT";
    private SqliteController controller = new SqliteController(this);
    //Initialize a handler on the main thread.
    public Handler mHandler;
    public MyIntentService() {
        super(TAG);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(getMainLooper());
        Log.i(TAG, "onCreate");
        // TODO: Actions to perform when service is created.
    }
    @Override
    protected void onHandleIntent(final Intent intent) {
        // This handler occurs on a background thread.
        // TODO The time consuming task should be implemented here.
        // Each Intent supplied to this IntentService will be
        // processed consecutively here. When all incoming Intents
        // have been processed the Service will terminate itself.

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                message newmsg = (message) intent.getSerializableExtra("NewMessage");
                if(newmsg!=null) {
                    long dbstatus=controller.insertmsg(newmsg.getTitle(), newmsg.getContent(), newmsg.getTime(), newmsg.getLatitude(), newmsg.getLongitude());

                    Toast.makeText(MyIntentService.this, String.valueOf(dbstatus), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, newmsg.getContent(), Toast.LENGTH_SHORT).show();
                    if(dbstatus==-1)
                    {
                        //Toast.makeText(context, "User already Exists", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Toast.makeText(context, "Message Registered Successfully", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
