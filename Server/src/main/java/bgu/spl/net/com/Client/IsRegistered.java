package bgu.spl.net.com.Client;

import bgu.spl.net.com.Server.Acknowledgement;
import bgu.spl.net.com.Server.Error;
import bgu.spl.net.com.Server.ServerFace;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

import java.util.List;

public class IsRegistered extends ClientFace {
    int courseNum;

    public IsRegistered(int courseNum) {
        this.courseNum = courseNum;
    }

    @Override
    public String execute(int connectionId, Database data)  {

        ServerFace answer;
        String output;
        String userName =  data.getConnectionstoNames(connectionId);
        User user=data.getUser(userName);

        if (user == null) {
            answer = new Error();
            output = answer.msgForClient("9", "you have to register first");
            return output;

        } else {
            if (!user.isActive()) {
                answer = new Error();
                output = answer.msgForClient("9", "you have to login first");
                return output;
            }
            else if (user.isAdmin()) {
                answer = new Error();
                output = answer.msgForClient("9", "sorry! admin can't check this command (not possible even to register to course)");
                return output;
            }
            else if (data.getCourse(courseNum) == null) {
                answer = new Error();
                output = answer.msgForClient("9", "illegal course number");
                return output;

            } else {
                List<Integer> registeredCourses = user.getRegisteredCourses();
                answer = new Acknowledgement();
                if (registeredCourses.contains(courseNum)) {
                    output = answer.msgForClient("9", "REGISTERED");
                    return output;

                }
                output = answer.msgForClient("9", "NOT REGISTERED");

                return output;
            }
        }
    }





}
