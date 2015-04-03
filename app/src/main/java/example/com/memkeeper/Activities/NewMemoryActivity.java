package example.com.memkeeper.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import example.com.memkeeper.Layouts.NewMemoryFragLayout;
import example.com.memkeeper.R;


public class NewMemoryActivity extends ActionBarActivity implements NewMemoryFragLayout.OnNewMemoryFragmentListener {

    NewMemoryFragLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holder_frag_new_memory);

        layout = (NewMemoryFragLayout) getSupportFragmentManager().findFragmentById(R.id.new_memory_fragment);
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

}
