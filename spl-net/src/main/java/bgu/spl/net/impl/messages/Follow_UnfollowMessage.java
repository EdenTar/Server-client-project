package bgu.spl.net.impl.messages;

public class Follow_UnfollowMessage extends Message{
    private short follow;
    private String  username;

    public Follow_UnfollowMessage(short follow,String username){
        super((short)4);
        this.follow=follow;
        this.username=username;
    }

    public String getUsername(){return username;}
    public short getFollow(){return follow;}
}
