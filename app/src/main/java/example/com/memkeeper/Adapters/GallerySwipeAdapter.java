package example.com.memkeeper.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import example.com.memkeeper.POJO.Memory;
import example.com.memkeeper.R;


/**
 * Created by Alexandru on 04-Apr-15.
 */
public class GallerySwipeAdapter extends PagerAdapter {
    private Activity activity;
    private ArrayList<String> images;
    private LayoutInflater inflater;
//    private GallerySwipeableFilesLayout layout;
//    private ViewListener viewListener;

    public GallerySwipeAdapter(Context context, Memory memory) {
        activity = (Activity) context;
        images = (ArrayList<String>) memory.getImagesPaths();
//        this.layout = layout;
//        this.viewListener = viewListener;
    }

    @Override
    public int getCount()
    {
        if (images == null)
        {
            return 0;
        }

        return images.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg0 == ((RelativeLayout) arg1);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        ImageView imgDisplay;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.photo_layout, container, false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.photo_layout_image_view);

        final String galleryFile = images.get(position);
        Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(activity.getContentResolver(),
                Long.parseLong(galleryFile), MediaStore.Images.Thumbnails.MINI_KIND, null);
        imgDisplay.setImageBitmap(bm);
//        Bitmap bitmap = null;
//        try
//        {
//            if (file instanceof VideoGalleryFile)
//            {
//                bitmap = BitmapFactory.decodeFile(images.get(position).getThumbUri(), options);
//            }
//            else
//            {
//                bitmap = BitmapFactory.decodeFile(images.get(position).getUri(), options);
//            }
//        }
//        catch (OutOfMemoryError oom)
//        {
//            oom.printStackTrace();
//        }
//        catch (Exception e)
//        {
//        }
//
//        if (bitmap != null)
//        {
//            imgDisplay.setImageBitmap(bitmap);
//            imgDisplay.setMaxZoom(8);
//        }

        ((ViewPager) container).addView(viewLayout);
//        viewLayout.setOnClickListener(layout);
//        imgDisplay.setOnClickListener(layout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}