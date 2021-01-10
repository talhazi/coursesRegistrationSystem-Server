package bgu.spl.net.com.Client;

import bgu.spl.net.com.Server.Acknowledgement;
import bgu.spl.net.com.Server.Error;
import bgu.spl.net.com.Server.ServerFace;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

public class adminPrintCourses extends ClientFace {
    private int courseNum;

    public adminPrintCourses(int courseNum) {
        this.courseNum = courseNum;
    }

    @Override
    public String execute(int connectionId, Database data)  {
        ServerFace answer;
        String output;
        String userName =  data.getConnectionstoNames(connectionId);
        if (userName == null) {
            answer = new Error();
            output = answer.msgForClient("7", "you have to register first");
            return output;
        } else {
            User user=data.getUser(userName);
            if (!user.isActive()) {
                answer = new Error();
                output = answer.msgForClient("7", "you have to login first");
                return output;
            } else if (!user.isAdmin()) {
                answer = new Error();
                output = answer.msgForClient("7", "this action available to admin only");
                return output;
            } else if (data.getCourse(courseNum) == null) {
                answer = new Error();
                output = answer.msgForClient("7", "illegal course number");
                return output;
            }

                answer = new Acknowledgement();
                String studentsRegistered = "";
                for (User student : data.getCourse(courseNum).getStudentsRegistered()) {
                    studentsRegistered = studentsRegistered + student.getName() + ",";
                }
                if (!studentsRegistered.equals(""))
                    studentsRegistered = studentsRegistered.substring(0, studentsRegistered.length() - 1);
                String msg = "Course: " + "(" + courseNum + ") " + data.getCourse(courseNum).getCourseName() + "&"
                        + "Seats Available: " + ((data.getCourse(courseNum).getCourseCapacity())-(data.getCourse(courseNum).getStudentsRegistered().size())) + "/" + data.getCourse(courseNum).getCourseCapacity() + "&"
                        + "Students Registered: " + "[" + studentsRegistered + "]";
            output = answer.msgForClient("7", msg);
            return output;
        }
    }
}