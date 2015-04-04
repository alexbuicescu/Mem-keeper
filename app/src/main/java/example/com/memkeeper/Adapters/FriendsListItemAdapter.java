package example.com.memkeeper.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import example.com.memkeeper.POJO.Friend;
import example.com.memkeeper.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Alexandru on 03-Apr-15.
 */
public class FriendsListItemAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    Context context;
    ArrayList<Friend> currentItems;
    ArrayList<String> currentHeaders;
    private HashMap<String, Integer> indexer;
    protected LayoutInflater inflater;

    public FriendsListItemAdapter(Context context, ArrayList<Friend> currentItems) {

        this.context = context;
        this.currentItems = currentItems;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initIndexer();
    }

    private void initIndexer() {
        indexer = new HashMap<>();

        Collections.sort(currentItems, new Comparator<Friend>() {
            @Override
            public int compare(Friend lhs, Friend rhs) {
                return lhs.getName().toUpperCase().compareTo(rhs.getName().toUpperCase());
            }
        });

        Iterator iterator = currentItems.iterator();
        String last = "";
        if(iterator.hasNext())
        {
            last = ((Friend)iterator.next()).getName();
        }
        while(iterator.hasNext())
        {
            String current = ((Friend)iterator.next()).getName();
            if(current.toUpperCase().equals(last.toUpperCase()))
            {
                Log.i("sorted", "removed " + last);
                iterator.remove();
            }
            last = current;
        }

        for (int i = 1; i < currentItems.size(); i++) {
            if(currentItems.get(i-1).getName().toUpperCase().equals(currentItems.get(i).getName().toUpperCase()))
            {

            }
        }
        for (int i = 0; i < currentItems.size(); i++) {
            if(indexer.get(currentItems.get(i).getName().toUpperCase().charAt(0) + "") == null)
            {
                indexer.put(currentItems.get(i).getName().toUpperCase().charAt(0) + "", i);
            }
        }

        Set<String> keys = indexer.keySet();
        currentHeaders = new ArrayList<>(keys);
        Collections.sort(currentHeaders);
    }

    public void setCurrentItems(ArrayList<Friend> currentItems) {
        this.currentItems = currentItems;
    }

    @Override
    public View getHeaderView(int i, View convertView, ViewGroup parent) {
        DividerViewHolder holder;

        if (convertView == null) {
            holder = new DividerViewHolder();
            convertView = inflater.inflate(R.layout.friend_header_list_view_item, parent, false);
            holder.groupName = (TextView) convertView.findViewById(R.id.friend_item_layout_header_name_text_view);
            convertView.setTag(holder);
        } else {
            holder = (DividerViewHolder) convertView.getTag();
        }

        holder.groupName.setText("Header: " + currentItems.get(i).getName().toUpperCase().charAt(0));

        return convertView;
    }

    @Override
    public long getHeaderId(int i) {
        return currentItems.get(i).getName().toUpperCase().charAt(0);
    }

    @Override
    public int getPositionForSection(int section) {
        try {
            return indexer.get(currentHeaders.get(section));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < currentHeaders.size(); i++) {
            if (position < indexer.get(currentHeaders.get(i))) {
                return i - 1;
            }
        }
        return currentHeaders.size() - 1;
    }

    @Override
    public Object[] getSections() {
        return currentHeaders.toArray();
    }

    @Override
    public int getCount() {
        return currentItems.size();
    }

    @Override
    public Object getItem(int position) {
        return currentItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        //if it is a new item
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.friend_list_view_item, parent, false);

            holder = new ViewHolder();

            holder.titleTextView = (TextView) convertView.findViewById(R.id.friend_item_layout_name_text_view);

            convertView.setTag(holder);
        } else {
            //here we recycle the previous ViewHolder, by using an older one
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleTextView.setText(currentItems.get(position).getName());

        return convertView;
    }

    private class ViewHolder {
        TextView titleTextView;
    }

    public static class DividerViewHolder {
        TextView groupName;
    }
}
