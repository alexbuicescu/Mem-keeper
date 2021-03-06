package example.com.memkeeper.POJO;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class Album {
    private String name;
    private String path;
    private Bitmap thumbnail;
    private List<Photo> photosList;
    private int nrSelectedPhotos;

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
        if(photosList != null && !photosList.isEmpty())
        {
            return photosList.get(0).getThumbnail();
        }
        return null;
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

    public int getNrSelectedPhotos() {
        return nrSelectedPhotos;
    }

    public void setNrSelectedPhotos(int nrSelectedPhotos) {
        this.nrSelectedPhotos = nrSelectedPhotos;
    }

    public void addPhoto(Photo photo)
    {
        if(photosList == null)
        {
            photosList = new ArrayList<>();
        }
        photosList.add(photo);
    }
}
