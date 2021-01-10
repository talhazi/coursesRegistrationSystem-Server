package bgu.spl.net.api;
import bgu.spl.net.com.Client.ClientFace;
import bgu.spl.net.com.Client.Login;
import bgu.spl.net.com.Client.Logout;
import bgu.spl.net.srv.Database;


public class MessagingProtocolImpl<T> implements MessagingProtocol<T> {
    private Database data; // We can get the num of the Id from here of the CH
    private boolean shouldTerminate;
    private int ID;

    public MessagingProtocolImpl(){
        data=Database.getInstance();
        shouldTerminate = false;
        ID= data.getId();
        data.setId();
    }

    @Override
    public T process(T msg) {
        return process((ClientFace)msg);
    }

    public T process(ClientFace msg){
        T respone = (T)msg.execute(ID, data);
        String s=(String)respone;
        String command = s.substring(0,3);
        if (msg instanceof Logout &&command.equals("ACK") ){
            shouldTerminate = true;
        }
        if (msg instanceof Login){
            if(command.equals("ACK")){
                int nameLength = Integer.parseInt(String.valueOf(s.charAt(s.length()-1)));
                int nameStart = s.length() - nameLength -1;
                String userName = s.substring(nameStart,s.length()-1);
                this.ID = data.getNamesToConnections(userName);
            }
        }
        return respone;
    }





    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

}