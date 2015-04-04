package example.com.memkeeper.Adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
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

            holder.memoryNameTextView = (TextView) convertView.findViewById(R.id.memory_list_view_item_title_text_view);
            holder.memoryDateTextView = (TextView) convertView.findViewById(R.id.memory_list_view_item_date_text_view);
            holder.memoryFromTextView = (TextView) convertView.findViewById(R.id.memory_list_view_item_from_text_view);
            holder.memoryToTextView = (TextView) convertView.findViewById(R.id.memory_list_view_item_to_text_view);
            holder.memoryCoverImageView = (ImageView) convertView.findViewById(R.id.memory_list_view_item_cover_photo_image_view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.memoryNameTextView.setText(memoryList.get(position).getName());
        holder.memoryDateTextView.setText(memoryList.get(position).getDate());
        holder.memoryFromTextView.setText(memoryList.get(position).getLocationOne());
        holder.memoryToTextView.setText(memoryList.get(position).getLocationTwo());

        try {
            Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),
                    Long.parseLong(memoryList.get(position).getCoverImagePath()), MediaStore.Images.Thumbnails.MINI_KIND, null);
            holder.memoryCoverImageView.setImageBitmap(bm);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            holder.memoryCoverImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.upload_cover));
        }

        return convertView;
    }

    public void refresh()
    {
        notifyDataSetChanged();
        Memory memory = memoryList.get(memoryList.size() - 1);
        memoryList.remove(memoryList.size() - 1);
        memoryList.add(memory);
    }

    public void setMemoriesList(List<Memory> memoryList)
    {
        this.memoryList = memoryList;
    }

    private class ViewHolder {
        TextView memoryNameTextView;
        TextView memoryDateTextView;
        TextView memoryFromTextView;
        TextView memoryToTextView;
        ImageView memoryCoverImageView;
    }

}