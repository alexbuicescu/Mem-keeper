package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import example.com.memkeeper.Activities.NewMemoryActivity;
import example.com.memkeeper.Adapters.FriendsListItemAdapter;
import example.com.memkeeper.POJO.Photo;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.FriendsUtils;
import example.com.memkeeper.Utils.PhotoUtils;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class FriendsFragLayout extends BaseFragment {

	public interface OnFriendsFragmentListener {
		public void onFriendClicked(int position);
	}

    private Activity context;

	private OnFriendsFragmentListener listener;
	private View view;
    private LayoutInflater inflater;
    private ListView friendsListView;
    private FriendsListItemAdapter friendsListItemAdapter;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if (activity instanceof OnFriendsFragmentListener)
		{
			listener = (OnFriendsFragmentListener) activity;
		} else
		{
			throw new ClassCastException(activity.toString() + " must implemenet OnFriendsFragmentListener");
		}
        context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        this.inflater = inflater;
		view = inflater.inflate(R.layout.friends_fragment, container, false);
		initAll();
//        initTopBar();

		return view;
	}

	private void initAll()
	{
        try {
            updateView();
        }
        catch (Exception e)
        {
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

    private void updateView() {
        friendsListView = (ListView) view.findViewById(R.id.friends_fragment_list_view);
        friendsListItemAdapter = new FriendsListItemAdapter(getActivity(), FriendsUtils.getFriendList());
        friendsListView.setAdapter(friendsListItemAdapter);
    }
}
