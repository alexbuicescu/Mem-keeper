package example.com.memkeeper.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public abstract class FacebookButton
{
	protected UiLifecycleHelper mUiHelper;
	protected Session.StatusCallback mCallback;

	public SessionState getSessionStatus()
	{
		Session session = Session.getActiveSession();

		if (session != null)
		{
			return session.getState();
		}
		return null;// should I return SessionState.CLOSED; ?????
	}

	abstract protected void onSessionStateChange(Session session, SessionState state, Exception exception);

	abstract protected void setCallback();
	
	abstract public void setOnClickListener(OnClickListener listener);

	// setUiHelper(...) must be called after setCallback(), because the callback
	// must be defined
	private void setUiHelper(Activity activity)
	{
		mUiHelper = new UiLifecycleHelper(activity, mCallback);
	}

	public void onCreate(Bundle savedInstanceState)
	{
		mUiHelper.onCreate(savedInstanceState);
	}

	protected void initializeTheButton(Activity activity)
	{
		setCallback();
		setUiHelper(activity);
	}

	public void onResume()
	{
		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed()))
		{
			onSessionStateChange(session, session.getState(), null);
		}

		mUiHelper.onResume();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		mUiHelper.onActivityResult(requestCode, resultCode, data);
	}

	public void onPause()
	{
		mUiHelper.onPause();
	}

	public void onDestroy()
	{
		mUiHelper.onDestroy();
	}

	public void onSaveInstanceState(Bundle outState)
	{
		mUiHelper.onSaveInstanceState(outState);
	}
}
