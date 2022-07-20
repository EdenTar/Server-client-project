package bgu.spl.net.impl.messages;

public class ErrorMessage extends Message{
    private short messageOpCode;
    public ErrorMessage(short _messageOpCode) {
        super((short)11);
        messageOpCode = _messageOpCode;
    }

    public short getMessageOpCode(){return messageOpCode;}

    public String toString(){return "11"+messageOpCode;}
}
