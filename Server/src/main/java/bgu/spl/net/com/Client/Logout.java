package bgu.spl.net.com.Client;

import bgu.spl.net.com.Server.Acknowledgement;
import bgu.spl.net.com.Server.Error;
import bgu.spl.net.com.Server.ServerFace;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

public class Logout extends ClientFace {

    public Logout() {
    }

    @Override
    public String execute(int connectionId, Database data)  {
        ServerFace answer;
        String output;
        String userName =  data.getConnectionstoNames(connectionId);
        if (userName == null) {
            answer = new Error();
            output = answer.msgForClient("4", "you are not even registered");
            return output;
        }
        else {
            User user=data.getUser(userName);
            if (!user.isActive()) {
                answer = new Error();
                output = answer.msgForClient("4", "you are not even Logged in!");
                return output;

            }
            answer = new Acknowledgement();
            user.setActive(false);
            output = answer.msgForClient("4", "Logout success!");

        }
        return output;

    }

}