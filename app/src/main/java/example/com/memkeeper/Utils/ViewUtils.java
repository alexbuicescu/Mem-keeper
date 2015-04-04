package example.com.memkeeper.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Alexandru on 04-Apr-15.
 */
public class ViewUtils {
    public static float calculateDpToPixel(float dp, Context context) {

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;

    }

    public static void launchRingDialog(final Context context, final Runnable runnable) {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(context, "Please wait ...", "I'm thinking ...", true);
        ringProgressDialog.setCancelable(false);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                runnable.run();
                ringProgressDialog.dismiss();
            }
        });
        th.start();

    }

}
