package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import example.com.memkeeper.Activities.MainActivity;
import example.com.memkeeper.Adapters.FriendsListItemAdapter;
import example.com.memkeeper.Adapters.MemoriesListItemAdapter;
import example.com.memkeeper.Database.DatabaseHelper;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.FriendsUtils;
import example.com.memkeeper.Utils.MemoriesUtils;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class MemoryLaneFragLayout extends BaseFragment {

    public interface OnMemoryLaneFragmentListener {
        public void onMemoryClicked(int position);

        public void onAddNewMemoryClicked();
    }

    private Activity context;

    private OnMemoryLaneFragmentListener listener;
    private View view;
    private LayoutInflater inflater;
    private ListView memoriesListView;
    private MemoriesListItemAdapter memoriesListItemAdapter;
    private long lastScrollY;
    private long initialPositionAddNewButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnMemoryLaneFragmentListener) {
            listener = (OnMemoryLaneFragmentListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implemenet OnMemoryLaneFragmentListener");
        }
        context = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        view = inflater.inflate(R.layout.memory_lane_fragment, container, false);
        initAll();
//        initTopBar();

        return view;
    }

    private void initAll() {
        try {
            updateView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void initTopBar()
//    {
//        ViewGroup topBarLayout = (ViewGroup) view.findViewById(R.id.memory_lane_fragment_topbar_layout);
//        RelativeLayout backButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_main_back_container);
//
//        backButtonLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ((NewMemoryActivity) context).onBackPressed();
////                hideKeyboard();
////                listener.onTopBarBackClick();
//            }
//        });
//
//    }

    public void refresh() {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
//        MemoriesUtils.setMemoryList(dbHelper.getAllMemories());
        if (MainActivity.currentYear != null && !MainActivity.currentYear.equals("")) {
            MemoriesUtils.setMemoryList(dbHelper.getAllMemoriesYear(MainActivity.currentYear));
        } else {
            if (MainActivity.friend != null && !MainActivity.friend.equals("")) {
                MemoriesUtils.setMemoryList(dbHelper.getAllMemoriesFriend(MainActivity.friend));
            } else {
                MemoriesUtils.setMemoryList(dbHelper.getAllMemories());
            }
        }
        memoriesListItemAdapter.setMemoriesList(MemoriesUtils.getMemoryList());
        memoriesListItemAdapter.notifyDataSetChanged();
        memoriesListView.setAdapter(memoriesListItemAdapter);
//        context.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                memoriesListItemAdapter.notifyDataSetChanged();
//            }
//        });
    }

    int lastDirection = 0;
    long lsatScroll = 0;

    private void updateView() {
        final ViewGroup addNewMemoryView = (ViewGroup) view.findViewById(R.id.main_activity_new_memory_button);
        addNewMemoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddNewMemoryClicked();
            }
        });
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        if (MainActivity.currentYear != null && !MainActivity.currentYear.equals("")) {
            MemoriesUtils.setMemoryList(dbHelper.getAllMemoriesYear(MainActivity.currentYear));
        } else {
            if (MainActivity.friend != null && !MainActivity.friend.equals("")) {
                MemoriesUtils.setMemoryList(dbHelper.getAllMemoriesFriend(MainActivity.friend));
            } else {
                MemoriesUtils.setMemoryList(dbHelper.getAllMemories());
            }
        }

            memoriesListView = (ListView) view.findViewById(R.id.main_activity_memories_list_view);
            memoriesListItemAdapter = new MemoriesListItemAdapter(getActivity(), MemoriesUtils.getMemoryList());
            memoriesListView.setAdapter(memoriesListItemAdapter);
            memoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onMemoryClicked(position);
                }
            });


            final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    initialPositionAddNewButton = (long) addNewMemoryView.getY();
                    return true;
                }
            };
            view.getViewTreeObserver().addOnPreDrawListener(preDrawListener);

            initialPositionAddNewButton = (long) addNewMemoryView.getY();
            memoriesListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    try {
                        View c = memoriesListView.getChildAt(0);
                        int scrolly = -c.getTop() + memoriesListView.getFirstVisiblePosition() * c.getHeight();
//                if(scrolly  > lastScrollY)
//                {
                        if(scrolly - lastScrollY != 0)
                        {
                            lastDirection = (int) (scrolly - lastScrollY);
                        }
                        lsatScroll = System.currentTimeMillis();

                        addNewMemoryView.setY((float) (addNewMemoryView.getY() + (scrolly - lastScrollY) / 6));
                        if (scrolly > lastScrollY &&
                                addNewMemoryView.getY() < initialPositionAddNewButton + addNewMemoryView.getHeight()) {
//                    addNewMemoryView.setY(addNewMemoryView.getY() + (scrolly - lastScrollY) / 2);
                        } else if (scrolly > lastScrollY &&
                                addNewMemoryView.getY() > initialPositionAddNewButton + addNewMemoryView.getHeight()) {
                            addNewMemoryView.setY(initialPositionAddNewButton + addNewMemoryView.getHeight());
                        } else if (scrolly < lastScrollY &&
                                addNewMemoryView.getY() > initialPositionAddNewButton) {
//                    addNewMemoryView.setY(addNewMemoryView.getY() + (scrolly - lastScrollY) / 2);
                        } else if (scrolly < lastScrollY &&
                                addNewMemoryView.getY() <= initialPositionAddNewButton) {
                            addNewMemoryView.setY(initialPositionAddNewButton);
                        }
//                }
//                else
//                if(scrolly  < lastScrollY)
//                {
//                    addNewMemoryView.setY(addNewMemoryView.getY() - scrolly + lastScrollY);
//                }
                        Log.i("scroll", scrolly + "");
                        lastScrollY = scrolly;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true)
                {
                    if(System.currentTimeMillis() - lsatScroll > 1000) {
                        if (lastDirection > 0) {


                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Animation animation = new TranslateAnimation(
                                            Animation.ABSOLUTE, 0,
                                            Animation.ABSOLUTE, 0,
                                            Animation.ABSOLUTE, 0,
                                            Animation.ABSOLUTE, (addNewMemoryView.getY() - initialPositionAddNewButton - addNewMemoryView.getHeight()) * -1);
//                                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_bottom_top);
                                    animation.setDuration(500);
                                    animation.setInterpolator(new AccelerateDecelerateInterpolator());
                                    animation.setFillAfter(true);
                                    addNewMemoryView.startAnimation(animation);
                                }
                            });
//                            addNewMemoryView.setY(initialPositionAddNewButton + addNewMemoryView.getHeight());
                            lastDirection = 0;
                        } else
                        if (lastDirection < 0) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Animation animation = new TranslateAnimation(
                                            Animation.ABSOLUTE, 0,
                                            Animation.ABSOLUTE, 0,
                                            Animation.ABSOLUTE, 0,
                                            Animation.ABSOLUTE, initialPositionAddNewButton - addNewMemoryView.getY());
//                                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_bottom_top);
                                    animation.setDuration(500);
                                    animation.setInterpolator(new AccelerateDecelerateInterpolator());
                                    animation.setFillAfter(true);
                                    addNewMemoryView.startAnimation(animation);
                                }
                            });
                            lastDirection = 0;
//                            addNewMemoryView.setY(initialPositionAddNewButton);
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        }
    }

