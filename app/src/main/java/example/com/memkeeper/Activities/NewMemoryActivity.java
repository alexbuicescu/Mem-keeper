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
    boolean selectCover;
    Photo lastCoverPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holder_frag_gallery);

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
            if(!selectCover)
            {
                layoutNewMemory.updatePhotos();
            }
            else
            {
                if(lastCoverPhoto != null)
                {
                    lastCoverPhoto.setSelected(false);
                }
            }
            super.onBackPressed();
        }
        else
        {
            getSupportFragmentManager().popBackStack();
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, layoutAlbums)
//                    .commitAllowingStateLoss();
            isInAlbum = false;
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
    public void onPhotoClicked(int position) {

        if(!selectCover) {
            if (PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position).isSelected()) {
                PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).setNrSelectedPhotos(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getNrSelectedPhotos() + 1);
                PhotoUtils.addSelectedPhotos(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position));
            } else {
                PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).setNrSelectedPhotos(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getNrSelectedPhotos() - 1);
                PhotoUtils.removeSelectedPhotos(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position));
            }
            layoutAlbums.refresh();
        }
        else
        {
            if(lastCoverPhoto == null)
            {
                lastCoverPhoto = PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position);
                lastCoverPhoto.setSelected(true);
            }
            else
            {
                lastCoverPhoto.setSelected(false);
                lastCoverPhoto = PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position);
                lastCoverPhoto.setSelected(true);
            }
            layoutNewMemory.initCover(lastCoverPhoto);
        }
    }

    @Override
    public void onAddPhotosClicked() {

        layoutAlbums = new AlbumsFragLayout();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, layoutAlbums)
                .addToBackStack("some name")
                .commit();
        isInAlbum = false;
        selectCover = false;

//        Intent myIntent = new Intent(NewMemoryActivity.this, AddPhotosActivity.class);
//        this.startActivity(myIntent);
    }

    @Override
    public void onChangeCoverClicked() {

        layoutAlbums = new AlbumsFragLayout();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, layoutAlbums)
                .addToBackStack("some name")
                .commit();
        isInAlbum = false;
        selectCover = true;
        if(lastCoverPhoto != null)
        {
            lastCoverPhoto.setSelected(true);
        }
    }
}
