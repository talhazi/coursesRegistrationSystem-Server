package bgu.spl.net.com.Server;

public class Error implements ServerFace {
    @Override
    public String msgForClient(String numOPCode, String msg) {
        String output = "ERR " + numOPCode + " " + msg;
        return output;
    }


}
