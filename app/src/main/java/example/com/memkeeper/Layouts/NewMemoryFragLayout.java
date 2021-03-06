package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import android.widget.Toast;

import com.nirhart.parallaxscroll.views.ParallaxScrollView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import example.com.memkeeper.Activities.AddPhotosActivity;
import example.com.memkeeper.Activities.MainActivity;
import example.com.memkeeper.Activities.MemoryActivity;
import example.com.memkeeper.Activities.NewMemoryActivity;
import example.com.memkeeper.Adapters.MemoryGridItemAdapter;
import example.com.memkeeper.Database.DatabaseHelper;
import example.com.memkeeper.POJO.Album;
import example.com.memkeeper.POJO.Friend;
import example.com.memkeeper.POJO.Memory;
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
        public void onSaveClicked();
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

    TextView memoryItemName;
    TextView addNewButton;
    EditText memoryFriendNameEditText;
    EditText memoryNameEditText;
    EditText memoryDateEditText;
    EditText memoryFromEditText;
    EditText memoryToEditText;

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
        initTopBar();
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

    private void initTopBar()
    {
        ViewGroup topBarLayout = (ViewGroup) view.findViewById(R.id.new_memory_fragment_top_bar);
        RelativeLayout backButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_new_memory_back_container);
        RelativeLayout doneButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_new_memory_done_container);

        if(NewMemoryActivity.isEdit == 1)
        {
            TextView backButtonTitleTextView = (TextView) topBarLayout.findViewById(R.id.topbar_new_memory_title_text_view);
            backButtonTitleTextView.setText("Edit");
            RelativeLayout deleteButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_new_memory_delete_container);
            deleteButtonLayout.setVisibility(View.VISIBLE);
            deleteButtonLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    dbHelper.deleteMemory(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getId());
                    hideKeyboard();
//                    getActivity().finish();
                    Intent myIntent = new Intent(context, MainActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(myIntent);
                    broadcastIntent(null);
                }
            });
        }

        backButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewMemoryActivity)context).onBackPressed();
                broadcastIntent(null);
                hideKeyboard();
//                listener.onTopBarBackClick();
            }
        });

        doneButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveMemory()) {
                    listener.onSaveClicked();
                    hideKeyboard();
                    broadcastIntent(null);
                    getActivity().finish();
                }
            }
        });
    }

    private void updateView()
    {
        ViewGroup memoryItem;

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


        if(PhotoUtils.getSelectedPhotos() != null) {
            for (Photo photo : PhotoUtils.getSelectedPhotos()) {
                photo.setSelected(false);
            }
            PhotoUtils.getSelectedPhotos().clear();
        }
        for (Album album : PhotoUtils.getAlbums()) {
            album.setNrSelectedPhotos(0);
        }


        if(NewMemoryActivity.isEdit == 1)
        {
            fillForEdit(finalMemoryItem, friendsContainer);
        }
    }

    public void initCover(Photo coverPhoto)
    {
        this.coverPhoto = coverPhoto;
        if(coverPhoto == null)
        {
        }
        else
        {
            coverImageView.setImageBitmap(coverPhoto.getThumbnail());
//            RelativeLayout.LayoutParams rlImgAvatarHolderParams = (RelativeLayout.LayoutParams) coverImageView.getLayoutParams();
//            ParallaxScrollView scrollView = (ParallaxScrollView) view.findViewById(R.id.sv_content);
//            scrollView.setExternalParallaxedView(coverImageView);
//            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//            Display display = wm.getDefaultDisplay();
//            Log.i("size", -display.getWidth() / 4 + "");
//            rlImgAvatarHolderParams.setMargins(0, -display.getWidth() / 4, 0, 0);
//            coverImageView.setLayoutParams(rlImgAvatarHolderParams);
        }
    }

    private void fillForEdit(final ViewGroup finalMemoryItem, final LinearLayout friendsContainer)
    {
        Memory memory = MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory());
        memoryNameEditText.setText(memory.getName());
        memoryDatePicker.setText(memory.getDate());
        memoryFromEditText.setText(memory.getLocationOne());
        memoryToEditText.setText(memory.getLocationTwo());
        Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),
                Long.parseLong(memory.getCoverImagePath()), MediaStore.Images.Thumbnails.MINI_KIND, null);
        coverImageView.setImageBitmap(bm);
        for(String friend : memory.getFriends())
        {

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
            friendsNameEditText.setText(friend);
            friendsEditText.add(friendsNameEditText);

            friendsContainer.addView(newFriendView);
        }

        if(PhotoUtils.getSelectedPhotos() != null) {
            for (Photo photo : PhotoUtils.getSelectedPhotos()) {
                photo.setSelected(false);
            }
            PhotoUtils.getSelectedPhotos().clear();
        }
        for (Album album : PhotoUtils.getAlbums()) {
            album.setNrSelectedPhotos(0);
        }

        for(String id : memory.getImagesPaths()) {
            for (Album album : PhotoUtils.getAlbums()) {
                for (Photo photo : album.getPhotosList()) {
                    if (photo.getPhotoId() == Integer.parseInt(id))
                    {
                        album.setNrSelectedPhotos(album.getNrSelectedPhotos() + 1);
                        photo.setSelected(true);
                        PhotoUtils.addSelectedPhotos(photo);
                        break;
                    }
                }
            }
        }

        if(coverPhoto == null)
        {
            coverPhoto = new Photo();
        }
        coverPhoto.setPhotoId(Integer.parseInt(memory.getCoverImagePath()));
        coverPhoto.setThumbnail(bm);

        updatePhotos();
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

            Calendar cal = Calendar.getInstance(TimeZone.getDefault());

            if(Integer.parseInt(year1) > cal.get(Calendar.YEAR))
            {
                memoryDatePicker.setText("Select a lower value!");
                return;
            }
            if(Integer.parseInt(year1) == cal.get(Calendar.YEAR) && Integer.parseInt(month1) - 1 > cal.get(Calendar.MONTH) )
            {
                memoryDatePicker.setText("Select a lower value!");
                return;
            }
            if(Integer.parseInt(year1) == cal.get(Calendar.YEAR) && Integer.parseInt(month1) - 1 == cal.get(Calendar.MONTH)
                    && Integer.parseInt(day1) > cal.get(Calendar.DAY_OF_MONTH))
            {
                memoryDatePicker.setText("Select a lower value!");
                return;
            }
            memoryDatePicker.setText(year1 + "/" + month1 + "/" + day1);
        }
    };


    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(coverImageView.getWindowToken(), 0);
    }

    private boolean saveMemory()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        Memory memory = new Memory();
        try {
            memory.setId(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(memoryNameEditText.getText().toString().trim().equals(""))
        {
            memoryNameEditText.requestFocus();
            memoryNameEditText.setError("Can't be empty!");
            return false;
        }
        memory.setName(memoryNameEditText.getText().toString());
        if(memoryDatePicker.getText().toString().charAt(0) >= '0' && memoryDatePicker.getText().toString().charAt(0) <= '9')
        {
            memory.setDate(memoryDatePicker.getText().toString());
        }
        else
        {
            Toast.makeText(context, "Select a normal date....", Toast.LENGTH_LONG).show();
            return false;
        }
        if(memoryFromEditText.getText().toString().trim().equals(""))
        {
            memoryFromEditText.requestFocus();
            memoryFromEditText.setError("Can't be empty!");
            return false;
        }
        memory.setLocationOne(memoryFromEditText.getText().toString());
        if(memoryToEditText.getText().toString().trim().equals(""))
        {
            memoryToEditText.requestFocus();
            memoryToEditText.setError("Can't be empty!");
            return false;
        }
        memory.setLocationTwo(memoryToEditText.getText().toString());
        if(coverPhoto != null)
        {
            memory.setCoverImagePath(coverPhoto.getPhotoId() + "");
        }

        List<String> photosIDs = new ArrayList<>();
        List<String> latitude = new ArrayList<>();
        List<String> longitude = new ArrayList<>();
        if(PhotoUtils.getSelectedPhotos() != null) {
            for (Photo photo : PhotoUtils.getSelectedPhotos()) {
                photosIDs.add(String.valueOf(photo.getPhotoId()));
                if(photo.getLatitude() != null && photo.getLongitude() != null)
                {
                    latitude.add(photo.getLatitude());
                    longitude.add(photo.getLongitude());
                }
            }
        }
        memory.setLatitude(latitude);
        memory.setLongitude(longitude);

        memory.setImagesPaths(photosIDs);
        List<String> friends = new ArrayList<>();
        for(EditText editText : friendsEditText)
        {
            if(editText.getText().toString().trim().equals(""))
            {
                editText.requestFocus();
                editText.setError("Can't be empty!");
                return false;
            }
            friends.add(editText.getText().toString());
        }
        memory.setFriends(friends);

        if(NewMemoryActivity.isEdit == 1)
        {
            if(dbHelper.updateMemory(memory))
            {
                MemoriesUtils.getMemoryList().set(MemoriesUtils.getCurrentMemory(), dbHelper.getMemory(memory.getId()));
                return true;
            } else {
                Log.i("memory", "update failed");
                Toast.makeText(context, "update failed", Toast.LENGTH_LONG).show();
            }
        }
        else {
            if (dbHelper.insertMemory(memory)) {
                return true;
            } else {
                Toast.makeText(context, "insertion failed", Toast.LENGTH_LONG).show();
                Log.i("memory", "insertion failed");
            }
        }
        return false;
    }


    // broadcast a custom intent.
    public void broadcastIntent(View view)
    {
        Intent intent = new Intent();
        intent.setAction("example.com.memkeeper");
        getActivity().sendBroadcast(intent);

//        Intent intent2 = new Intent();
//        intent2.setAction("example.com.memkeeper");
//        getActivity().sendBroadcast(intent2);
    }

    public void update()
    {
//        Intent intent = new Intent("com.yourcompany.testIntent");
//        intent.putExtra("value","test");
//        sendBroadcast(intent);
    }
}
