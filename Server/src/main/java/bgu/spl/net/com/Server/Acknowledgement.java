package bgu.spl.net.com.Server;

public class Acknowledgement implements ServerFace {
    @Override
    public String msgForClient(String numOPCode, String msg) {
        String output = "ACK " + numOPCode + " " + msg;
        return output;
    }
}
