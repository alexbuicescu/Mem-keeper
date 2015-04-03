package example.com.memkeeper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import example.com.memkeeper.POJO.Memory;
import example.com.memkeeper.R;


/**
 * Created by Alexandru on 03-Apr-15.
 */
public class MemoriesListItemAdapter extends BaseAdapter {
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
    protected List<Memory> memoryList;

    public MemoriesListItemAdapter(Context context, List<Memory> memoryList) {
        this.context = context;
        this.memoryList = memoryList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(memoryList != null)
        {
            return memoryList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return memoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.memory_list_view_item, parent, false);

            holder = new ViewHolder();

            holder.memoryNameTextView = (TextView) convertView.findViewById(R.id.memory_list_view_item_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.memoryNameTextView.setText(memoryList.get(position).getName());

        return convertView;
    }

    public void setMemoriesList(List<Memory> memoryList)
    {
        this.memoryList = memoryList;
    }

    private class ViewHolder {
        TextView memoryNameTextView;
    }
}