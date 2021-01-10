package bgu.spl.net.com.Client;

import bgu.spl.net.com.Server.Acknowledgement;
import bgu.spl.net.com.Server.Error;
import bgu.spl.net.com.Server.ServerFace;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

import java.util.List;

public class checkKdamCourses extends ClientFace {
    private int courseNum;

    public checkKdamCourses(int courseNum) {
        this.courseNum = courseNum;
    }


    @Override
    public String execute(int connectionId, Database data)  {
        ServerFace answer;
        String output;
        String userName =  data.getConnectionstoNames(connectionId);
        List<Integer> kdamCourses = data.getKdamCourses(courseNum);
        if (userName == null) {
            answer = new Error();
            output = answer.msgForClient("6", "you have to register first");
            return output;

        } else {
            User user=data.getUser(userName);
            if (!user.isActive()) {
                answer = new Error();
                output = answer.msgForClient("6", "you have to login first");
                return output;
            }
            else if (user.isAdmin()) {
                answer = new Error();
                output = answer.msgForClient("6", "sorry! admin can't check kdam courses");
                return output;
            }
            else if (data.getCourse(courseNum) == null) {
                answer = new Error();
                output = answer.msgForClient("6", "illegal course number");
                return output;

            }
            else {
                answer = new Acknowledgement();
                StringBuilder msg = new StringBuilder();
                for (Integer course : kdamCourses) {
                    msg.append(course.toString()).append(",");
                }
                if (!msg.toString().equals(""))
                    msg = new StringBuilder(msg.substring(0, msg.length() - 1));
                output = answer.msgForClient("6", "[" + msg + "]");

                return output;
            }
        }
    }
}
