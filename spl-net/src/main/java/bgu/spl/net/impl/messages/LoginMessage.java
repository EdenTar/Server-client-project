package bgu.spl.net.impl.messages;

import java.nio.ByteBuffer;

public class LoginMessage extends Message{

    private String username;
    private String password;
    private short captcha;

    public LoginMessage(String username, String password,short captcha){
        super((short)2);
        this.username=username;
        this.password=password;
        this.captcha=captcha;
    }

    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public short getCaptcha(){return captcha;}


}
