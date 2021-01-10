package bgu.spl.net.com.Client;

import bgu.spl.net.com.Server.Acknowledgement;
import bgu.spl.net.com.Server.ServerFace;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;
import bgu.spl.net.com.Server.Error;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class Login extends ClientFace {
    private String userName;
    private String password;

    public Login(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String execute(int connectionId, Database data) {
        ServerFace answer;
        String output;

        String myUserName =  data.getConnectionstoNames(connectionId);
        User myUser = data.getUser(myUserName);

        User loginUser = data.getUser(userName);
        if (loginUser == null) {
            answer = new Error();
            output = answer.msgForClient( "3", "you have to register first");
            return output;
        }
        else if (myUser!=null&&myUser.isActive()) {
            answer = new Error();
            output = answer.msgForClient("3","you can't login because you are already logged in");
            return output;
        }
       /* else if (myUser==null) {
            answer = new Error();
            output = answer.msgForClient("3","you have to register first");
            return output;
        }*/

        //check if user is already active
        if (loginUser.isActive()) {
            answer = new Error();
            output = answer.msgForClient("3", "you are already logged in");
            return output;
        }
        //check if password is correct
        if (!loginUser.getPassword().equals(this.password)) {
            answer = new Error();
            output = answer.msgForClient("3", "Wrong password");
            return output;
        }
        answer = new Acknowledgement();
        loginUser.setActive(true);
        if (data.getNamesToConnections(userName)!=null){
            int oldID=data.getNamesToConnections(userName);
            loginUser.setID(oldID);
        }
        String msg = "Login success! "+userName+userName.length();
        output =answer.msgForClient("3", msg);
        return output;
    }

    public String getUserName()
    {
        return this.userName;
    }
}
