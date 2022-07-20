//
// Created by MAY on 04/01/2022.
//

#ifndef CONNECTIONHANDLER_CPP_MESSAGE_H
#define CONNECTIONHANDLER_CPP_MESSAGE_H


#include <string>

class Message {

private:
    short opCode;

public:
    Message(short opCode);
    short getOpcode();
    virtual std::string toString();
    virtual ~Message() = default;
};


#endif //CONNECTIONHANDLER_CPP_MESSAGE_H
