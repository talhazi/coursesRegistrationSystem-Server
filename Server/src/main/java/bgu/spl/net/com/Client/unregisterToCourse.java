package bgu.spl.net.com.Client;

import bgu.spl.net.com.Server.Acknowledgement;
import bgu.spl.net.com.Server.Error;
import bgu.spl.net.com.Server.ServerFace;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

import java.util.List;

public class unregisterToCourse extends ClientFace {
    private int courseNum;

    public unregisterToCourse(int courseNum) {
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
            output = answer.msgForClient("14", "you have to register first");
            return output;
        }
        else {
            if (!user.isActive()) {
                answer = new Error();
                output = answer.msgForClient("14", "you have to login first");
                return output;

            }
            else if (user.isAdmin()) {
                answer = new Error();
                output = answer.msgForClient("14", "admin can not unregister from any course");
                return output;

            }
            else if (data.getCourse(courseNum) == null) {
                answer = new Error();
                output = answer.msgForClient("14", "illegal course number");
                return output;
            }
            else {
                List<Integer> registeredCourses = user.getRegisteredCourses();
                if (!registeredCourses.contains(courseNum)) {
                        answer = new Error();
                        output = answer.msgForClient("14", "failed! you were not registered to this course");
                        return output;

                    }
                answer = new Acknowledgement();
                data.getCourse(courseNum).unregister(user);   //update the course
                user.unregister(courseNum);   //update the user
                output = answer.msgForClient("14", "unregistered from course success!");
                return output;
            }
        }
    }
}