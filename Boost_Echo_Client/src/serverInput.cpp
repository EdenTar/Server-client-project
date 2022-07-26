//
// Created by MAY on 03/01/2022.
//
#include <iostream>
#include "../include/serverInput.h"
#include "../include/connectionHandler.h"
#include "../include/ObjectEncoderDecoder.h"
#include "../include/AckMessage.h"

serverInput::serverInput(ConnectionHandler *_connectionHandler) : connectionHandler(_connectionHandler), encdec(ObjectEncoderDecoder()) {
}

void serverInput::run() {
    bool login = true;
    while(login){
        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        std::string answer;
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end

        if (!connectionHandler->getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        //DECODE
        //int len=answer.length();
        // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
        // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.

        Message*  receivedMessage = encdec.decode(answer);
        std::cout<<receivedMessage->toString()<<std::endl;
        if (receivedMessage->getOpcode()== 10 && ((AckMessage*)&receivedMessage)->getMessageOpCode()==3)
        {
            login = false;
        }
        delete receivedMessage;
    }

}

