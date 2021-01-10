package bgu.spl.net.com.Client;

import bgu.spl.net.com.Server.Acknowledgement;
import bgu.spl.net.com.Server.Error;
import bgu.spl.net.com.Server.ServerFace;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

import java.util.List;

public class registerToCourse extends ClientFace {
    private int courseNum;


    public registerToCourse(int courseNum) {
        this.courseNum = courseNum;
    }

    @Override
    public String execute(int connectionId, Database data)  {
        ServerFace answer;
        String output;
        String userName =  data.getConnectionstoNames(connectionId);
        List<Integer> kdamCourses = data.getKdamCourses(courseNum);
        List<Integer> registeredCourses;
        if (userName == null) {
            answer = new Error();
            output = answer.msgForClient("5", "you have to register first");
            return output;
        }
        else {
            User user=data.getUser(userName);
            if (!user.isActive()) {
                answer = new Error();
                output = answer.msgForClient("5", "you have to login first");
                return output;
            }
            else if (user.isAdmin()) {
                    answer = new Error();
                    output = answer.msgForClient("5", "admin can not register to any course");
                return output;
                }
            else if (data.getCourse(courseNum) == null) {
                answer = new Error();
                output = answer.msgForClient("5", "illegal course number");
                return output;
            }
            else {
                registeredCourses=data.getUser(userName).getRegisteredCourses();
                if (registeredCourses.contains(courseNum)) {
                    answer = new Error();
                    output = answer.msgForClient("5", "you already registered to this course");
                    return output;
                }
                for (Integer course : kdamCourses){
                    if (!registeredCourses.contains(course)) {
                        answer = new Error();
                        output = answer.msgForClient("5", "you should complete your kdam courses");
                        return output;
                    }
                }
                if (!data.getCourse(courseNum).getPossibleToRegister()){
                    answer = new Error();
                    output = answer.msgForClient("5", "no seats are available in this course");
                    return output;
                }
                answer = new Acknowledgement();
                data.getCourse(courseNum).register(user);   //update the course
                user.register(courseNum);   //update the user
                output = answer.msgForClient("5", "Registered to course success!");

                return output;
            }
        }
    }

}
