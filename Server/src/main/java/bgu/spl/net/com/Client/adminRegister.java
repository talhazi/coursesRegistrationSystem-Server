package bgu.spl.net.com.Client;

import bgu.spl.net.com.Server.Acknowledgement;
import bgu.spl.net.com.Server.Error;
import bgu.spl.net.com.Server.ServerFace;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

public class adminRegister extends ClientFace {
    private String userName;
    private String password;

    public adminRegister(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String execute(int connectionId, Database data)  {
	// assuming users can't register on the same terminal
        ServerFace answer;
        String output;
        String myUserName =  data.getConnectionstoNames(connectionId);
        User myUser = data.getUser(myUserName);
        if (data.getUser(userName) != null) {
            answer = new Error();
            output = answer.msgForClient("1","User already registered...");
        }
        else if (myUser!=null&&myUser.isActive()) {
            answer = new Error();
            output = answer.msgForClient("1","you can't registered because you are logged in");
        }
        else {
            User newUser = new User(userName, password,true);
            data.setClientsInfo(userName, newUser);
            data.setConnectionstoNames(connectionId,userName);
            data.setNamesToConnections(userName,connectionId);
            answer = new Acknowledgement();
            output = answer.msgForClient("1","Registered success!");
        }
        return output;
    }
}
