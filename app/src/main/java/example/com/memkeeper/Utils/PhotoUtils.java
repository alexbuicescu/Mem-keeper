package example.com.memkeeper.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
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
    private static int currentAlbum;

    public static List<Album> getAlbums() {
        if(albums == null)
        {
            albums = new ArrayList<>();
        }
        loadAlbums();
        return albums;
    }

    public static void setAlbums(List<Album> albums) {
        PhotoUtils.albums = albums;
    }

    private static int calculateInSampleSize(

        BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public static void loadAlbums() {
        albums.clear();
        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/";

        File targetDirector = new File(targetPath);

        findFilesInDir(targetDirector);
    }

    private static boolean findFilesInDir(File targetDirector)
    {
        boolean foundPhoto = false;
        List<Photo> photos = new ArrayList<>();

        File[] files = targetDirector.listFiles();
        for (File file : files){
            if(file.isFile()) {
                if (file.getAbsolutePath().endsWith(".jpg") || file.getAbsolutePath().endsWith(".png")) {
                    Log.i("photos", file.getAbsolutePath());
                    foundPhoto = true;

                    Photo photo = new Photo();
                    photo.setName(file.getName());
                    photo.setPath(file.getAbsolutePath());

                    photos.add(photo);
                }
            }
            else
            {
                if(!file.getName().startsWith("."))
                {
                    findFilesInDir(new File(file.getAbsolutePath() + "/"));
                }
            }
        }
        if(!photos.isEmpty())
        {
            Album album = new Album();
            album.setName(targetDirector.getName());
            album.setPath(targetDirector.getAbsolutePath());
            album.setPhotosList(photos);
            album.setThumbnail(decodeSampledBitmapFromUri(photos.get(0).getPath(), 200, 200));
            albums.add(album);
        }
        return foundPhoto;
    }

    public static Album getCurrentAlbum() {
        return albums.get(currentAlbum);
    }

    public static void setCurrentAlbum(int currentAlbum) {
        for(int i = 0; i < albums.get(currentAlbum).getPhotosList().size(); i++)
        {
            String path = albums.get(currentAlbum).getPhotosList().get(i).getPath();
            albums.get(currentAlbum).getPhotosList().get(i).setThumbnail(decodeSampledBitmapFromUri(path, 200, 200));
        }
        PhotoUtils.currentAlbum = currentAlbum;
    }
}
