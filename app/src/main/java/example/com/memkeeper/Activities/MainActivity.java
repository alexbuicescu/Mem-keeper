package example.com.memkeeper.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import example.com.memkeeper.R;
import example.com.memkeeper.Utils.PhotoUtils;


public class MainActivity extends ActionBarActivity {

    private SlidingMenu mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        initLayout();
        initSlidingMenu();
        initTopBar();
    }

    private void initLayout()
    {
        final Activity activity = this;

        Button showGalleryButton = (Button) findViewById(R.id.main_activity_show_gallery_button);
        showGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, MemoryActivity.class);
                activity.startActivity(myIntent);
            }
        });

        RelativeLayout addNewMemory = (RelativeLayout) findViewById(R.id.main_activity_new_memory_button);
        addNewMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, NewMemoryActivity.class);
                activity.startActivity(myIntent);
            }
        });

        PhotoUtils.queryPhotos(this);
    }

    private void initTopBar()
    {
        ViewGroup topBarLayout = (ViewGroup) findViewById(R.id.activity_main_topbar_layout);
        RelativeLayout backButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_main_back_container);

        backButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenu.toggle();
            }
        });
    }


    private void initSlidingMenu()
    {
        mainMenu = new SlidingMenu(this);
        mainMenu.setMode(SlidingMenu.LEFT);
        mainMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        mainMenu.setShadowWidthRes(R.dimen.shadow_width);
        mainMenu.setShadowDrawable(R.drawable.shadow);
        mainMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mainMenu.setFadeDegree(0.35f);
        mainMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mainMenu.setMenu(R.layout.menu);

//        initSlidingMenuButtons();
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
