package example.com.memkeeper.POJO;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class Memory {
    private String name;
    private Bitmap coverImage;
    private String coverImagePath;
    private int id;
    private List<Photo> images;
    private List<String> imagesPaths;
    private List<String> longitude;
    private List<String> latitude;
    private List<String> friends;
    private List<String> comments;
    private String date;
    private String locationOne;
    private String locationTwo;

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

    public List<Photo> getImages() {
        return images;
    }

    public void setImages(List<Photo> images) {
        this.images = images;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public List<String> getImagesPaths() {
        return imagesPaths;
    }

    public void setImagesPaths(List<String> imagesPaths) {
        this.imagesPaths = imagesPaths;
    }

    public List<String> getLatitude() {
        return latitude;
    }

    public void setLatitude(List<String> latitude) {
        this.latitude = latitude;
    }

    public List<String> getLongitude() {
        return longitude;
    }

    public void setLongitude(List<String> longitude) {
        this.longitude = longitude;
    }

    public String getLocationOne() {
        return locationOne;
    }

    public void setLocationOne(String locationOne) {
        this.locationOne = locationOne;
    }

    public String getLocationTwo() {
        return locationTwo;
    }

    public void setLocationTwo(String locationTwo) {
        this.locationTwo = locationTwo;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public void addComment(String comment)
    {
        if(comments == null)
        {
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }
}
