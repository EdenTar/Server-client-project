package bgu.spl.net.impl.messages;

public abstract class Message implements java.io.Serializable {

    private short opCode;

    public Message(short opCode) {
       this.opCode=opCode;
    }

    public short getOpCode(){
        return opCode;
    }



}
