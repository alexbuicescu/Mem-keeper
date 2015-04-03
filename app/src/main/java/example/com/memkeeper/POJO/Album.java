package example.com.memkeeper.POJO;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class Album {
    private String name;
    private String path;
    private Bitmap thumbnail;
    private List<Photo> photosList;

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

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Photo> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(List<Photo> photosList) {
        this.photosList = photosList;
    }
}
