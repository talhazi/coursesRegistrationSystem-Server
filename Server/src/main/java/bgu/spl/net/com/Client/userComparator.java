package bgu.spl.net.com.Client;
import bgu.spl.net.srv.User;
import java.util.Comparator;

public class userComparator implements Comparator<User> {

    public userComparator() {
    }

    public int compare(User user1, User user2) {
        return user1.getName().compareTo(user2.getName());
    }

}
