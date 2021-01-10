package bgu.spl.net.com.Client;

import bgu.spl.net.srv.Database;

import java.io.IOException;

public abstract class ClientFace<T> {

    public abstract T execute(int connectionId, Database data);

    }
