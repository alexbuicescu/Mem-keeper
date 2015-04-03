package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import example.com.memkeeper.Adapters.PhotosGridItemAdapter;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.PhotoUtils;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class PhotosFragLayout extends BaseFragment {

	public interface OnPhotosFragmentListener {
		public void onPhotoClicked(int position);
	}

    private Activity context;

	private OnPhotosFragmentListener listener;
	private View view;
    protected GridView photosGridView;

    private PhotosGridItemAdapter photosGridItemAdapter;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if (activity instanceof OnPhotosFragmentListener)
		{
			listener = (OnPhotosFragmentListener) activity;
		} else
		{
			throw new ClassCastException(activity.toString() + " must implemenet OnPhotosFragmentListener");
		}
        context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.photos_fragment, container, false);
		initAll();

		return view;
	}

	private void initAll()
	{
        try {
            photosGridView = (GridView) view.findViewById(R.id.activity_photos_grid_view);

            photosGridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    boolean isSelected = PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position).isSelected();
                    PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList().get(position).setSelected(!isSelected);
                    photosGridItemAdapter.notifyDataSetChanged();

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

    private void updateView()
    {
        if (photosGridItemAdapter == null)
        {
            photosGridItemAdapter = new PhotosGridItemAdapter(context,
                    PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList());
            photosGridView.setAdapter(photosGridItemAdapter);
        } else
        {
            photosGridItemAdapter.setPhotos(PhotoUtils.getAlbums().get(PhotoUtils.getCurrentAlbum()).getPhotosList());
        }
    }
}
