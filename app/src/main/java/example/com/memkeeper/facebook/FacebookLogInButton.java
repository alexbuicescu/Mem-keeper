package example.com.memkeeper.facebook;

import android.app.Activity;
import android.util.Log;
import android.view.View.OnClickListener;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.LoginButton;

import java.util.List;

public class FacebookLogInButton extends FacebookButton
{
	private final String TAG = "FacebookLogIn";
	
	private boolean isPendingSharing = false;
	
	private static List<String> mListOfReadingPermissions;
	
	private FacebookShareButton mFbShare;

	private Activity mActivity = null;
	private int mButtonID = 0;

	private LoginButton mFacebookSDKLogInButton;

	private static Runnable mOnLogInSuccess, mOnLogInFailure;


    public FacebookLogInButton(Activity activity, FacebookShareButton fbShare, LoginButton facebookSDKLogInButton)
    {
        mActivity = activity;
        mFbShare = fbShare;

        initializeTheButton(mActivity);

        this.mFacebookSDKLogInButton = facebookSDKLogInButton;

        if(mListOfReadingPermissions != null)
        {
            addReadPermissions(mListOfReadingPermissions);
        }
    }

	public FacebookLogInButton(Activity activity, int buttonID, FacebookShareButton fbShare)
	{
		mActivity = activity;
		mButtonID = buttonID;
		mFbShare = fbShare;

		initializeTheButton(mActivity);

		mFacebookSDKLogInButton = (LoginButton) mActivity.findViewById(mButtonID);
		
		if(mListOfReadingPermissions != null)
		{
			addReadPermissions(mListOfReadingPermissions);
		}
	}
	
	public static void setOnSuccessRunnable(Runnable onLogInSuccess)
	{
		mOnLogInSuccess = onLogInSuccess;
	}

	public static void setOnFailureRunnable(Runnable onLogInFailure)
	{
		mOnLogInFailure = onLogInFailure;
	}
	
	public void performClick()
	{
        Log.i("facebook", "publishing " + getSessionStatus().isOpened());
		isPendingSharing = true;
		if(!getSessionStatus().isOpened())
		{
			mFacebookSDKLogInButton.performClick();
		}
		else
			if(getSessionStatus().isOpened() && isPendingSharing)
			{
				mFbShare.performClick();
				isPendingSharing = false;
			}
	}
	
	public static void setReadPermissions(List<String> list)
	{
		mListOfReadingPermissions = list;
	}
	
	public void setOnClickListener(OnClickListener listener)
	{
		mFacebookSDKLogInButton.setOnClickListener(listener);
	}

	private void addReadPermissions(List<String> list)
	{
		mFacebookSDKLogInButton.setReadPermissions(list);
	}
	
	@Override
	protected void onSessionStateChange(Session session, SessionState state, Exception exception)
	{
		// TODO Auto-generated method stub

		if (state.isOpened())
		{
			Log.i(TAG, "Logged in... "+ isPendingSharing);
			
			if(isPendingSharing)
			{
				mFbShare.performClick();
				isPendingSharing = false;
			}
//			mOnLogInSuccess.run();
		}
		else if (state.isClosed())
		{
			Log.i(TAG, "Logged out...");
		}
		else
		{
			//mOnLogInFailure.run();
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
				Log.i(TAG, "session: " + state);
				
				//mActivity.finish();
			}
		};

	}
}
