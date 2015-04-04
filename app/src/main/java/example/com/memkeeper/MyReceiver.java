package example.com.memkeeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Alexandru on 04-Apr-15.
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("intent", "detected");
//            Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
    }

}
