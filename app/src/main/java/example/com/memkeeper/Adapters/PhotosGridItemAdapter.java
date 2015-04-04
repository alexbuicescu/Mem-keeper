package example.com.memkeeper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import example.com.memkeeper.Activities.NewMemoryActivity;
import example.com.memkeeper.POJO.Photo;
import example.com.memkeeper.R;


/**
 * Created by Alexandru on 03-Apr-15.
 */
public class PhotosGridItemAdapter extends BaseAdapter {
    /**
     * The inflater.
     */
    protected LayoutInflater inflater;

    /**
     * The context.
     */
    private Context context;

    /**
     * The photos.
     */
    protected List<Photo> photos;

    public PhotosGridItemAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(photos != null)
        {
            return photos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return photos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.photos_grid_view_item, parent, false);

            holder = new ViewHolder();

            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.photos_grid_view_thumbnail_image_view);
            holder.checkboxImageView = (ImageView) convertView.findViewById(R.id.photos_grid_view_selected_image_view);

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_bottom_top);
//            animation.setDuration(500);
            animation.setStartOffset(position * 200);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            convertView.startAnimation(animation);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.thumbnailImageView.setImageBitmap(photos.get(position).getThumbnail());

        if(NewMemoryActivity.selectCover)
        {
            holder.checkboxImageView.setVisibility(View.GONE);
            if(photos.get(position).isCover())
            {
                holder.checkboxImageView.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.checkboxImageView.setVisibility(View.GONE);
            }
        }
        else
        {
            if(photos.get(position).isSelected())
            {
//                if(holder.checkboxImageView.getVisibility() == View.GONE)
                {
                    holder.checkboxImageView.setVisibility(View.VISIBLE);
//                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_small_big);
////            animation.setDuration(500);
//                    animation.setInterpolator(new AccelerateDecelerateInterpolator());
//                    holder.checkboxImageView.startAnimation(animation);
                }
            }
            else
            {
                holder.checkboxImageView.setVisibility(View.GONE);
            }

        }

        return convertView;
    }

    public void setPhotos(List<Photo> photos)
    {
        this.photos = photos;
    }

    private class ViewHolder {
        ImageView thumbnailImageView;
        ImageView checkboxImageView;
    }
}