package example.com.memkeeper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import example.com.memkeeper.R;


/**
 * Created by Alexandru on 03-Apr-15.
 */
public class MemoryFriendsListItemAdapter extends BaseAdapter {
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
    protected List<String> friendsList;

    public MemoryFriendsListItemAdapter(Context context, List<String> friendsList) {
        this.context = context;
        this.friendsList = friendsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItems(List<String> items)
    {
        this.friendsList = items;
    }

    @Override
    public int getCount() {
        if(friendsList != null)
        {
            return friendsList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return friendsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.memory_friends_list_view_item, parent, false);

            holder = new ViewHolder();

            holder.friendsTextView= (TextView) convertView.findViewById(R.id.memory_friends_list_view_item_friend_text_view);

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_right_left);
//            animation.setDuration(500);
            animation.setStartOffset(position * 200);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            convertView.startAnimation(animation);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.friendsTextView.setText(friendsList.get(position));

        return convertView;
    }

    private class ViewHolder {
        TextView friendsTextView;
    }

}