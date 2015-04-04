package example.com.memkeeper.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import example.com.memkeeper.POJO.Memory;
import example.com.memkeeper.R;


/**
 * Created by Alexandru on 03-Apr-15.
 */
public class CommentsListItemAdapter extends BaseAdapter {
    /**
     * The inflater.
     */
    protected LayoutInflater inflater;

    /**
     * The context.
     */
    private Context context;

    /**
     * The list of memories.
     */
    protected List<String> commentsList;

    public CommentsListItemAdapter(Context context, List<String> commentsList) {
        this.context = context;
        this.commentsList = commentsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItems(List<String> items)
    {
        this.commentsList = items;
    }

    @Override
    public int getCount() {
        if(commentsList != null)
        {
            return commentsList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return commentsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.comments_list_view_item, parent, false);

            holder = new ViewHolder();

            holder.commentTextView = (TextView) convertView.findViewById(R.id.comments_list_view_item_comment_text_view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.commentTextView.setText(commentsList.get(position));

        return convertView;
    }

    private class ViewHolder {
        TextView commentTextView;
    }

}