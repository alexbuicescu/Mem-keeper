package example.com.memkeeper.Utils;

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
}
