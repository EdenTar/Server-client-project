package bgu.spl.net.impl.messages;

import bgu.spl.net.impl.User;

public class PostMessage extends Message{
    private String content;
    public PostMessage(String _content) {
        super((short)5);
        content=_content;
    }
    public String getContent(){return content;}
}
