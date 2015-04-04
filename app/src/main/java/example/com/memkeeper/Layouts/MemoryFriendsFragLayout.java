package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import example.com.memkeeper.Adapters.CommentsListItemAdapter;
import example.com.memkeeper.Adapters.MemoryFriendsListItemAdapter;
import example.com.memkeeper.Database.DatabaseHelper;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.MemoriesUtils;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class MemoryFriendsFragLayout extends BaseFragment {

	public interface OnFriendsFragmentListener {
//		public void onPhotoClicked(int position);
	}

    private Activity context;

	private OnFriendsFragmentListener listener;
	private View view;
    private ListView friendsListView;
    private TextView noFriendsTextView;
    private MemoryFriendsListItemAdapter friendsListItemAdapter;

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
		view = inflater.inflate(R.layout.memory_friends_fragment, container, false);
		initAll();
        initTopBar();

		return view;
	}

	private void initAll()
	{
        try {
            friendsListView = (ListView) view.findViewById(R.id.memory_friends_fragment_list_view);
            noFriendsTextView = (TextView) view.findViewById(R.id.memory_friends_fragment_no_friends_text_view);
            noFriendsTextView.setVisibility(View.GONE);

            updateView();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initTopBar()
    {
        ViewGroup topBarLayout = (ViewGroup) view.findViewById(R.id.memory_friends_fragment_topbar);
        RelativeLayout backButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_new_memory_back_container);
        RelativeLayout doneButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_new_memory_done_container);
        doneButtonLayout.setVisibility(View.GONE);
        TextView topBarTitleTextView = (TextView) topBarLayout.findViewById(R.id.topbar_new_memory_title_text_view);
        topBarTitleTextView.setText("Friends");

        backButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }


    private void updateView()
    {
        if(friendsListItemAdapter != null)
        {
            friendsListItemAdapter.setItems(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getFriends());
            friendsListView.setAdapter(friendsListItemAdapter);
            friendsListItemAdapter.notifyDataSetChanged();
        }
        else
        {
            friendsListItemAdapter = new MemoryFriendsListItemAdapter(context,
                    MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getFriends());
            friendsListView.setAdapter(friendsListItemAdapter);
        }
        if(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getFriends() == null ||
                MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getFriends().size() == 0)
        {
            noFriendsTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            noFriendsTextView.setVisibility(View.GONE);
        }
    }
}
