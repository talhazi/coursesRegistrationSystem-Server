package bgu.spl.net.com.Client;

import bgu.spl.net.com.Server.Acknowledgement;
import bgu.spl.net.com.Server.Error;
import bgu.spl.net.com.Server.ServerFace;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

public class adminPrintStudentStatus extends ClientFace {
    private String userName;

    public adminPrintStudentStatus(String userName) {
        this.userName = userName;
    }


    @Override
    public String execute(int connectionId, Database data) {
        ServerFace answer;
        String output;
        String userName =  data.getConnectionstoNames(connectionId);
        if (userName == null) {
            answer = new Error();
            output = answer.msgForClient("8", "you have to register first");
            return output;
        } else {
            User user=data.getUser(userName);
            if (!user.isActive()) {
                answer = new Error();
                output = answer.msgForClient("8", "you have to login first");
                return output;
            } else if (!user.isAdmin()) {
                answer = new Error();
                output = answer.msgForClient("8", "this action available to admin only");
                return output;
            }
            else if (data.getUser(this.userName) == null) {
                answer = new Error();
                output = answer.msgForClient("8", "illegal student name");
                return output;
            }
            else if (data.getUser(this.userName).isAdmin()) {
                    answer = new Error();
                    output = answer.msgForClient("8", "not possible to get this data about admin user");
                return output;

            }
            else {
                answer = new Acknowledgement();
                StringBuilder Courses = new StringBuilder();
                for (Integer course : data.getUser(this.userName).getRegisteredCourses()) {
                    Courses.append(course).append(",");
                }
                if (!Courses.toString().equals(""))
                    Courses = new StringBuilder(Courses.substring(0, Courses.length() - 1));
                String msg = "Student: " + this.userName + "&"
                        + "Courses: " + "[" + Courses + "]";
                output = answer.msgForClient("8", msg);
                return output;
            }
        }
    }
}