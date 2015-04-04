package example.com.memkeeper.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import example.com.memkeeper.Layouts.AlbumsFragLayout;
import example.com.memkeeper.Layouts.NewMemoryFragLayout;
import example.com.memkeeper.Layouts.PhotosFragLayout;
import example.com.memkeeper.POJO.Photo;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.PhotoUtils;


public class NewMemoryActivity extends ActionBarActivity implements NewMemoryFragLayout.OnNewMemoryFragmentListener,
        AlbumsFragLayout.OnAlbumsFragmentListener,
        PhotosFragLayout.OnPhotosFragmentListener  {

    NewMemoryFragLayout layoutNewMemory;
    AlbumsFragLayout layoutAlbums;
    PhotosFragLayout layoutPhotos;
    boolean isInAlbum;
    public static boolean selectCover;
    Photo lastCoverPhoto;
    Photo firstCoverPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holder_frag_gallery);

        getSupportActionBar().hide();

        isInAlbum = false;
        selectCover = false;
        layoutNewMemory = new NewMemoryFragLayout();//(NewMemoryFragLayout) getSupportFragmentManager().findFragmentById(R.id.new_memory_fragment);
//        layoutAlbums = new AlbumsFragLayout(); //(AlbumsFragLayout) getSupportFragmentManager().findFragmentById(R.id.albums_fragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, layoutNewMemory).commit();
        onContentChanged();
    }

    @Override
    public void onBackPressed()
    {
        if(!isInAlbum)
        {
            Log.i("back", "back");
            if(!selectCover)
            {
                layoutNewMemory.updatePhotos();
            }
            else
            {
                if(lastCoverPhoto != null)
                {
                    lastCoverPhoto.setCover(false);
                }
                if(firstCoverPhoto != null)
                {
                    firstCoverPhoto.setCover(false);
                }
//                if(firstCoverPhoto == null)
//                {
//                    if(lastCoverPhoto != null)
//                    {
//                        firstCoverPhoto = lastCoverPhoto;
//                    }
//                }
//                if(firstCoverPhoto != null)
//                {
//                    layoutNewMemory.initCover(firstCoverPhoto);
//                }
            }
            getSupportFragmentManager().popBackStack();
            super.onBackPressed();
        }
        else
        {
//            getSupportFragmentManager().popBackStack();

//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, layoutAlbums)
//                    .commitAllowingStateLoss();
            isInAlbum = false;
        }
        onTopBarBackClick();
        onTopBarBackClickPhotos();
        if(lastCoverPhoto != null)
        {
            lastCoverPhoto.setCover(false);
        }
        if(firstCoverPhoto != null)
        {
            firstCoverPhoto.setCover(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAlbumClicked(int position) {

        PhotoUtils.setCurrentAlbum(position);

        layoutPhotos = new PhotosFragLayout();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, layoutPhotos)
                .addToBackStack("some name")
                .commit();
        isInAlbum = true;
    }

    @Override
    public void onTopBarBackClick() {
        try {
            getSupportFragmentManager().popBackStack();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        isInAlbum = false;
        if(layoutAlbums != null)
        {
            layoutAlbums.refresh();
            PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).setNrSelectedPhotos(0);
            if(PhotoUtils.getSelectedPhotos() != null)
            {
                PhotoUtils.getSelectedPhotos().clear();
            }
        }
    }

    @Override
    public void onTopBarDoneClick() {
        isInAlbum = false;
        getSupportFragmentManager().popBackStack();
        layoutNewMemory.updatePhotos();
    }

    @Override
    public void onTopBarDoneClickPhotos() {

        isInAlbum = false;
        getSupportFragmentManager().popBackStack();
        layoutAlbums.refresh();
        if(selectCover) {
            layoutNewMemory.initCover(lastCoverPhoto);
            firstCoverPhoto = lastCoverPhoto;
        }
    }

    @Override
    public void onPhotoClicked(int position) {

        if(!selectCover) {
            boolean isSelected = PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position).isSelected();
            PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position).setSelected(!isSelected);

            if (PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position).isSelected()) {
                PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).setNrSelectedPhotos(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getNrSelectedPhotos() + 1);
                PhotoUtils.addSelectedPhotos(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position));
            } else {
                PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).setNrSelectedPhotos(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getNrSelectedPhotos() - 1);
                PhotoUtils.removeSelectedPhotos(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position));
            }
        }
        else
        {
            if(lastCoverPhoto == null)
            {
                lastCoverPhoto = PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position);
                lastCoverPhoto.setCover(true);
            }
            else
            {
                lastCoverPhoto.setCover(false);
                lastCoverPhoto = PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position);
                lastCoverPhoto.setCover(true);
            }
//            layoutNewMemory.initCover(lastCoverPhoto);
        }
    }

    @Override
    public void onTopBarBackClickPhotos() {

        isInAlbum = false;
//        getSupportFragmentManager().popBackStack();
        if(layoutAlbums != null) {
            layoutAlbums.refresh();

            for (Photo photo : PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList()) {
                photo.setSelected(false);
            }
            PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).setNrSelectedPhotos(0);
            if (PhotoUtils.getSelectedPhotos() != null) {
                PhotoUtils.getSelectedPhotos().clear();
            }

            if (selectCover) {
                if (lastCoverPhoto != null) {
                    lastCoverPhoto.setCover(false);
                    firstCoverPhoto.setCover(true);
                    lastCoverPhoto = firstCoverPhoto;
                }
            }
        }
//        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onAddPhotosClicked() {

        selectCover = false;
        layoutAlbums = new AlbumsFragLayout();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, layoutAlbums)
                .addToBackStack("some name")
                .commit();
        isInAlbum = false;

//        Intent myIntent = new Intent(NewMemoryActivity.this, AddPhotosActivity.class);
//        this.startActivity(myIntent);
    }

    @Override
    public void onChangeCoverClicked() {

        selectCover = true;
        layoutAlbums = new AlbumsFragLayout();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, layoutAlbums)
                .addToBackStack("some name")
                .commit();
        isInAlbum = false;
        if(lastCoverPhoto != null)
        {
            lastCoverPhoto.setCover(true);
            firstCoverPhoto = lastCoverPhoto;
        }
    }

    @Override
    public void onSaveClicked() {

    }
}
