package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import example.com.memkeeper.Activities.AddPhotosActivity;
import example.com.memkeeper.Adapters.MemoryGridItemAdapter;
import example.com.memkeeper.POJO.Photo;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.MemoriesUtils;
import example.com.memkeeper.Utils.PhotoUtils;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class NewMemoryFragLayout extends BaseFragment {

	public interface OnNewMemoryFragmentListener {
		public void onAddPhotosClicked();
        public void onChangeCoverClicked();
	}

    private Activity context;

	private OnNewMemoryFragmentListener listener;
	private View view;
    private ArrayList<EditText> friendsEditText = new ArrayList<>();
    private LayoutInflater inflater;

    private ImageView coverImageView;
    private Photo coverPhoto;

    private ImageView image1ImageView;
    private ImageView image2ImageView;
    private ImageView image3ImageView;
    private TextView memoryDatePicker;
    private RelativeLayout newPhotoView1;
    private RelativeLayout newPhotoView2;
    private RelativeLayout newPhotoView3;
    private List<String> imagesPaths = new ArrayList<>();

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if (activity instanceof OnNewMemoryFragmentListener)
		{
			listener = (OnNewMemoryFragmentListener) activity;
		} else
		{
			throw new ClassCastException(activity.toString() + " must implemenet OnNewMemoryFragmentListener");
		}
        context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        this.inflater = inflater;
		view = inflater.inflate(R.layout.new_memory_fragment, container, false);
		initAll();

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

    private void updateView()
    {
        ViewGroup memoryItem;
        TextView memoryItemName;
        TextView addNewButton;
        EditText memoryFriendNameEditText;
        EditText memoryNameEditText;
        EditText memoryDateEditText;
        EditText memoryFromEditText;
        EditText memoryToEditText;

        memoryItem = (ViewGroup) view.findViewById(R.id.new_memory_fragment_name_container);
        memoryItemName = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_name_text_view);
        memoryItemName.setText("Name of your journey:");
        memoryNameEditText = (EditText) memoryItem.findViewById(R.id.new_memory_fragment_name_edit_text);
        memoryNameEditText.clearFocus();
        addNewButton = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_add_new_button);
        addNewButton.setVisibility(View.GONE);

        memoryItem = (ViewGroup) view.findViewById(R.id.new_memory_fragment_friends_container);
        memoryItemName = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_name_text_view);
        final LinearLayout friendsContainer = (LinearLayout) memoryItem.findViewById(R.id.new_memory_fragment_add_new_friends_container);
        memoryItemName.setText("Who were you with:");
        memoryFriendNameEditText = (EditText) memoryItem.findViewById(R.id.new_memory_fragment_name_edit_text);
        memoryFriendNameEditText.setVisibility(View.GONE);
        addNewButton = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_add_new_button);
        addNewButton.setVisibility(View.VISIBLE);
        final ViewGroup finalMemoryItem = memoryItem;
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFriends(finalMemoryItem, friendsContainer);
            }
        });

        memoryItem = (ViewGroup) view.findViewById(R.id.new_memory_fragment_location_from_container);
        memoryItemName = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_name_text_view);
        memoryItemName.setText("Where did you travel from:");
        memoryFromEditText = (EditText) memoryItem.findViewById(R.id.new_memory_fragment_name_edit_text);
        addNewButton = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_add_new_button);
        addNewButton.setVisibility(View.GONE);

        memoryItem = (ViewGroup) view.findViewById(R.id.new_memory_fragment_location_to_container);
        memoryItemName = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_name_text_view);
        memoryItemName.setText("Where did you travel to:");
        memoryToEditText = (EditText) memoryItem.findViewById(R.id.new_memory_fragment_name_edit_text);
        addNewButton = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_add_new_button);
        addNewButton.setVisibility(View.GONE);

        memoryItem = (ViewGroup) view.findViewById(R.id.new_memory_fragment_date_container);
        memoryItemName = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_name_text_view);
        memoryItemName.setText("When did you travel:");
        memoryDateEditText = (EditText) memoryItem.findViewById(R.id.new_memory_fragment_name_edit_text);
        memoryDateEditText.setVisibility(View.GONE);
        memoryDatePicker = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_date_picker);
        memoryDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog datePicker = new DatePickerDialog(context,
                        R.style.AppTheme, datePickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select the date");
                datePicker.show();
            }
        });
        memoryDatePicker.setVisibility(View.VISIBLE);
        addNewButton = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_add_new_button);
        addNewButton.setVisibility(View.GONE);

        memoryItem = (ViewGroup) view.findViewById(R.id.new_memory_fragment_add_photos_container);
        memoryItemName = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_name_text_view);
        memoryItemName.setText("What did you see:");
        final ViewGroup finalMemoryItem2 = memoryItem;
        final LinearLayout photosContainer = (LinearLayout) memoryItem.findViewById(R.id.new_memory_fragment_photos_container);
        EditText memoryEditText = (EditText) memoryItem.findViewById(R.id.new_memory_fragment_name_edit_text);
        memoryEditText.setVisibility(View.GONE);
        addNewButton = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_add_new_button);
        addNewButton.setVisibility(View.GONE);
//        addNewButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        initPhotos(finalMemoryItem2, photosContainer);

        coverImageView = (ImageView) view.findViewById(R.id.new_memory_fragment_cover_image_view);
        coverImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                listener.onChangeCoverClicked();
            }
        });
        coverImageView.requestFocus();
    }

    public void initCover(Photo coverPhoto)
    {
        if(coverPhoto == null)
        {
        }
        else
        {
            coverImageView.setImageBitmap(coverPhoto.getThumbnail());
        }
    }

    private void initFriends(final ViewGroup finalMemoryItem, final LinearLayout friendsContainer)
    {
        boolean ok = true;
        for(EditText friendEditText : friendsEditText)
        {
            if(friendEditText.getText().toString().equals(""))
            {
                friendEditText.setError("Can't be empty");
                friendEditText.requestFocus();
                ok = false;
            }
        }
        if(!ok)
        {
            return;
        }

        final RelativeLayout newFriendView = (RelativeLayout) inflater.inflate(R.layout.friends_edit_text_layout, finalMemoryItem, false);
        final EditText friendsNameEditText = (EditText) newFriendView.findViewById(R.id.new_memory_fragment_name_edit_text);
        ImageButton friendsRemoveButton = (ImageButton) newFriendView.findViewById(R.id.new_memory_fragment_remove_button);
        friendsRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendsEditText.remove(friendsNameEditText);
                friendsContainer.removeView(newFriendView);
                if(!friendsEditText.isEmpty())
                {
                    friendsEditText.get(friendsEditText.size() - 1).requestFocus();
                }
            }
        });
        friendsEditText.add(friendsNameEditText);
        friendsNameEditText.requestFocus();

        friendsContainer.addView(newFriendView);

    }

    private void initPhotos(final ViewGroup finalMemoryItem, final LinearLayout photosContainer)
    {
        newPhotoView1 = (RelativeLayout) inflater.inflate(R.layout.add_new_photos_layout, finalMemoryItem, false);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) newPhotoView1.getLayoutParams();
        layoutParams.width = 0;
        layoutParams.weight = 1;
        newPhotoView1.setLayoutParams(layoutParams);
        image1ImageView = (ImageView) newPhotoView1.findViewById(R.id.add_new_photos_image_view);
        newPhotoView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        photosContainer.addView(newPhotoView1);

        newPhotoView2 = (RelativeLayout) inflater.inflate(R.layout.add_new_photos_layout, finalMemoryItem, false);
        layoutParams = (LinearLayout.LayoutParams) newPhotoView2.getLayoutParams();
        layoutParams.width = 0;
        layoutParams.weight = 1;
        newPhotoView2.setLayoutParams(layoutParams);
        image2ImageView = (ImageView) newPhotoView2.findViewById(R.id.add_new_photos_image_view);
        newPhotoView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        newPhotoView2.setVisibility(View.INVISIBLE);
        photosContainer.addView(newPhotoView2);

        newPhotoView3 = (RelativeLayout) inflater.inflate(R.layout.add_new_photos_layout, finalMemoryItem, false);
        layoutParams = (LinearLayout.LayoutParams) newPhotoView3.getLayoutParams();
        layoutParams.width = 0;
        layoutParams.weight = 1;
        newPhotoView3.setLayoutParams(layoutParams);
        image3ImageView = (ImageView) newPhotoView3.findViewById(R.id.add_new_photos_image_view);
        newPhotoView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        newPhotoView3.setVisibility(View.INVISIBLE);
        photosContainer.addView(newPhotoView3);
    }

    private void openGallery()
    {
//        Intent myIntent = new Intent(getActivity(), AddPhotosActivity.class);
//        context.startActivity(myIntent);
        hideKeyboard();
        listener.onAddPhotosClicked();
    }

    public void updatePhotos()
    {
        if(PhotoUtils.getSelectedPhotos() == null)
        {
            newPhotoView1.setVisibility(View.VISIBLE);
            newPhotoView2.setVisibility(View.INVISIBLE);
            newPhotoView3.setVisibility(View.INVISIBLE);
            image1ImageView.setImageDrawable(getResources().getDrawable(R.drawable.gallery_arrow_right));
            return;
        }
        if(PhotoUtils.getSelectedPhotos().size() > 0)
        {
            newPhotoView1.setVisibility(View.VISIBLE);
            newPhotoView2.setVisibility(View.INVISIBLE);
            newPhotoView3.setVisibility(View.INVISIBLE);
            image1ImageView.setImageBitmap(PhotoUtils.getSelectedPhotos().get(0).getThumbnail());
            image2ImageView.setImageDrawable(getResources().getDrawable(R.drawable.gallery_arrow_right));
        }
        else
        {
            newPhotoView1.setVisibility(View.VISIBLE);
            newPhotoView2.setVisibility(View.INVISIBLE);
            newPhotoView3.setVisibility(View.INVISIBLE);
            image1ImageView.setImageDrawable(getResources().getDrawable(R.drawable.gallery_arrow_right));
        }

        if(PhotoUtils.getSelectedPhotos().size() > 1)
        {
            newPhotoView2.setVisibility(View.VISIBLE);
            newPhotoView3.setVisibility(View.VISIBLE);
            image1ImageView.setImageBitmap(PhotoUtils.getSelectedPhotos().get(0).getThumbnail());
            image2ImageView.setImageBitmap(PhotoUtils.getSelectedPhotos().get(1).getThumbnail());
            image3ImageView.setImageDrawable(getResources().getDrawable(R.drawable.gallery_arrow_right));
        }
        else
        if(PhotoUtils.getSelectedPhotos().size() == 1)
        {
            newPhotoView2.setVisibility(View.VISIBLE);
            newPhotoView3.setVisibility(View.INVISIBLE);
            image2ImageView.setImageDrawable(getResources().getDrawable(R.drawable.gallery_arrow_right));
        }
//        if(PhotoUtils.getSelectedPhotos().size() > 2)
//        {
//            newPhotoView1.setVisibility(View.VISIBLE);
//            newPhotoView2.setVisibility(View.VISIBLE);
//            newPhotoView3.setVisibility(View.VISIBLE);
//            image1ImageView.setImageBitmap(PhotoUtils.getSelectedPhotos().get(0).getThumbnail());
//            image2ImageView.setImageBitmap(PhotoUtils.getSelectedPhotos().get(1).getThumbnail());
//            image3ImageView.setImageDrawable(getResources().getDrawable(R.drawable.gallery_arrow_right));
//        }
    }
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);

            memoryDatePicker.setText(day1 + "/" + month1 + "/" + year1);
        }
    };


    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(coverImageView.getWindowToken(), 0);
    }
}
