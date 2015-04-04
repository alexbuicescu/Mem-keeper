package example.com.memkeeper.POJO;

import android.net.Uri;

/**
 * Created by Alexandru on 04-Apr-15.
 */
public class Friend {
    private String name;
    private Uri uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
