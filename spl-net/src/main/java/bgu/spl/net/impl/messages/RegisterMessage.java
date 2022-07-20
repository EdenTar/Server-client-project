package bgu.spl.net.impl.messages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterMessage extends Message{
    private String username;
    private String password;
    private String birthday;
    public RegisterMessage(String _username, String _password, String _birthday ) throws ParseException {
        super((short)1);
        username=_username;
        password=_password;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        this.birthday = _birthday;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getBirthday(){
        return birthday;
    }
}
