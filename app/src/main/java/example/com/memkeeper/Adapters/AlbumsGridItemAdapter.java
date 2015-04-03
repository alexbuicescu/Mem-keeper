package example.com.memkeeper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import example.com.memkeeper.POJO.Album;
import example.com.memkeeper.POJO.Memory;
import example.com.memkeeper.R;


/**
 * Created by Alexandru on 03-Apr-15.
 */
public class AlbumsGridItemAdapter extends BaseAdapter {
    /**
     * The inflater.
     */
    protected LayoutInflater inflater;

    /**
     * The context.
     */
    private Context context;

    /**
     * The albums.
     */
    protected List<Album> albums;

    public AlbumsGridItemAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(albums != null)
        {
            return albums.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return albums.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.albums_grid_view_item, parent, false);

            holder = new ViewHolder();

            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.albums_grid_view_thumbnail_image_view);
            holder.countTextView = (TextView) convertView.findViewById(R.id.albums_grid_view_counter_text_view);
            holder.albumName = (TextView) convertView.findViewById(R.id.albums_grid_view_name_text_view);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.thumbnailImageView.setImageBitmap(albums.get(position).getThumbnail());
        holder.countTextView.setText(albums.get(position).getPhotosList().size() + "");
        holder.albumName.setText(albums.get(position).getName());

        return convertView;
    }

    public void setAlbums(List<Album> albums)
    {
        this.albums = albums;
    }

    private class ViewHolder {
        ImageView thumbnailImageView;
        TextView countTextView;
        TextView albumName;
    }
}