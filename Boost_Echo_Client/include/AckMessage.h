//
// Created by MAY on 04/01/2022.
//

#ifndef CONNECTIONHANDLER_CPP_ACKMESSAGE_H
#define CONNECTIONHANDLER_CPP_ACKMESSAGE_H


#include <string>
#include "Message.h"

class AckMessage : public Message {
private:
    short messageOpcode;
    public:
    AckMessage(short _messageOpcode, short _opCode);
    short getMessageOpCode();
    std::string toString();
};


#endif //CONNECTIONHANDLER_CPP_ACKMESSAGE_H
