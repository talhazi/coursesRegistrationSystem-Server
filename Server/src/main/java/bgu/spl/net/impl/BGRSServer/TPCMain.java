package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.MessagingProtocolImpl;
import bgu.spl.net.srv.TPCServer;

public class TPCMain {

    public static void main(String[] args) throws Exception {
        TPCServer tpcServer = new TPCServer(Integer.parseInt(args[0]),() ->  new MessagingProtocolImpl(), //protocol factory
                ()->new MessageEncoderDecoderImpl()); //message encoder decoder factory)
        tpcServer.serve();
    }
}
