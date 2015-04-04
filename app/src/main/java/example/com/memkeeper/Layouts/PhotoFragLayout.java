package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import example.com.memkeeper.Adapters.GallerySwipeAdapter;
import example.com.memkeeper.Adapters.MemoryGridItemAdapter;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.MemoriesUtils;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class PhotoFragLayout extends BaseFragment {

	public interface OnPhotoFragmentListener {
//		public void onPhotoClicked(int position);
	}

    private Activity context;

	private OnPhotoFragmentListener listener;
	private View view;
    protected ImageView photoImageView;
    private android.support.v4.view.ViewPager viewPager;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if (activity instanceof OnPhotoFragmentListener)
		{
			listener = (OnPhotoFragmentListener) activity;
		} else
		{
			throw new ClassCastException(activity.toString() + " must implemenet OnPhotoFragmentListener");
		}
        context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.photo_fragment, container, false);
		initAll();
        initTopBar();

		return view;
	}

	private void initAll()
	{
        try {
            viewPager = (android.support.v4.view.ViewPager) view.findViewById(R.id.photos_holder_view_pager);
            GallerySwipeAdapter gallerySwipeAdapter = new GallerySwipeAdapter(context, MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()));
            viewPager.setAdapter(gallerySwipeAdapter);
//            photoImageView = (ImageView) view.findViewById(R.id.photo_layout_image_view);

            updateView();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initTopBar()
    {
        ViewGroup topBarLayout = (ViewGroup) view.findViewById(R.id.photo_fragment_top_bar);
        RelativeLayout backButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_memory_back_container);
        RelativeLayout doneButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_memory_done_container);
        doneButtonLayout.setVisibility(View.GONE);
        TextView topBarTitleTextView = (TextView) topBarLayout.findViewById(R.id.topbar_memory_title_text_view);
        topBarTitleTextView.setText(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getName());

        backButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }


    private void updateView()
    {
    }
}
