package bgu.spl.net.impl.messages;

public class AckMessage extends Message {

    private short messageOpcode;

    public AckMessage(short messageOpcode)
    {
        super((short)10);
        this.messageOpcode=messageOpcode;
    }

    public short getMessageOpcode(){return messageOpcode;}
    public String toString(){
        short opcode=getOpCode();
        String output=String.valueOf(opcode);
        short messageOpcode=getMessageOpcode();
        output+=messageOpcode;
        return output;
    }
}
