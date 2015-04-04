package example.com.memkeeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import example.com.memkeeper.Activities.CameraActivity;
import example.com.memkeeper.POJO.Photo;
import example.com.memkeeper.Utils.PhotoUtils;


public class SimpleDialog extends Dialog {

	private static boolean isDialogShown = false;

	private boolean dismissDialogOnBackPressed = true;

	public void show()
	{

		if (!isDialogShown)
		{
			super.show();
			isDialogShown = true;
		}

	}

	public void dismiss()
	{
		super.dismiss();
		isDialogShown = false;
	}

	public static void unsetDialog()
	{
		isDialogShown = false;
	}

	public static boolean getIsDialogShown()
	{
		return isDialogShown;
	}

	@Override
	public void onBackPressed()
	{
        super.onBackPressed();
	}

	public boolean isDismissDialogOnBackPressed()
	{
		return dismissDialogOnBackPressed;
	}

	public void setDismissDialogOnBackPressed(boolean dismissDialogOnBackPressed)
	{
		this.dismissDialogOnBackPressed = dismissDialogOnBackPressed;
	}

	/**
	 * Instantiates a new pause dialog.
	 *
	 * @param context
	 *            the context
	 */
	public SimpleDialog(Context context) {
		super(context);
	}

	/**
	 * The Class Builder.
	 */
	public static class Builder {

		/** The pos x. */
		private int posX = -1;

		/** The pos y. */
		private int posY = -1;

		/** The context. */
		private final Context context;

		private boolean dismissDialogOnBackPressed = true;

        private Runnable galleryRunnable;
        int TAKE_PHOTO_CODE = 0;
        public static int count=0;

		/**
		 * Instantiates a new builder.
		 *
		 * @param context
		 *            the context
		 */
		public Builder(Context context) {
			this.context = context;
		}

		public Builder setDismissDialogOnBackPressed(boolean dismissDialogOnBackPressed)
		{
			this.dismissDialogOnBackPressed = dismissDialogOnBackPressed;
			return this;
		}


		/**
		 * Sets the position.
		 *
		 * @param x
		 *            the x
		 * @param y
		 *            the y
		 * @return the builder
		 */
		public Builder setPosition(int x, int y)
		{
			posX = x;
			posY = y;
			return this;
		}

		/**
		 * Builds and returns the dialog.
		 *
		 * @return the dialog
		 */
		public SimpleDialog build()
		{
			final SimpleDialog result = new SimpleDialog(getContext());
			result.setDismissDialogOnBackPressed(dismissDialogOnBackPressed);
			// request that dialog has no title
			result.requestWindowFeature(Window.FEATURE_NO_TITLE);
			WindowManager.LayoutParams WMLP = result.getWindow().getAttributes();

			// if no values for positioning are desired place the dialog in the center of the screen
			if (posX == -1 && posY == -1)
			{
				WMLP.gravity = Gravity.CENTER;
			} else
			{
				// gravity is top left so that x and y have only positive values
				WMLP.gravity = Gravity.TOP | Gravity.LEFT;
				WMLP.x = posX; // x position
				WMLP.y = posY; // y position
			}

			// set the position of the dialog
			result.getWindow().setAttributes(WMLP);

			// remove the dialog border drawable
//			result.getWindow().setBackgroundDrawableResource(R.drawable.dialog_stats);

			// set the layout of the dialog

			result.setContentView(R.layout.dialog_simple);

			result.setCanceledOnTouchOutside(true);

            initializeTextViews(result);

			return result;
		}

        private void initializeTextViews(final SimpleDialog dialogLayout)
        {
            TextView cameraTextView = (TextView) dialogLayout.findViewById(R.id.dialog_simple_camera_text_view);
            TextView galleryTextView = (TextView) dialogLayout.findViewById(R.id.dialog_simple_gallery_text_view);

            cameraTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent myIntent = new Intent(context, CameraActivity.class);
//                    context.startActivity(myIntent);
                    openCamera();
                    dialogLayout.dismiss();
                }
            });
            galleryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(galleryRunnable != null)
                    {
                        galleryRunnable.run();
                        dialogLayout.dismiss();
                    }
                }
            });
        }

		private Context getContext()
		{
			return context;
		}

        public Runnable getGalleryRunnable() {
            return galleryRunnable;
        }

        public Builder setGalleryRunnable(Runnable galleryRunnable) {
            this.galleryRunnable = galleryRunnable;
            return this;
        }

        private void openCamera()
        {
            final String dir = Environment.getExternalStorageDirectory() + "/mem-keeper/";
            File newdir = new File(dir);
            newdir.mkdirs();

            // here,counter will be incremented each time,and the picture taken by camera will be stored as 1.jpg,2.jpg and likewise.
            count++;
            String file = dir+count+".jpg";
            File newfile = new File(file);
            try {
                newfile.createNewFile();
            } catch (IOException e) {}

            Uri outputFileUri = Uri.fromFile(newfile);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            ((Activity)context).startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(outputFileUri);
            ((Activity)context).sendBroadcast(mediaScanIntent);
//            Photo photo = new Photo();
//            photo.setPhotoId(1);
//            PhotoUtils.getAlbum(dir).addPhoto(photo);

        }
    }

}
