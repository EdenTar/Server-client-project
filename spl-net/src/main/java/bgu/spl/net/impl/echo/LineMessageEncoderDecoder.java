package bgu.spl.net.impl.echo;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.messages.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class LineMessageEncoderDecoder implements MessageEncoderDecoder<String> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;

    @Override
    public String decodeNextByte(byte nextByte) {
        //notice that the top 128 ascii characters have the same representation as their utf-8 counterparts
        //this allow us to do the following comparison
        if (nextByte == ';') {
            return popString();
        }

        pushByte(nextByte);
        return null; //not a line yet
    }

    @Override
    public byte[] encode(String message) {
        return (message + ";").getBytes(); //uses utf8 by default
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }


    private String popString() {
        //notice that we explicitly requesting that the string will be decoded from UTF-8
        //this is not actually required as it is the default encoding in java.
        String result = new String(bytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        return result;
    }
    public short bytesToShort(byte[] byteArr)

    {

        short result = (short)((byteArr[0] & 0xff) << 8);

        result += (short)(byteArr[1] & 0xff);

        return result;

    }
    public <T> T convertToObject(byte[] str) {

        System.out.println("starting decoding");
        short opcode;
        String op=String.valueOf(str[0])+String.valueOf(str[1]);
        if((char)str[1]=='8'){
            opcode=8;
        }
        else if(Integer.parseInt(op)==12)
            opcode=12;
        else{
            byte[] byteOpcode = new byte[]{str[0], str[1]};
            opcode = bytesToShort(byteOpcode);
        }
        int index=2;
        if(opcode==1){
            String username="";
            String password="";
            String birthday="";
            while (str[index]!='\0') {
                username += (char)str[index];
                index++;
            }
            index++;
            while (str[index]!='\0') {
                password += (char)str[index];
                index++;
            }
            index++;
            int count=0;
            while (count<10) {
                birthday += (char)str[index];
                index++;
                count++;
            }
            try {

                return (T)(new RegisterMessage(username,password,birthday));
            }
            catch (Exception e){
                System.out.println("birthday not legal");
            }

        }
        else if(opcode==2){
            String username="";
            String password="";
            short captcha;
            while (str[index]!='\0') {
                username += (char)str[index];
                index++;
            }
            index++;
            while (str[index]!='\0') {
                password += (char)str[index];
                index++;
            }
            index++;
            captcha=str[index];

            return (T)new LoginMessage(username,password,captcha);

        }
        else if(opcode==3){
            return  (T)new LogoutMessage();

        }
        else if(opcode==4){
            String username="";
            short follow;
            follow=str[index];
            index++;
            while (str[index]!='\0') {
                username += (char)str[index];
                index++;
            }

            return (T)new Follow_UnfollowMessage(follow,username);

        }
        else if(opcode==5){
            String content="";

            while (str[index]!='\0') {
                content += (char)str[index];
                index++;
            }

            return (T)new PostMessage(content);
        }
        else if(opcode==6){
            String username="";
            String content="";
            String date_time="";

            while (str[index]!='\0') {
                username += (char)str[index];
                index++;
            }
            index++;
            while (str[index]!='\0') {
                content += (char)str[index];
                index++;
            }
            index++;
            while (index<str.length && str[index]!='\0') {
                date_time +=(char)str[index];
                index++;
            }
            try {

                return (T)new PMMessage(username,content,date_time);
            }catch (Exception e){
                System.out.println("birthday not legal");
            }

        }
        else if(opcode==7){

            return (T)new LogstatMessage();
        }
        else if(opcode==8){
            LinkedList<String> usernames=new LinkedList<>();
            String username="";
            while (str[index]!='\0') {
                if((char)str[index]!='|' ) {
                    username += (char)str[index];
                    index++;
                }
                else {
                    usernames.add(username);
                    username="";
                    index++;
                }
            }
            usernames.add(username);
            return (T)new StatsMessage(usernames);

        }
        else if(opcode==12){
            String username="";
            while (str[index]!='\0') {
                username += (char)str[index];
                index++;
            }

            return (T)new BlockMessage(username);
        }
        return null;
    }
}
