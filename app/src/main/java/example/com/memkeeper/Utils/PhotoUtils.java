package example.com.memkeeper.Utils;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import example.com.memkeeper.POJO.Album;
import example.com.memkeeper.POJO.Photo;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class PhotoUtils {
    private static List<Album> albums;
    private static List<Photo> selectedPhotos;
    private static int currentAlbum;

    public static List<Album> getAlbums() {
        if(albums == null)
        {
            albums = new ArrayList<>();
        }

//        loadAlbums();
        return albums;
    }

    public static void setAlbums(List<Album> albums) {
        PhotoUtils.albums = albums;
    }

    public static int getCurrentAlbum() {
        return currentAlbum;
    }

    public static void setCurrentAlbum(int currentAlbum) {
        PhotoUtils.currentAlbum = currentAlbum;
    }

    public static void queryPhotos(Activity activity)
    {
        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.PICASA_ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN
        };

        // Get the base URI for the People table in the Contacts content provider.
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        Cursor cur = activity.getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC"        // Ordering
        );

        Log.i("ListingImages"," query count="+cur.getCount());
        String lastBucketName = "";
        albums = new ArrayList<>();
        List<Photo> photos = new ArrayList<>();

        if (cur.moveToFirst()) {
            String bucket;
            String name;
            String date;
            String path;

            int idColumn = cur.getColumnIndex(
                    MediaStore.Images.Media._ID);

            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int displayNameColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DISPLAY_NAME);

            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);

            int photoIDColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.PICASA_ID);

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            do {
                // Get the field values
                int id = cur.getInt(idColumn);
                name = cur.getString(displayNameColumn);
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                path = cur.getString(dataColumn);

                if(!lastBucketName.equals("") && !bucket.equals(lastBucketName))
                {
                    Album album = new Album();
                    album.setName(lastBucketName);
                    album.setPhotosList(photos);
                    Log.i("album1", lastBucketName + " " + cur.getString(dataColumn));
                    albums.add(album);
                    photos = new ArrayList<>();
                }
//                else
                {
                    Photo photo = new Photo();
                    photo.setName(name);
                    int photoID = cur.getInt(photoIDColumn);
                    photo.setUri(Uri.withAppendedPath(
                            MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + photoID));

                    Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(activity.getContentResolver(),
                            id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                    photo.setThumbnail(bm);

                    photos.add(photo);
                }
                if(cur.isLast())
                {
                    Album album = new Album();
                    album.setName(bucket);
                    album.setPhotosList(photos);
                    Log.i("album2", bucket);
                    albums.add(album);
                }
                // Do something with the values.
//                Log.i("ListingImages", " bucket=" + bucket + "|" + name
//                        + "  date_taken=" + date);
                lastBucketName = bucket;
            } while (cur.moveToNext());

        }
    }

    public static List<Photo> getSelectedPhotos() {
        return selectedPhotos;
    }

    public static void setSelectedPhotos(List<Photo> selectedPhotos) {
        PhotoUtils.selectedPhotos = selectedPhotos;
    }

    public static void addSelectedPhotos(Photo photo)
    {
        if(PhotoUtils.selectedPhotos == null)
        {
            PhotoUtils.selectedPhotos = new ArrayList<>();
        }
        PhotoUtils.selectedPhotos.add(photo);
    }
    public static void removeSelectedPhotos(Photo photo)
    {
        if(PhotoUtils.selectedPhotos == null)
        {
            PhotoUtils.selectedPhotos = new ArrayList<>();
        }
        PhotoUtils.selectedPhotos.remove(photo);
    }
}
