package bgu.spl.net.impl.messages;

import java.util.LinkedList;

public class StatsMessage extends Message{
    private int opcode;
    private LinkedList<String> usernames;

    public StatsMessage(LinkedList<String> usernames){
        super((short)8);
        this.usernames=usernames;
    }

    public LinkedList<String> getUsernames(){
        return usernames;
    }

}
