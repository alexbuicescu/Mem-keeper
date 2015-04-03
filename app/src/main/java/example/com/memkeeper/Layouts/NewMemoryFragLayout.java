package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import example.com.memkeeper.Adapters.MemoryGridItemAdapter;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.MemoriesUtils;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class NewMemoryFragLayout extends BaseFragment {

	public interface OnNewMemoryFragmentListener {
//		public void onPhotoClicked(int position);
	}

    private Activity context;

	private OnNewMemoryFragmentListener listener;
	private View view;
    private ArrayList<EditText> friendsEditText = new ArrayList<>();
    private LayoutInflater inflater;

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
        EditText memoryFromEditText;
        EditText memoryToEditText;

        memoryItem = (ViewGroup) view.findViewById(R.id.new_memory_fragment_name_container);
        memoryItemName = (TextView) memoryItem.findViewById(R.id.new_memory_fragment_name_text_view);
        memoryItemName.setText("Name of your journey:");
        memoryNameEditText = (EditText) memoryItem.findViewById(R.id.new_memory_fragment_name_edit_text);
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
}
