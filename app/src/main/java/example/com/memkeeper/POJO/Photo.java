package example.com.memkeeper.POJO;

import android.graphics.Bitmap;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class Photo {
    private String name;
    private String path;
    private Bitmap bitmap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
