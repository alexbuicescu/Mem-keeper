package example.com.memkeeper.facebook;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.FacebookDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FacebookShareButton extends FacebookButton
{
	private static final String TAG = "FacebookShare";
	
	private String mSharingLink, mSharingName, mSharingCaption, mSharingDescription, mSharingPicture;

	private Activity mActivity;

	private Session mSession;

//	private Button mShareButton;

	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";

	private boolean pendingPublishReauthorization = false;

	public FacebookShareButton(Activity activity, Bundle savedInstanceState)
	{
		mActivity = activity;

//		mShareButton = (Button) mActivity.findViewById(buttonID);
//		mShareButton.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Log.i(TAG, "publishing now + " + getSessionStatus());
//				if(getSessionStatus().isOpened())
//				{
//					publishStory();
//				}
//				else
//				{
//					pendingPublishReauthorization = true;
//					fblogin.performClick();
////					publishStory();
//				}
//			}
//		});

		initializeTheButton(mActivity);

		if (savedInstanceState != null)
		{
			pendingPublishReauthorization = savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
		}
	}

	public void onSaveInstanceState(Bundle outState)
	{
		outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
		mUiHelper.onSaveInstanceState(outState);
	}

	private void publishStory()
	{
		mSession = Session.getActiveSession();
		if (mSession != null)
		{
			// Check for publish permissions
			List<String> permissions = mSession.getPermissions();
			Log.i(TAG, "my permissions:\n" + permissions);

			if (!isSubsetOf(PERMISSIONS, permissions))
			{
				pendingPublishReauthorization = true;
				requestPermissions(mSession);

				return;
			}

			 postThroughFacebookDialog();

//			postThroughAppDirectly(session);
		}
	}

	public void requestPermissions(Session session)
	{
        try {
            Log.i(TAG, "request permissions");
            Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(mActivity, Arrays.asList("publish_actions"));
            session.requestNewPublishPermissions(newPermissionsRequest);
        }
        catch (Exception ex)
        {
            Log.i(TAG, "request permissions FAILED (maybe because it is already requesting permissions)");
        }
	}
	
	public void setSharingLink(String link)
	{
		mSharingLink = link;
	}
	
	public void setSharingPicture(String picture)
	{
		mSharingPicture = picture;
	}

	public void setSharingName(String name)
	{
		mSharingName = name;
	}

	public void setSharingCaption(String caption)
	{
		mSharingCaption = caption;
	}
	
	public void setSharingDescription(String description)
	{
		mSharingDescription = description;
	}
	
	public void postThroughFacebookDialog()
	{
		FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(mActivity)
				.setLink(mSharingLink).setPicture(mSharingPicture).setName(mSharingName)
				.setCaption(mSharingCaption).setDescription(mSharingDescription)
				.build();
		mUiHelper.trackPendingDialogCall(shareDialog.present());
	}

	public void postThroughAppDirectly(Session session)
	{
		Bundle postParams = new Bundle();
		postParams.putString("name", "Facebook SDK for Android");
		postParams.putString("caption", "Build great social apps and get more installs.");
		postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
		postParams.putString("link", "https://developers.facebook.com/android");
		postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

		Request.Callback callback = new Request.Callback()
		{
			public void onCompleted(Response response)
			{
				JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
				String postId = null;
				try
				{
					postId = graphResponse.getString("id");
				}
				catch (JSONException e)
				{
					Log.e(TAG, "JSON error " + e.getMessage());
				}
				FacebookRequestError error = response.getError();
				if (error != null)
				{
					Log.e(TAG, error.getErrorMessage());
				}
				else
				{
					Toast.makeText(mActivity, postId, Toast.LENGTH_LONG).show();
					Toast.makeText(mActivity, "Sharing on facebook completed successfully", Toast.LENGTH_LONG).show();
				}
			}
		};

		Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
	}

	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset)
	{
		for (String string : subset)
		{
			if (!superset.contains(string))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	protected void onSessionStateChange(Session session, SessionState state, Exception exception)
	{
		// TODO Auto-generated method stub

		if (state.isOpened())
		{
//			mShareButton.setVisibility(View.VISIBLE);
			if (pendingPublishReauthorization && state.equals(SessionState.OPENED_TOKEN_UPDATED))
			{
				pendingPublishReauthorization = false;
				publishStory();
			}
		}
		else if (state.isClosed())
		{
//			mShareButton.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void setCallback()
	{
		// TODO Auto-generated method stub

		mCallback = new Session.StatusCallback()
		{
			@Override
			public void call(final Session session, final SessionState state, final Exception exception)
			{
				onSessionStateChange(session, state, exception);
				if (session.isOpened() && session.getPermissions().contains("publish_actions"))
				{
					// Log.i(TAG, "publish from here 1");
					// publishStory();
				}
				else if (session.isOpened() && !session.getPermissions().contains("publish_actions"))
				{
					requestPermissions(session);
				}
			}
		};
	}
	
	public void performClick()
	{
//		mShareButton.performClick();
		Log.i(TAG, "publishing now + " + getSessionStatus());
		if(getSessionStatus().isOpened())
		{
			publishStory();
		}
		else
		{
			pendingPublishReauthorization = true;
//			fblogin.performClick();
//			publishStory();
		}
	}

	@Override
	public void setOnClickListener(OnClickListener listener)
	{
		// TODO Auto-generated method stub
		
//		mShareButton.setOnClickListener(listener);
	}

}