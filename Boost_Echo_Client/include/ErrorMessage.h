//
// Created by MAY on 04/01/2022.
//

#ifndef CONNECTIONHANDLER_CPP_ERRORMESSAGE_H
#define CONNECTIONHANDLER_CPP_ERRORMESSAGE_H


#include <string>
#include "Message.h"

class ErrorMessage : public Message{
private:
    short messageOpcode;
public:
    ErrorMessage(short _messageOpcode, short _opCode);
    short getMessageopcode();
    std::string toString();

};


#endif //CONNECTIONHANDLER_CPP_ERRORMESSAGE_H
