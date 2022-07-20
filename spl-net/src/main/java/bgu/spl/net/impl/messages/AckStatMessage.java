package bgu.spl.net.impl.messages;

import java.util.LinkedList;

public class AckStatMessage extends AckMessage {
    private LinkedList<AckMessageUserInfo> userInfos;
    public AckStatMessage(short messageOpcode, LinkedList<AckMessageUserInfo> userInfos) {
        super(messageOpcode);
        this.userInfos=userInfos;
    }

    public LinkedList<AckMessageUserInfo> getUserInfos(){return userInfos;}
    @Override
    public String toString(){
        short opcode=getOpCode();
        String output=String.valueOf(opcode);
        short messageOpcode=getMessageOpcode();
        output+=messageOpcode;

        LinkedList<AckMessageUserInfo> userinfo=getUserInfos();
        String info="";
        for(int i=0;i<userinfo.size();i++){
            info+=userinfo.get(i).getAge();
            info+=userinfo.get(i).getNumPosts();
            info+=userinfo.get(i).getNumFollowers();
            info+=userinfo.get(i).getNumFollowing();
            info+="\\|";
        }
        output+=info;

        return output;
    }
}