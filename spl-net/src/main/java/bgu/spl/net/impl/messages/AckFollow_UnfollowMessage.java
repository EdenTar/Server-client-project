package bgu.spl.net.impl.messages;

public class AckFollow_UnfollowMessage extends AckMessage{

    private String username;

    public AckFollow_UnfollowMessage(String username){
        super((short)4);
        this.username=username;
    }
    @Override
    public String toString(){
        short opcode=getOpCode();
        String output=String.valueOf(opcode);
        short messageOpcode=getMessageOpcode();
        output+=messageOpcode;
        output+=(username+'\0');
        return output;
    }

}
