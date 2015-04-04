package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import example.com.memkeeper.Activities.CommentsActivity;
import example.com.memkeeper.Activities.FriendsActivity;
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
            TextView nrCommentsTextView = (TextView) view.findViewById(R.id.memory_list_view_item_comments_nr_text_view);
            if(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getComments() != null)
            {
                nrCommentsTextView.setText(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getComments().size() + "");
            }
            else
            {
                nrCommentsTextView.setText("0");
            }
            TextView nrFriendsTextView = (TextView) view.findViewById(R.id.memory_list_view_item_friends_nr_text_view);
            if(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getFriends() != null)
            {
                nrFriendsTextView.setText(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getFriends().size() + "");
            }
            else
            {
                nrFriendsTextView.setText("0");
            }
            LinearLayout commentsLayout = (LinearLayout) view.findViewById(R.id.memory_list_view_item_comments_container);
            commentsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(context, CommentsActivity.class);
                    context.startActivity(myIntent);
                }
            });
            LinearLayout friendsLayout = (LinearLayout) view.findViewById(R.id.memory_list_view_item_friends_container);
            friendsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(context, FriendsActivity.class);
                    context.startActivity(myIntent);
                }
            });

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

    private void updateData()
    {
        TextView memoryNameTextView = (TextView) view.findViewById(R.id.memory_list_view_item_title_text_view);
        memoryNameTextView.setText(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getName());
        TextView memoryDateTextView = (TextView) view.findViewById(R.id.memory_list_view_item_date_text_view);
        memoryDateTextView.setText(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getDate());
        TextView memoryFromTextView = (TextView) view.findViewById(R.id.memory_list_view_item_from_text_view);
        memoryFromTextView.setText(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getLocationOne());
        TextView memoryToTextView = (TextView) view.findViewById(R.id.memory_list_view_item_to_text_view);
        memoryToTextView.setText(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getLocationTwo());
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
        updateData();
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
