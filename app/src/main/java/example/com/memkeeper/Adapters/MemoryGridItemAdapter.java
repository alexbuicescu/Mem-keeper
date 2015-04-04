package example.com.memkeeper.Adapters;

import android.content.Context;
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
public class MemoryGridItemAdapter extends BaseAdapter {
    /**
     * The inflater.
     */
    protected LayoutInflater inflater;

    /**
     * The context.
     */
    private Context context;

    /**
     * The memory.
     */
    protected Memory memory;

    public MemoryGridItemAdapter(Context context, Memory memory) {
        this.context = context;
        this.memory = memory;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(memory != null && memory.getImagesPaths() != null)
        {
            return memory.getImagesPaths().size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return memory.getImagesPaths().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.memory_photos_grid_view_item, parent, false);

            holder = new ViewHolder();

            holder.thumbnailImageView= (ImageView) convertView.findViewById(R.id.memory_photos_grid_thumbnail_image_view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.thumbnailImageView.setImageBitmap(memory.getImages().get(position).getThumbnail());

        Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),
                Long.parseLong(memory.getImagesPaths().get(position)), MediaStore.Images.Thumbnails.MINI_KIND, null);
        holder.thumbnailImageView.setImageBitmap(bm);

        return convertView;
    }

    public void setMemory(Memory memory)
    {
        this.memory = memory;
    }

    private class ViewHolder {
        ImageView thumbnailImageView;
    }
}