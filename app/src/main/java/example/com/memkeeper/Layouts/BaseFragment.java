package example.com.memkeeper.Layouts;

import android.support.v4.app.Fragment;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class BaseFragment extends Fragment {

    private String topbarTitle;

    public boolean onBackPressed()
    {
        return true;
    }

    public String getTopbarTitle() {
        return topbarTitle;
    }

    public void setTopbarTitle(String topbarTitle) {
        this.topbarTitle = topbarTitle;
    }
}
