package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import example.com.memkeeper.Adapters.MemoryGridItemAdapter;
import example.com.memkeeper.Database.DatabaseHelper;
import example.com.memkeeper.POJO.Memory;
import example.com.memkeeper.POJO.Photo;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.MemoriesUtils;
import example.com.memkeeper.Utils.PhotoUtils;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class MemoryPhotosFragLayout extends BaseFragment {

	public interface OnMemoryPhotoFragmentListener {
		public void onPhotoClicked(int position);
		public void onEditClicked();
		public void onShareClicked();
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

            Button seeMap = (Button) view.findViewById(R.id.memory_fragment_see_map_button);
            seeMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float longitude = 0;
                    float latitude = 0;
                    for(Photo photo : PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList())
                    {
                        if(photo.getLongitude() != null)
                        {
                            longitude = Float.parseFloat(photo.getLongitude());
                            latitude = Float.parseFloat(photo.getLatitude());
                            Log.i("found", "longitude" + longitude + " " + latitude);
//                            break;
                        }
                    }
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    context.startActivity(intent);
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
        RelativeLayout shareButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_memory_share_container);
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

        shareButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShareClicked();
            }
        });
    }

    public void refresh()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        memoryGridItemAdapter.setMemory(dbHelper.getMemory(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getId()));
        memoryGridItemAdapter.notifyDataSetChanged();
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
