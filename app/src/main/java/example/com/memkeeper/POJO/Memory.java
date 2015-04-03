package example.com.memkeeper.POJO;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class Memory {
    private String name;
    private Bitmap coverImage;
    private int id;
    private List<Bitmap> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Bitmap coverImage) {
        this.coverImage = coverImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Bitmap> getImages() {
        return images;
    }

    public void setImages(List<Bitmap> images) {
        this.images = images;
    }
}
