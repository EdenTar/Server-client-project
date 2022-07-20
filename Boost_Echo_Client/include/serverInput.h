//
// Created by MAY on 03/01/2022.
//

#ifndef CONNECTIONHANDLER_CPP_SERVERINPUT_H
#define CONNECTIONHANDLER_CPP_SERVERINPUT_H


#include "connectionHandler.h"
#include "ObjectEncoderDecoder.h"

class serverInput {
private:
    ConnectionHandler * connectionHandler;
    ObjectEncoderDecoder  encdec;
public:
    serverInput (ConnectionHandler *_connectionHandler);
    void run();

};


#endif //CONNECTIONHANDLER_CPP_SERVERINPUT_H