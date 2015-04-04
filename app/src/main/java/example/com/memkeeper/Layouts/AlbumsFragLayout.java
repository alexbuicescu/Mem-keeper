package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import example.com.memkeeper.Activities.NewMemoryActivity;
import example.com.memkeeper.Adapters.AlbumsGridItemAdapter;
import example.com.memkeeper.Adapters.MemoryGridItemAdapter;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.MemoriesUtils;
import example.com.memkeeper.Utils.PhotoUtils;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class AlbumsFragLayout extends BaseFragment {

	public interface OnAlbumsFragmentListener {
		public void onAlbumClicked(int position);
        public void onTopBarBackClick();
        public void onTopBarDoneClick();
	}

    private Activity context;

	private OnAlbumsFragmentListener listener;
	private View view;
    protected GridView albumsGridView;

    private TextView noPhotosTextView;

    private AlbumsGridItemAdapter albumsGridItemAdapter;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if (activity instanceof OnAlbumsFragmentListener)
		{
			listener = (OnAlbumsFragmentListener) activity;
		} else
		{
			throw new ClassCastException(activity.toString() + " must implemenet OnAlbumsFragmentListener");
		}
        context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.albums_fragment, container, false);
		initAll();

		return view;
	}

	private void initAll()
	{
        try {
            albumsGridView = (GridView) view.findViewById(R.id.activity_albums_grid_view);

            noPhotosTextView = (TextView) view.findViewById(R.id.activity_albums_no_albums_text_view);

            albumsGridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onAlbumClicked(position);
                }
            });

            updateView();
            initTopBar();

//            for(int i = 0; i < 3; i++) {
//                Animation anim = AnimationUtils.loadAnimation(
//                        context, android.R.anim.slide_out_right
//                );
//                anim.setDuration(500);
////                albumsGridView.getChildAt(i).startAnimation(anim);
//            }
//            new Handler().postDelayed(new Runnable() {
//
//                public void run() {
//
//                    FavouritesManager.getInstance().remove(
//                            FavouritesManager.getInstance().getTripManagerAtIndex(index)
//                    );
//                    populateList();
//                    adapter.notifyDataSetChanged();
//
//                }
//
//            }, anim.getDuration());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initTopBar()
    {
        ViewGroup topBarLayout = (ViewGroup) view.findViewById(R.id.albums_fragment_top_bar);
        TextView backButtonTextView = (TextView) topBarLayout.findViewById(R.id.topbar_gallery_back_text_view);
        RelativeLayout backButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_gallery_back_container);
        RelativeLayout doneButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_gallery_done_container);

        backButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTopBarDoneClick();
//                listener.onTopBarBackClick();
            }
        });
        doneButtonLayout.setVisibility(View.GONE);

        doneButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTopBarDoneClick();
            }
        });

        if(NewMemoryActivity.selectCover)
        {
            backButtonTextView.setText("Choose cover memory");
        }
    }

    public void refresh()
    {
        if(albumsGridItemAdapter != null)
        {
            Log.i("update", "update");
            albumsGridItemAdapter.setAlbums(PhotoUtils.getAlbums());
            albumsGridItemAdapter.notifyDataSetChanged();
        }
    }

    public void updateView()
    {
        if (albumsGridItemAdapter == null)
        {
            albumsGridItemAdapter = new AlbumsGridItemAdapter(context,
                    PhotoUtils.getAlbums());
            albumsGridView.setAdapter(albumsGridItemAdapter);
        } else
        {
            albumsGridItemAdapter.setAlbums(PhotoUtils.getAlbums());
        }

        if(albumsGridView != null) {
            if (PhotoUtils.getAlbums().size() == 0) {
                noPhotosTextView.setVisibility(View.VISIBLE);
            } else {
                noPhotosTextView.setVisibility(View.GONE);
            }
            albumsGridItemAdapter.notifyDataSetChanged();
        }
    }
}
