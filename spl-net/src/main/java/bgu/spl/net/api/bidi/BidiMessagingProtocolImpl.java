package bgu.spl.net.api.bidi;

import bgu.spl.net.impl.messages.*;
import bgu.spl.net.srv.Manager;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol{

    private Connections connections;
    private int connectionId;
    private boolean isLoggedOut;

    public  BidiMessagingProtocolImpl(){
        connections=null;
    }
    @Override
    public void start(int connectionId, Connections connections) {
        this.connections=connections;
        this.connectionId=connectionId;
        isLoggedOut=false;
    }

    @Override
    public void process(Object message) {
        int opcode=((Message)message).getOpCode();
        Manager manager=Manager.getInstance();
        if(opcode==1)
            manager.registerRequest((RegisterMessage) message,connectionId);
        else if(opcode==2)
            manager.loginRequest((LoginMessage) message,connectionId);
        else if (opcode==3)
            manager.logoutRequest((LogoutMessage)message,connectionId);
        else if (opcode==4)
            manager.follow_unfollowRequest((Follow_UnfollowMessage) message,connectionId);
        else if (opcode==5)
            manager.postRequest((PostMessage) message,connectionId);
        else if (opcode==6)
            manager.pmRequest((PMMessage) message,connectionId);
        else if (opcode==7)
            manager.logstatRequest((LogstatMessage) message,connectionId);
        else if (opcode==8)
            manager.statRequest((StatsMessage) message,connectionId);
        else if (opcode==12)
            manager.blockRequest((BlockMessage) message,connectionId);
    }

    @Override
    public boolean shouldTerminate() {
        if(!isLoggedOut)
            return false;
        return true;
    }

    public void setLoggedOut(){isLoggedOut=false;}
}
