package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

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

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if (activity instanceof OnMemoryLaneFragmentListener)
		{
			listener = (OnMemoryLaneFragmentListener) activity;
		} else
		{
			throw new ClassCastException(activity.toString() + " must implemenet OnMemoryLaneFragmentListener");
		}
        context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        this.inflater = inflater;
		view = inflater.inflate(R.layout.memory_lane_fragment, container, false);
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
        ViewGroup addNewMemoryView = (ViewGroup) view.findViewById(R.id.main_activity_new_memory_button);
        addNewMemoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddNewMemoryClicked();
            }
        });
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        MemoriesUtils.setMemoryList(dbHelper.getAllMemories());
        memoriesListView = (ListView) view.findViewById(R.id.main_activity_memories_list_view);
        memoriesListItemAdapter = new MemoriesListItemAdapter(getActivity(), MemoriesUtils.getMemoryList());
        memoriesListView.setAdapter(memoriesListItemAdapter);
        memoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onMemoryClicked(position);
            }
        });
    }
}
