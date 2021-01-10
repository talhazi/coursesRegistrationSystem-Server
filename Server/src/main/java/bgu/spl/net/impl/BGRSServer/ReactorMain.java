package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.MessagingProtocolImpl;
import bgu.spl.net.srv.Reactor;

public class ReactorMain {

    public static void main(String[] args) throws Exception {
        Reactor server = new Reactor(Integer.parseInt(args[1]),Integer.parseInt(args[0]),() ->  new MessagingProtocolImpl(), //protocol factory
               ()->new MessageEncoderDecoderImpl()); //message encoder decoder factory)
        server.serve();
    }
}
