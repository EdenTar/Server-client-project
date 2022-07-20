package bgu.spl.net.impl.rci;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.impl.messages.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class ObjectEncoderDecoder implements MessageEncoderDecoder<Serializable> {

    private byte[] objectBytes=new byte[1<<10];
    private int objectBytesIndex = 0;

    @Override
    public Serializable decodeNextByte(byte nextByte) {
        System.out.println("in the decodeNextByte");
        if(nextByte!=59){
            objectBytes[objectBytesIndex] = nextByte;
            objectBytesIndex++;
            }
        else {
            Serializable result = decode();
            //objectBytes = null;
            objectBytesIndex=0;
            return result;
        }
        return null;
    }


    private Serializable decode() {
        try {
            return (Serializable)convertToObject();
        } catch (Exception ex) {
            throw new IllegalArgumentException("cannot deserialize object", ex);
        }

    }

    private Message convertToObject() {
        System.out.println("starting decoding");
        String input=new String(objectBytes, StandardCharsets.UTF_8);
       // byte[] opcodeByte=input.substring(0,2).getBytes(StandardCharsets.UTF_8);
        short opcode=Short.valueOf(input.substring(0,2));
        input=input.substring(2);
        int index=0;

        if(opcode==1){
            String username="";
            String password="";
            String birthday="";
            while (input.charAt(index)!='0') {
                username += input.charAt(index);
                index++;
            }
            index++;
            while (input.charAt(index)!='0') {
                password += input.charAt(index);
                index++;
            }
            index++;
            int count=0;
            while (count<10) {
                birthday += input.charAt(index);
                index++;
                count++;
            }
            try {
                return new RegisterMessage(username,password,birthday);
            }
            catch (Exception e){
                System.out.println("birthday not legal");
            }

        }
        else if(opcode==2){
            String username="";
            String password="";
            char captcha;
            while (input.charAt(index)!='0') {
                username += input.charAt(index);
                index++;
            }
            index++;
            while (input.charAt(index)!='0') {
                password += input.charAt(index);
                index++;
            }
            index++;
            captcha=input.charAt(index);



        }
        else if(opcode==3){
            return  new LogoutMessage();

        }
        else if(opcode==4){
            String username="";
            char follow;
            follow=input.charAt(index);
            index++;
            while (input.charAt(index)!='0') {
                username += input.charAt(index);
                index++;
            }


        }
        else if(opcode==5){
            String content="";

            while (input.charAt(index)!='0') {
                content += input.charAt(index);
                index++;
            }
            return new PostMessage(content);
        }
        else if(opcode==6){
            String username="";
            String content="";
            String date_time="";

            while (input.charAt(index)!='0') {
                username += input.charAt(index);
                index++;
            }
            index++;
            while (input.charAt(index)!='0') {
                content += input.charAt(index);
                index++;
            }
            index++;
            while (input.charAt(index)!='0') {
                date_time += input.charAt(index);
                index++;
            }
            try {
                    return new PMMessage(username,content,date_time);
            }catch (Exception e){
                System.out.println("birthday not legal");
            }

        }
        else if(opcode==7){
            return new LogstatMessage();
        }
        else if(opcode==8){
            LinkedList<String> usernames=new LinkedList<>();
            String username="";
            while (input.charAt(index)!='0') {
                if(input.charAt(index)!='|') {
                    username += input.charAt(index);
                    index++;
                }
                else {
                    usernames.add(username);
                    username="";
                    index++;
                }
            }
                return new StatsMessage(usernames);

        }
        else if(opcode==12){
            String username="";
            while (input.charAt(index)!='0') {
                username += input.charAt(index);
                index++;
            }
                return new BlockMessage(username);
        }

   return null;
    }

    private short bytesToShort(byte[] bytes)
    {
        short result = (short)((bytes[0] & 0xff) << 8);
        result += (short)(bytes[1] & 0xff);
        return result;
    }

    @Override
    public byte[] encode(Serializable message) {
        return  (convertToString(message) + ";").getBytes();
    }
    private String convertToString(Serializable message){
        short opcode=((Message)message).getOpCode();
        String output=String.valueOf(opcode);

        if(opcode==9){
            output+= ((NotificationMessage)message).getPm_public();
            output+=(((NotificationMessage)message).getPostingUser()+'0');
            output+=(((NotificationMessage)message).getContent()+'0');
            return output;
        }

        else if(opcode==10){
            short messageOpcode=((AckMessage)message).getMessageOpcode();
            output+=messageOpcode;
            if (messageOpcode==1 | messageOpcode==2 |messageOpcode==3 | messageOpcode==5|messageOpcode==6|messageOpcode==12)
                return output;
            else {
                if(messageOpcode==4){
                    output+=(((Follow_UnfollowMessage)message).getUsername()+'0');
                    return output;
                }
                else if(messageOpcode==7 || messageOpcode==8){
                    LinkedList<AckMessageUserInfo> userinfo=((AckLogstat)message).getUserInfos();
                    String info="";
                    for(int i=0;i<userinfo.size();i++){
                        info+=userinfo.get(i).getAge();
                        info+=userinfo.get(i).getNumPosts();
                        info+=userinfo.get(i).getNumFollowers();
                        info+=userinfo.get(i).getNumFollowing();
                        info+="|";
                    }
                    output+=info;
                    return  output;
                }

            }
        }
        else if(opcode==11){
            output+=((ErrorMessage)message).getMessageOpCode();
            return output;
        }


        return null;
    }


    /*private String convertTobytes(Serializable message) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            //placeholder for the object size
           // for (int i = 0; i < 4; i++) {
              //  bytes.write(0);
           // }
           // ObjectOutput out = new ObjectOutputStream(bytes);
            //out.writeObject(message);
            //out.flush();
           return convertToString((Message)message);

            //now write the object size
           // ByteBuffer.wrap(result).putInt(result.length - 4);
            return result;

        } catch (Exception ex) {
            throw new IllegalArgumentException("cannot serialize object", ex);
        }
    }*/

  /*  private String convertToString(Serializable message){
        short opcode=((Message)message).getOpCode();
        byte[] opcodeBytes=shortToBytes(opcode);
        byte[] res=opcodeBytes;

        if(opcode==9){
            byte [] pm_post = new byte[]{(byte)((NotificationMessage)message).getPm_public()};
            res=combineArrays(res,pm_post);
            byte [] postingUser=(((NotificationMessage)message).getPostingUser()+'0').getBytes(StandardCharsets.UTF_8);
            res=combineArrays(res,postingUser);
            byte [] contant=(((NotificationMessage)message).getContent()+'0').getBytes(StandardCharsets.UTF_8);
            return combineArrays(res,contant);
        }

        else if(opcode==10){
            short messageOpcode=((AckMessage)message).getMessageOpcode();
            byte[] messageOpcodeBytes=shortToBytes(messageOpcode);
            res=combineArrays(res,messageOpcodeBytes);

            if (messageOpcode==1 | messageOpcode==2 |messageOpcode==3 | messageOpcode==5|messageOpcode==6)
                return res;
            else {
                if(messageOpcode==4){
                    byte[] username=(((Follow_UnfollowMessage)message).getUsername()+'0').getBytes(StandardCharsets.UTF_8);
                    return combineArrays(res,username);
                }
                else if(messageOpcode==7 || messageOpcode==8){
                    LinkedList<AckMessageUserInfo> userinfo=((AckLogstat)message).getUserInfos();
                    String info="";
                    for(int i=0;i<userinfo.size();i++){
                        info+=userinfo.get(i).getAge();
                        info+=userinfo.get(i).getNumPosts();
                        info+=userinfo.get(i).getNumFollowers();
                        info+=userinfo.get(i).getNumFollowing();
                        info+="|";
                    }
                    byte[] byteInfo=info.getBytes(StandardCharsets.UTF_8);
                    return  combineArrays(res,byteInfo);
                }

            }
        }
        else if(opcode==11){
            short messageOpcode=((ErrorMessage)message).getMessageOpCode();
            byte[] messageOpcodeBytes=shortToBytes(messageOpcode);
            return combineArrays(res,messageOpcodeBytes);
        }


        return null;
    }
    private byte[] combineArrays(byte[] arr1,byte[] arr2){
        byte[] combination = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, combination, 0, arr1.length);
        System.arraycopy(arr2, 0, combination, arr1.length, arr2.length);
        return combination;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }*/

}
