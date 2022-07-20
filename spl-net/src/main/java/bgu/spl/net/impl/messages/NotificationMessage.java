package bgu.spl.net.impl.messages;

public class NotificationMessage extends Message{

    private char pm_public;
    private String postingUser;
    private String content;
    public NotificationMessage(char pm_public,String postingUser,String content) {
        super((short)9);
        this.pm_public=pm_public;
        this.postingUser=postingUser;
        this.content=content;
    }

    public char getPm_public(){return pm_public;}

    public String getPostingUser(){return postingUser;}

    public String getContent(){return content;}

    public String toString(){
        String s="09";
        s+=pm_public;
        s+=postingUser;
        s+='\0';
        s+=content;
        s+='\0';
        return s;}
}
