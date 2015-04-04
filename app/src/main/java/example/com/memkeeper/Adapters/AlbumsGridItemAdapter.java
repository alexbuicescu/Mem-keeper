package example.com.memkeeper.Adapters;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import android.os.Handler;

import example.com.memkeeper.Activities.NewMemoryActivity;
import example.com.memkeeper.POJO.Album;
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
            holder.selectedTextView = (TextView) convertView.findViewById(R.id.albums_grid_view_selected_text_view);


            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_bottom_top);
//            animation.setDuration(500);
            animation.setStartOffset(position * 200);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            convertView.startAnimation(animation);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.thumbnailImageView.setImageURI(albums.get(position).getPhotosList().get(0).getUri());
        holder.thumbnailImageView.setImageBitmap(albums.get(position).getPhotosList().get(0).getThumbnail());
        holder.countTextView.setText(albums.get(position).getPhotosList().size() + "");
        holder.albumName.setText(albums.get(position).getName());

        if(NewMemoryActivity.selectCover)
        {
            holder.selectedTextView.setVisibility(View.GONE);
        }
        else
        {
            if(albums.get(position).getNrSelectedPhotos() > 0)
            {
                holder.selectedTextView.setVisibility(View.VISIBLE);
                holder.selectedTextView.setText(albums.get(position).getNrSelectedPhotos() + "");
            }
            else
            {
                holder.selectedTextView.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private final Handler animationsHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 0 || msg.what == 1)
            {
            }
        }
    };

    public void setAlbums(List<Album> albums)
    {
        this.albums = albums;
    }

    private class ViewHolder {
        ImageView thumbnailImageView;
        TextView countTextView;
        TextView albumName;
        TextView selectedTextView;
    }
}