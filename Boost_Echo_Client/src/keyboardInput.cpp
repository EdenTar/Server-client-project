//
// Created by MAY on 03/01/2022.
//

#include <iostream>
#include "../include/keyboardInput.h"
#include <cstdio>
#include <ctime>
#include <iomanip>

/*REGISTER E 1 11-11-1111
ACK 1
LOGIN E 1 1
ACK 2
REGISTER A 1 11-11-1111
ACK 1
REGISTER S 1 11-11-1111
ACK 1
STAT A|S
*/

keyboardInput::keyboardInput(ConnectionHandler *_connectionHandler) : connectionHandler(_connectionHandler), encdec(ObjectEncoderDecoder()){
}

void keyboardInput::run() {
    //From here we will see the rest of the ehco client implementation:
    std::cout<<"start run"<<std::endl;
    bool login = true;
    while(login){
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);

        //get object
        std::string clientMessage = encodeToString(line);

        // Message clientMessage = getMessageObject(line);
        if (clientMessage.substr(0,2)== "3" )
        {
            login = false;
        }

        /* REGISTER e 1 11-11-1111
         ACK 1
         LOGIN e 1 1
         ACK 2
         REGISTER m 1 11-11-1111
         ACK 1
         FOLLOW 0 m

         REGISTER E 1 11-11-2002
 ACK 1
 LOGIN E 1 1
 ACK 2
 FOLLOW 0 A
 ACK 4 A
 PM A HEY anxiety
         REGISTER A 1 11-11-2002
 ACK 1
 LOGIN A 1 1
         */
        //ENCODE
        if (!connectionHandler->sendLine(clientMessage)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
    }
}
std::string keyboardInput::encodeToString(std::string line){
    std::string findOpcode = line.substr(0, line.find(" "));
    std::string output="";
    if (findOpcode == "REGISTER")
    {
        output+='\0';
        output+='\1';
        std::vector<std::string> vect;
        char delim=' ';
        split(line, delim, vect);
        for(int i=1;(unsigned )i<vect.size();i++){
            output.append(vect[i]);
            output+='\0';
        }
    }
    else if (findOpcode == "LOGIN")
    {
        output+='\0';
        output+='\2';
        std::vector<std::string> vect;
        char delim=' ';
        split(line, delim, vect);
        for(int i=1;(unsigned )i<vect.size();i++){
            output.append(vect[i]);
            output+='\0';
        }
        if(stoi(vect[3])==1)
            output+='\1';
        else
            output+='\0';

    }
    else if (findOpcode == "LOGOUT")
    {
        output+='\0';
        output+='\3';

    }
    else if (findOpcode == "FOLLOW")
    {
        output+='\0';
        output+='\4';
        std::vector<std::string> vect;
        char delim=' ';
        split(line, delim, vect);
        if(stoi(vect[1])==1)
            output+='\1';
        else
            output+='\0';
        for(int i=2;(unsigned )i<vect.size();i++){
            output.append(vect[i]);
        }
        output+='\0';
    }
    else if (findOpcode == "POST")
    {
        output+='\0';
        output+='\5';
        line.erase(0,5);
        output.append(line);
        output+='\0';
    }
    else if (findOpcode == "PM")
    {
        output+='\0';
        output+='\6';
        /*line.erase(0,3);
        output.append(line.substr(0,line.find(" ")));
        line.erase(0,line.find(" ");
        std::vector<std::string> vect;
        char delim=' ';
        split(line, delim, vect);
        for(int i=1;(unsigned )i<vect.size();i++){
            output.append(vect[i]);
            output+='\0';
        }*/

        line.erase(0,3);
        output.append(line.substr(0,line.find(" ")));
        output+='\0';
        line.erase(0,line.find(" ")+1);
        output.append(line);
        output+='\0';

        auto t = std::time(nullptr);
        auto tm = *std::localtime(&t);
        std::ostringstream oss;
        oss << std::put_time(&tm, "%d-%m-%Y %H:%M");
        auto str = oss.str();

        output.append(str);
        output.append("\0");
    }
    else if (findOpcode == "LOGSTAT")
    {
        output+='\0';
        output+='\7';

    }
    else if (findOpcode == "STAT"){

        output.append("08");
        std::vector<std::string> vect;
        char delim=' ';
        split(line, delim, vect);
        for(int i=1;(unsigned )i<vect.size()-1;i++){
            output.append(vect[i]);
            output+='|';
        }
        output.append(vect[vect.size()-1]);
        output+='\0';
    }
    else if(findOpcode=="BLOCK"){
        output+='\1';
        output+='\2';
        std::vector<std::string> vect;
        char delim=' ';
        split(line, delim, vect);
        for(int i=1;(unsigned )i<vect.size();i++){
            output.append(vect[i]);
        }
        output+='\0';
    }
    output=output+';';
    return output;
}
//std::string *keyboardInput::splitString(std::string line,std::string del=" ", std::string* arr[]){
//        int start = 0;
//        int i=0;
//        int end = line.find(del);
//        while (end != -1) {
//            arr[i]= line.substr(start, end - start);
//            i++;
//            start = end + del.size();
//            end = line.find(del, start);
//        }
//    arr[i]= line.substr(start, end - start);
//  }
/* std::string* arr=new  std::string[numOpParm];
int i = 0;
std::stringstream ssin(line);
while (ssin.good() && i < numOpParm){
    ssin >> arr[i];
    ++i;
}
return arr;*/

//NOTIFICATION <”PM”/”Public”> <PostingUser> <Content>
/*std::string keyboardInput::encodeToString(std::string line){
    std::string findOpcode = line.substr(0, line.find(" "));
    //std::string output="";
    char output[line.size()];

    int index=2;
    if (findOpcode == "REGISTER")
    {
        output[0]=0;
        output[1]=1;
        std::vector<std::string> vect;
        char delim=' ';
        split(line, delim, vect);
        for(int i=1;i<vect.size();i++){
            for(int j=0;j<vect[i].size();j++) {
                output[index] = vect[i][j];
                index++;
            }
            output[index]='\0';
            index++;
        }
    }
    else if (findOpcode == "LOGIN") {
        output[0]=0;
        output[1]=2;
        std::vector<std::string> vect;
        char delim = ' ';
        split(line, delim, vect);
        for (int i = 1; i < vect.size() - 1; i++) {
            for (int j = 0; j < vect[i].size(); j++) {
                output[index] = vect[i][j];
                index++;
            }
            output[index] = '\0';
        }
    }

    else if (findOpcode == "FOLLOW")
    {

        std::vector<std::string> vect;
        char delim=' ';
        split(line, delim, vect);
        for(int i=1;i<vect.size();i++){
            output.append(vect[i]);
        }
        output.append("\0");

    }
    else if (findOpcode == "POST")
    {
        line.erase(0,5);
        output.append(line);
        output.append("\0");
    }
    else if (findOpcode == "PM")
    {
        line.erase(0,3);
        output.append(line.substr(0,line.find(" ")));
        output.append("\0");
        line.erase(0,line.find(" "));
        output.append(line);
        output.append("\0");

        auto t = std::time(nullptr);
        auto tm = *std::localtime(&t);
        std::ostringstream oss;
        oss << std::put_time(&tm, "%d-%m-%Y %H:%M");
        auto str = oss.str();

        output.append(str);
        output.append("\0");

    }

    else if (findOpcode == "STAT")
    {
        std::vector<std::string> vect;
        char delim=' ';
        split(line, delim, vect);
        for(int i=1;i<vect.size();i++){
            output.append(vect[i]);
        }
        output.append("\0");
    }
    else if(findOpcode=="BLOCK"){
        std::vector<std::string> vect;
        char delim=' ';
        split(line, delim, vect);
        for(int i=1;i<vect.size();i++){
            output.append(vect[i]);
        }
        output.append("\0");
    }
    output=output+';';
  output[index]=';';
    return output;
}*/

std::vector<std::string> keyboardInput::split(const std::string &s, char delim, std::vector<std::string> &elems) {
    std::stringstream ss(s);
    std::string item;
    while(std::getline(ss, item, delim)) {
        elems.push_back(item);
    }
    return elems;
}



/*Message keyboardInput::getMessageObject(std::string line) {
    Message *finalMessage;
    //find opcode
    std::string findOpcode = line.substr(0, line.find(" "));
    if (findOpcode == "REGISTER")
    {
        std::string username,password,birthday;
        std::string s = line;
        std::string delimiter = " ";
        size_t pos = 0;
        int i=0;
        std::string token;
        while ((pos = s.find(delimiter)) != std::string::npos) {
            token = s.substr(0, pos);
            if (i==1)
                username = token;
            else if (i==2)
                password = token;
            s.erase(0, pos + delimiter.length());
            i++;
        }
        birthday = s;
        finalMessage = new RegisterMessage(username, password, birthday, short(1));
    }
    else if (findOpcode == "LOGIN")
    {
        std::string username,password;
        int captcha;
        std::string s = line;
        std::string delimiter = " ";
        size_t pos = 0;
        int i=0;
        std::string token;
        while ((pos = s.find(delimiter)) != std::string::npos) {
            token = s.substr(0, pos);
            if (i==1)
                username = token;
            else if (i==2)
                password = token;
            s.erase(0, pos + delimiter.length());
            i++;
        }
        captcha = s[0];
        finalMessage = new LoginMessage(username, password, captcha, short(2));
    }
    else if (findOpcode == "LOGOUT")
    {
        finalMessage = new LogoutMessage(short(3));
    }
    else if (findOpcode == "FOLLOW")
    {
        char follow;
        std::string username;
        std::string s = line;
        std::string delimiter = " ";
        size_t pos = 0;
        int i=0;
        std::string token;
        while ((pos = s.find(delimiter)) != std::string::npos) {
            token = s.substr(0, pos);
            if (i==1)
                follow = token[0];
            s.erase(0, pos + delimiter.length());
            i++;
        }
        username = s;
        //added captcha as field in login message(?)
        finalMessage = new Follow_UnfollowMessage(follow, username, short(4));
    }
    else if (findOpcode == "POST")
    {
        std::string s = line;
        std::string delimiter = " ";
        size_t pos = 0;
        std::string token;
        (pos = s.find(delimiter));
        token = s.substr(0, pos);
        s.erase(0, pos + delimiter.length());
        finalMessage = new PostMessage(s,short(5));
    }
    else if (findOpcode == "PM")
    {
        std::string s = line;
        std::string delimiter = " ";
        size_t pos = 0;
        std::string post, username;
        (pos = s.find(delimiter));
        post = s.substr(0, pos);
        s.erase(0, pos + delimiter.length());
        (pos = s.find(delimiter));
        username = s.substr(0, pos);
        s.erase(0, pos + delimiter.length());

        //current time:
        std::time_t rawtime;
        std::tm* timeinfo;
        char currentTime [80];
        std::time(&rawtime);
        timeinfo = std::localtime(&rawtime);
        std::strftime(currentTime,80,"%d-%m-%Y %H:%M",timeinfo);
        std::puts(currentTime);

        finalMessage = new PMMessage(username,s, currentTime ,short(6));
    }
    else if (findOpcode == "LOGSTAT")
    {
        finalMessage = new LogstatMessage(short(7));
    }
    else if (findOpcode == "STAT")
    {
        std::string s = line;
        std::string delimiter = " ";
        size_t pos = 0;
        std::string token;
        (pos = s.find(delimiter));
        token = s.substr(0, pos);
        s.erase(0, pos + delimiter.length());

        //from string to list
        std::list<std::string> usernames;
        while ((pos = s.find(delimiter)) != std::string::npos) {
            token = s.substr(0, pos);
            usernames.push_back(token);
            s.erase(0, pos + delimiter.length());
        }
        usernames.push_back(s);

        finalMessage = new StatsMessage(usernames,short(8));
    }
    else if(findOpcode=="BLOCK"){
        std::string userName = line.substr(line.find(" "));
        finalMessage = new BlockMessage(userName,(short)12);

    }

    return *finalMessage;

}*/




