package bgu.spl.net.com.Client;

import bgu.spl.net.com.Server.Acknowledgement;
import bgu.spl.net.com.Server.Error;
import bgu.spl.net.com.Server.ServerFace;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

public class myCurrentCourses extends ClientFace {

    public myCurrentCourses() {
    }

    @Override
    public String execute(int connectionId, Database data)  {
        ServerFace answer;
        String output;
        String userName =  data.getConnectionstoNames(connectionId);
        User user=data.getUser(userName);
        if (user == null) {
            answer = new Error();
            output = answer.msgForClient("11", "you should register first");
            return output;

        }
        else {
            if (!user.isActive()) {
                answer = new Error();
                output = answer.msgForClient("11", "you should login first");
                return output;
            }
            else if (user.isAdmin()) {
                answer = new Error();
                output = answer.msgForClient("11", "sorry! admin doesn't have registered courses (not possible register to course)");
                return output;
            }
            answer = new Acknowledgement();
            String Courses = "";
            for (Integer course : user.getRegisteredCourses()) {
                Courses = Courses + course+ ",";
            }
            if (!Courses.equals(""))
                Courses = Courses.substring(0, Courses.length()-1);
            String msg = "["+Courses+"]";
            output = answer.msgForClient("11", msg);

            return output;
        }
    }
}