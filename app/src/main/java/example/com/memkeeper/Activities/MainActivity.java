package example.com.memkeeper.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.widget.LoginButton;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import example.com.memkeeper.Database.DatabaseHelper;
import example.com.memkeeper.Layouts.FriendsFragLayout;
import example.com.memkeeper.Layouts.MemoryLaneFragLayout;
import example.com.memkeeper.Layouts.NewMemoryFragLayout;
import example.com.memkeeper.MyReceiver;
import example.com.memkeeper.POJO.Friend;
import example.com.memkeeper.POJO.Memory;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.FriendsUtils;
import example.com.memkeeper.Utils.MemoriesUtils;
import example.com.memkeeper.Utils.PhotoUtils;
import example.com.memkeeper.Utils.ViewUtils;
import example.com.memkeeper.facebook.FacebookLogInButton;
import example.com.memkeeper.facebook.FacebookShareButton;


public class MainActivity extends ActionBarActivity implements MemoryLaneFragLayout.OnMemoryLaneFragmentListener,
                FriendsFragLayout.OnFriendsFragmentListener{

//    MyReceiver myReceiver = null;
    Boolean myReceiverIsRegistered = false;

    public static FacebookLogInButton fbLoginButton;
    public static FacebookShareButton fbShareButton;

    private SlidingMenu mainMenu;
    private TextView topBarTitleTextView;

    public static String currentYear = "";
    public static String friend = "";

    private FriendsFragLayout layoutFriends;
    private MemoryLaneFragLayout layoutMemoryLane;

    Button lastSelectedViewInMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        layoutMemoryLane = new MemoryLaneFragLayout();//(NewMemoryFragLayout) getSupportFragmentManager().findFragmentById(R.id.new_memory_fragment);
//        layoutAlbums = new AlbumsFragLayout(); //(AlbumsFragLayout) getSupportFragmentManager().findFragmentById(R.id.albums_fragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_fragment_container, layoutMemoryLane).commit();
        onContentChanged();

        initLayout();
        initSlidingMenu();
        initTopBar();
        fbShareButton = new FacebookShareButton(this, savedInstanceState);
        fbLoginButton = new FacebookLogInButton(this, fbShareButton, new LoginButton(this));
        fbLoginButton.onCreate(savedInstanceState);
        fbShareButton.onCreate(savedInstanceState);
//        myReceiver = new MyReceiver();
//        IntentFilter filter = new IntentFilter("example.com.memkeeper.main");
//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
////                String value =  intent.getExtras().getString("value");
//                layoutMemoryLane.refresh();
//            }
//        };
//        registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i("onrsume", "main");
        layoutMemoryLane.refresh();
        fbLoginButton.onResume();
        fbShareButton.onResume();
//        if (!myReceiverIsRegistered) {
//            registerReceiver(myReceiver, new IntentFilter("example.com.memkeeper"));
//            myReceiverIsRegistered = true;
//        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        fbLoginButton.onPause();
        fbShareButton.onPause();
//        if (myReceiverIsRegistered) {
//            unregisterReceiver(myReceiver);
//            myReceiverIsRegistered = false;
//        }
    }

    private void initLayout()
    {
//        final Activity activity = this;
//
//        Button showGalleryButton = (Button) findViewById(R.id.main_activity_show_gallery_button);
//        showGalleryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(MainActivity.this, MemoryActivity.class);
//                activity.startActivity(myIntent);
//            }
//        });
//
//        RelativeLayout addNewMemory = (RelativeLayout) findViewById(R.id.main_activity_new_memory_button);
//        addNewMemory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(MainActivity.this, NewMemoryActivity.class);
//                activity.startActivity(myIntent);
//            }
//        });

        PhotoUtils.queryPhotos(this);
        FriendsUtils.fetchContacts(this);
    }

    private void initTopBar()
    {
        ViewGroup topBarLayout = (ViewGroup) findViewById(R.id.activity_main_topbar_layout);
        RelativeLayout backButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_main_back_container);
        topBarTitleTextView = (TextView) topBarLayout.findViewById(R.id.topbar_main_back_text_view);

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

        initSlidingMenuButtons();
    }

    private void initSlidingMenuButtons() {

        final Button menuFriends = (Button) mainMenu.findViewById(R.id.menu_see_your_friends_button);
        menuFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lastSelectedViewInMenu != null)
                {
                    lastSelectedViewInMenu.setTextColor(getResources().getColor(R.color.gray_light));
                    lastSelectedViewInMenu.setTypeface(null, Typeface.NORMAL);
                }
                lastSelectedViewInMenu = menuFriends;
                lastSelectedViewInMenu.setTextColor(getResources().getColor(R.color.transparent_blue_pressed));
                lastSelectedViewInMenu.setTypeface(null, Typeface.BOLD);
                topBarTitleTextView.setText("Your friends");

                currentYear = "";
                friend = "";
                layoutFriends = new FriendsFragLayout();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment_container, layoutFriends)
                        .commit();
                mainMenu.toggle();
            }
        });
        final Button menuMemoryLane = (Button) mainMenu.findViewById(R.id.menu_currentSelection_button);
        menuMemoryLane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(lastSelectedViewInMenu != null)
                {
                    lastSelectedViewInMenu.setTextColor(getResources().getColor(R.color.darker_gray));
                    lastSelectedViewInMenu.setTypeface(null, Typeface.NORMAL);
                }
                lastSelectedViewInMenu = null;
                topBarTitleTextView.setText("Memory Lane");
//                lastSelectedViewInMenu.setTextColor(getResources().getColor(R.color.transparent_blue_pressed));
//                lastSelectedViewInMenu.setTypeface(null, Typeface.BOLD);

                currentYear = "";
                friend = "";
                layoutMemoryLane = new MemoryLaneFragLayout();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment_container, layoutMemoryLane)
                        .commit();
                mainMenu.toggle();
            }
        });

        final DatabaseHelper dbHelper = new DatabaseHelper(this);
        MemoriesUtils.setMemoryList(dbHelper.getAllMemories());

        List<String> years = new ArrayList<>();
        for(Memory memory : MemoriesUtils.getMemoryList())
        {
            if(!years.contains(memory.getDate().substring(0, memory.getDate().indexOf('/'))))
            {
                years.add(memory.getDate().substring(0, memory.getDate().indexOf('/')));
            }
        }

        Log.i("year", years.size() + "");
        LinearLayout slidingWindowYearContainer = (LinearLayout) findViewById(R.id.menu_years_container);
        for(final String year : years)
        {
            final Button yearButton = new Button(this);
            yearButton.setText(year);
            yearButton.setTextColor(getResources().getColor(R.color.darker_gray));
            yearButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            yearButton.setBackgroundColor(Color.TRANSPARENT);
            slidingWindowYearContainer.addView(yearButton);
            yearButton.setPadding((int) ViewUtils.calculateDpToPixel(30, this), 0, 0, 0);
            yearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentYear = year;
                    friend = "";

//                    MemoriesUtils.setMemoryList(dbHelper.getAllMemories());
//                    Iterator iterator = MemoriesUtils.getMemoryList().iterator();
//                    while(iterator.hasNext())
//                    {
//                        Memory memory = (Memory) iterator.next();
//                        if(!memory.getDate().substring(0, memory.getDate().indexOf("/")).equals(currentYear))
//                        {
//                            iterator.remove();
//                        }
//                    }

                    if(lastSelectedViewInMenu != null)
                    {
                        lastSelectedViewInMenu.setTextColor(getResources().getColor(R.color.darker_gray));
                        lastSelectedViewInMenu.setTypeface(null, Typeface.NORMAL);
                    }
                    lastSelectedViewInMenu = yearButton;
                    topBarTitleTextView.setText("Memory Lane");
                    lastSelectedViewInMenu.setTextColor(getResources().getColor(R.color.transparent_blue_pressed));
                    lastSelectedViewInMenu.setTypeface(null, Typeface.BOLD);
//                lastSelectedViewInMenu.setTextColor(getResources().getColor(R.color.transparent_blue_pressed));
//                lastSelectedViewInMenu.setTypeface(null, Typeface.BOLD);

                    layoutMemoryLane = new MemoryLaneFragLayout();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_fragment_container, layoutMemoryLane)
                            .commit();
                    mainMenu.toggle();
                }
            });
            Log.i("year", year);
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
    public void onFriendClicked(int position) {
        friend = FriendsUtils.getFriendList().get(position).getName();

        Log.i("clicked", "friend");

        if(lastSelectedViewInMenu != null)
        {
            lastSelectedViewInMenu.setTextColor(getResources().getColor(R.color.darker_gray));
            lastSelectedViewInMenu.setTypeface(null, Typeface.NORMAL);
        }
        lastSelectedViewInMenu = null;
        topBarTitleTextView.setText("Memory Lane");
//                lastSelectedViewInMenu.setTextColor(getResources().getColor(R.color.transparent_blue_pressed));
//                lastSelectedViewInMenu.setTypeface(null, Typeface.BOLD);

        layoutMemoryLane = new MemoryLaneFragLayout();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, layoutMemoryLane)
                .commit();
//        mainMenu.toggle();
    }

    @Override
    public void onMemoryClicked(int position) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        MemoriesUtils.setCurrentMemory(position);
//        Log.i("memory4", MemoriesUtils.getMemoryList().get(position).getImagesPaths().size() + "");
        MemoriesUtils.getMemoryList().set(position, dbHelper.getMemory(MemoriesUtils.getMemoryList().get(position).getId()));
        Intent myIntent = new Intent(MainActivity.this, MemoryActivity.class);
        this.startActivity(myIntent);
    }

    @Override
    public void onAddNewMemoryClicked() {
        Intent myIntent = new Intent(MainActivity.this, NewMemoryActivity.class);
        myIntent.putExtra("edit", 0);
        this.startActivity(myIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        fbLoginButton.onActivityResult(requestCode, resultCode, data);
        fbShareButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        fbLoginButton.onSaveInstanceState(outState);
        fbShareButton.onSaveInstanceState(outState);
    }
}
