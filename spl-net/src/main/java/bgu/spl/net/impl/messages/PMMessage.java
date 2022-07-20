package bgu.spl.net.impl.messages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PMMessage extends Message{


    private String username;
    private String content;
    private String sendingDateTime;

    public PMMessage(String username, String content,String sendingDateTime) throws ParseException {
        super((short)6);
        this.username=username;
        this.content=content;
        this.sendingDateTime = sendingDateTime;
    }

    public String getUsername(){
        return username;
    }

    public String getContent(){
        return content;
    }

    public String setContent(String content){
        return this.content=content;
    }

}
