package bgu.spl.net.impl.messages;

public class BlockMessage extends Message{
    private String username;
    public BlockMessage(String username){
        super((short)12);
        this.username=username;
    }

    public String getUsername(){return username;}
}
