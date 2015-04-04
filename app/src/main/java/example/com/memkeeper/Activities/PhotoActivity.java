package example.com.memkeeper.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import example.com.memkeeper.Layouts.MemoryPhotosFragLayout;
import example.com.memkeeper.Layouts.PhotoFragLayout;
import example.com.memkeeper.R;


public class PhotoActivity extends ActionBarActivity implements PhotoFragLayout.OnPhotoFragmentListener{

    PhotoFragLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holder_frag_photo);

        getSupportActionBar().hide();

        layout = (PhotoFragLayout) getSupportFragmentManager().findFragmentById(R.id.photo_fragment);
        onContentChanged();
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

//    @Override
//    public void onPhotoClicked(int position) {
//
//    }
}
