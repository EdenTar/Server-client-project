//
// Created by MAY on 05/01/2022.
//

#include "../include/AckMessageUserInfo.h"

AckMessageUserInfo::AckMessageUserInfo(short _age, short _numPosts, short _numFollowers, short _numFollowing) : age(_age), numPosts(_numPosts), numFollowers(_numFollowers), numFollowing(_numFollowing) {
}

short AckMessageUserInfo::getNumPosts() {
    return numPosts;
}

short AckMessageUserInfo::getAge() {
    return age;
}

short AckMessageUserInfo::getNumFollowers() {
    return numFollowers;
}

short AckMessageUserInfo::getNumFollowing() {
    return numFollowing;
}

std::string AckMessageUserInfo::toString() {
    std::string answer="";
    answer.append(std::to_string(getAge()));
    answer.push_back(' ');
    answer.append(std::to_string(getNumPosts()));
    answer.push_back(' ');
    answer.append(std::to_string(getNumFollowers()));
    answer.push_back(' ');
    answer.append(std::to_string(getNumFollowing()));
    return answer;
}
