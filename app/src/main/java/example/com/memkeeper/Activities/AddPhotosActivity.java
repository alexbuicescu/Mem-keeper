package example.com.memkeeper.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import example.com.memkeeper.Layouts.AlbumsFragLayout;
import example.com.memkeeper.Layouts.PhotosFragLayout;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.PhotoUtils;


public class AddPhotosActivity extends ActionBarActivity implements
        AlbumsFragLayout.OnAlbumsFragmentListener,
        PhotosFragLayout.OnPhotosFragmentListener {

    AlbumsFragLayout layoutAlbums;
    PhotosFragLayout layoutPhotos;
    boolean isInAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holder_frag_gallery);

        isInAlbum = false;
        layoutAlbums = new AlbumsFragLayout(); //(AlbumsFragLayout) getSupportFragmentManager().findFragmentById(R.id.albums_fragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, layoutAlbums).commit();
        onContentChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if(!isInAlbum)
        {
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

    }

    @Override
    public void onTopBarDoneClick() {

    }

    @Override
    public void onTopBarDoneClickPhotos() {

    }

    @Override
    public void onPhotoClicked(int position) {
        if(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position).isSelected())
        {
            PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).setNrSelectedPhotos(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getNrSelectedPhotos() + 1);
        }
        else
        {
            PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).setNrSelectedPhotos(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getNrSelectedPhotos() - 1);
        }
        layoutAlbums.refresh();
    }

    @Override
    public void onTopBarBackClickPhotos() {

    }
}
