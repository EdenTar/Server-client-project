//
// Created by ynal6 on 05/01/2022.
//

#ifndef BOOST_ECHO_CLIENT_OBJECTENCODERDECODER_H
#define BOOST_ECHO_CLIENT_OBJECTENCODERDECODER_H



#include <string>
#include "Message.h"

class ObjectEncoderDecoder {
public:
    ObjectEncoderDecoder();
    Message* decode(std::string answer);
    Message* decodeNotification(std::string answer, short opCode);
    Message* decodeAckStatLogstat(std::string answer, short opCode, short messageOpcode);
    Message* decodeAck(std::string answer, short opCode);
    Message* decodeError(std::string answer, short opCode);
   // std::string encode(Message* message);
};


#endif //BOOST_ECHO_CLIENT_OBJECTENCODERDECODER_H
