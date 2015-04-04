package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import example.com.memkeeper.Adapters.MemoryGridItemAdapter;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.MemoriesUtils;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class MemoryPhotosFragLayout extends BaseFragment {

	public interface OnMemoryPhotoFragmentListener {
		public void onPhotoClicked(int position);
		public void onEditClicked();
	}

    private Activity context;

	private OnMemoryPhotoFragmentListener listener;
	private View view;
    protected GridView memoryPhotosGridView;

    private TextView noPhotosTextView;

    private MemoryGridItemAdapter memoryGridItemAdapter;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if (activity instanceof OnMemoryPhotoFragmentListener)
		{
			listener = (OnMemoryPhotoFragmentListener) activity;
		} else
		{
			throw new ClassCastException(activity.toString() + " must implemenet OnMemoryPhotoFragmentListener");
		}
        context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.memory_fragment, container, false);
		initAll();
        initTopBar();

		return view;
	}

	private void initAll()
	{
        try {
            memoryPhotosGridView = (GridView) view.findViewById(R.id.activity_memory_photos_grid_view);

            noPhotosTextView = (TextView) view.findViewById(R.id.activity_memory_no_photos_text_view);

            memoryPhotosGridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onPhotoClicked(position);
                }
            });

            updateView();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initTopBar()
    {
        ViewGroup topBarLayout = (ViewGroup) view.findViewById(R.id.memory_fragment_topbar);
        RelativeLayout backButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_memory_back_container);
        RelativeLayout editButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_memory_done_container);
        TextView topBarTitleTextView = (TextView) topBarLayout.findViewById(R.id.topbar_memory_title_text_view);
        topBarTitleTextView.setText(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getName());

        backButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        editButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditClicked();
            }
        });
    }


    private void updateView()
    {
        if (memoryGridItemAdapter == null)
        {
            memoryGridItemAdapter = new MemoryGridItemAdapter(context,
                    MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()));
            memoryPhotosGridView.setAdapter(memoryGridItemAdapter);
        } else
        {
            memoryGridItemAdapter.setMemory(
                    MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()));
        }

        if(memoryPhotosGridView != null) {
            if (MemoriesUtils.getMemoryList().size() == 0) {
                noPhotosTextView.setVisibility(View.VISIBLE);
            } else {
                noPhotosTextView.setVisibility(View.GONE);
            }
            memoryGridItemAdapter.notifyDataSetChanged();
        }
    }
}
