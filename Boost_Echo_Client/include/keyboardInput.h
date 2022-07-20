//
// Created by MAY on 03/01/2022.
//

#ifndef CONNECTIONHANDLER_CPP_KEYBOARDINPUT_H
#define CONNECTIONHANDLER_CPP_KEYBOARDINPUT_H


#include "connectionHandler.h"
#include "Message.h"
#include "ObjectEncoderDecoder.h"

class keyboardInput {
private:
    ConnectionHandler * connectionHandler;
    ObjectEncoderDecoder encdec;
public:
    keyboardInput (ConnectionHandler *_connectionHandler);
    void run ();
    //Message getMessageObject(std::string line);
    std::string encodeToString(std::string string);
    std::vector<std::string> split(const std::string &s, char delim, std::vector<std::string> &elems) ;
};


#endif //CONNECTIONHANDLER_CPP_KEYBOARDINPUT_H