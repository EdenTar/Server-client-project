//
// Created by MAY on 04/01/2022.
//

#include "../include/NotificationMessage.h"

NotificationMessage::NotificationMessage(char _pm_public,  std::string _postingUser, std::string _content,  short _opCode) : Message(_opCode), pm_public(_pm_public), postingUser(_postingUser), content(_content) {
}

char NotificationMessage::getPm_public() const {
    return pm_public;
}

std::string NotificationMessage::getPostingUser() {
    return postingUser;
}

std::string NotificationMessage::getContent() {
    return content;
}

std::string NotificationMessage::toString() {
    std::string answer="NOTIFICATION ";
    answer.push_back(getPm_public());
    answer.push_back(' ');
    answer.append(getPostingUser());
    answer.push_back(' ');
    answer.append(getContent());
    return answer;
}
