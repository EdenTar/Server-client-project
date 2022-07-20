package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.ConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl implements Connections{
    public ConcurrentHashMap<Integer, ConnectionHandler> connections;
    //maybe we need to add disconnected clients

    public ConnectionsImpl(){
        connections=new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, Object msg) {
     if(!connections.containsKey(connectionId))
         return false;

        connections.get(connectionId).send(msg);
        return true;
    }

    @Override
    public void broadcast(Object msg) {
        for(Integer key : connections.keySet()){
            connections.get(key).send(msg);
        }

    }

    @Override
    public void disconnect(int connectionId) {
        connections.remove(connectionId);

    }

    public void connect(int connectionId,ConnectionHandler handler){
        // check if to put nonblocking or blocking connection handler
        connections.put(connectionId,handler);
    }
}
