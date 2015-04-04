package example.com.memkeeper.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import example.com.memkeeper.Layouts.MemoryPhotosFragLayout;
import example.com.memkeeper.R;


public class MemoryActivity extends ActionBarActivity implements MemoryPhotosFragLayout.OnMemoryPhotoFragmentListener {

    MemoryPhotosFragLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holder_frag_memory);

        getSupportActionBar().hide();

        layout = (MemoryPhotosFragLayout) getSupportFragmentManager().findFragmentById(R.id.memory_fragment);
        onContentChanged();

//        IntentFilter filter = new IntentFilter("example.com.memkeeper.memory");
//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
////                String value =  intent.getExtras().getString("value");
//                layout.refresh();
//            }
//        };
//        registerReceiver(receiver, filter);
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
    public void onPhotoClicked(int position) {

        Intent myIntent = new Intent(MemoryActivity.this, PhotoActivity.class);
        this.startActivity(myIntent);
    }

    @Override
    public void onEditClicked() {

        Intent myIntent = new Intent(MemoryActivity.this, NewMemoryActivity.class);
        myIntent.putExtra("edit", 1);
        this.startActivity(myIntent);
    }
}
