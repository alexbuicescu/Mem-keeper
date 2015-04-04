package example.com.memkeeper.Layouts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import example.com.memkeeper.Adapters.CommentsListItemAdapter;
import example.com.memkeeper.Adapters.GallerySwipeAdapter;
import example.com.memkeeper.Database.DatabaseHelper;
import example.com.memkeeper.R;
import example.com.memkeeper.Utils.MemoriesUtils;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class CommentsFragLayout extends BaseFragment {

	public interface OnCommentsFragmentListener {
//		public void onPhotoClicked(int position);
	}

    private Activity context;

	private OnCommentsFragmentListener listener;
	private View view;
    private ListView commentsListView;
    private TextView noCommentsTextView;
    private EditText commentEditText;
    private CommentsListItemAdapter commentsListItemAdapter;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if (activity instanceof OnCommentsFragmentListener)
		{
			listener = (OnCommentsFragmentListener) activity;
		} else
		{
			throw new ClassCastException(activity.toString() + " must implemenet OnCommentsFragmentListener");
		}
        context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.comment_fragment, container, false);
		initAll();
        initTopBar();

		return view;
	}

	private void initAll()
	{
        try {
            commentsListView = (ListView) view.findViewById(R.id.comment_fragment_list_view);
            noCommentsTextView = (TextView) view.findViewById(R.id.comment_fragment_no_comments_text_view);
            commentEditText = (EditText) view.findViewById(R.id.comment_fragment_comment_edit_text);
            noCommentsTextView.setVisibility(View.GONE);

            final DatabaseHelper dbHelper = new DatabaseHelper(context);
            ImageButton sendComment = (ImageButton) view.findViewById(R.id.comment_fragment_send_comment_button);
            sendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!commentEditText.getText().toString().trim().equals(""))
                    {
                        MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).addComment(commentEditText.getText().toString());
                        dbHelper.updateMemory(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()));
                        MemoriesUtils.getMemoryList().set(MemoriesUtils.getCurrentMemory(),
                                dbHelper.getMemory(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getId()));
                        updateView();
                        commentEditText.setText("");
                    }
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
        ViewGroup topBarLayout = (ViewGroup) view.findViewById(R.id.comment_fragment_topbar);
        RelativeLayout backButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_new_memory_back_container);
        RelativeLayout doneButtonLayout = (RelativeLayout) topBarLayout.findViewById(R.id.topbar_new_memory_done_container);
        doneButtonLayout.setVisibility(View.GONE);
        TextView topBarTitleTextView = (TextView) topBarLayout.findViewById(R.id.topbar_new_memory_title_text_view);
        topBarTitleTextView.setText("Comments");

        backButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }


    private void updateView()
    {
        if(commentsListItemAdapter != null)
        {
            commentsListItemAdapter.setItems(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getComments());
            commentsListView.setAdapter(commentsListItemAdapter);
            commentsListItemAdapter.notifyDataSetChanged();
        }
        else
        {
            commentsListItemAdapter = new CommentsListItemAdapter(context,
                    MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getComments());
            commentsListView.setAdapter(commentsListItemAdapter);
        }
        if(MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getComments() == null ||
                MemoriesUtils.getMemoryList().get(MemoriesUtils.getCurrentMemory()).getComments().size() == 0)
        {
            noCommentsTextView.setVisibility(View.VISIBLE);
        }
        else
        {
            noCommentsTextView.setVisibility(View.GONE);
        }
    }
}
