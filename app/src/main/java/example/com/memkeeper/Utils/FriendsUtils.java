package example.com.memkeeper.Utils;

import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.com.memkeeper.POJO.Friend;

/**
 * Created by Alexandru on 04-Apr-15.
 */
public class FriendsUtils {
    private static ArrayList<Friend> friendList;

    public static ArrayList<Friend> getFriendList() {
        if(friendList == null)
        {
            friendList = new ArrayList<>();
        }
//        Friend f = new Friend();
//        f.setName("nume1");
//        friendList.add(f);
//        friendList.add(f);
//        f.setName("anume");
//        friendList.add(f);
        return friendList;
    }

    public static void fetchContacts(Activity activity)
    {
        if(friendList == null)
        {
            friendList = new ArrayList<>();
        }
        Cursor cursor = activity.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[]{
                        ContactsContract.RawContacts._ID,
                        ContactsContract.RawContacts.ACCOUNT_TYPE,
                        ContactsContract.RawContacts.ACCOUNT_NAME,
                        ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,
                },
                ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'com.anddroid.contacts.sim' "
//                        + " AND " + ContactsContract.RawContacts.ACCOUNT_TYPE + " <> 'com.google' " //if you don't want to google contacts also
                ,
                null,
                null);
        if(cursor.moveToFirst())
        {
            String name;

            int nameColumn = cursor.getColumnIndex(
                    ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY);

            do {
                name = cursor.getString(nameColumn);
                if(name != null && !name.equals("")) {
                    Friend friend = new Friend();
                    friend.setName(name);
                    friendList.add(friend);
                    Log.i("friend", name);
                }

            }while (cursor.moveToNext());
        }
    }

    public static void setFriendList(ArrayList<Friend> friendList) {
        FriendsUtils.friendList = friendList;
    }
}
