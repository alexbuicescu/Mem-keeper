package example.com.memkeeper.Utils;

import java.util.ArrayList;
import java.util.List;

import example.com.memkeeper.POJO.Friend;

/**
 * Created by Alexandru on 04-Apr-15.
 */
public class FriendsUtils {
    private static ArrayList<Friend> friendList;

    public static ArrayList<Friend> getFriendList() {
        friendList = new ArrayList<>();
        Friend f = new Friend();
        f.setName("nume1");
        friendList.add(f);
        friendList.add(f);
        friendList.add(f);
        return friendList;
    }

    public static void setFriendList(ArrayList<Friend> friendList) {
        FriendsUtils.friendList = friendList;
    }
}
