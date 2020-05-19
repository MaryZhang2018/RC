package com.rc.service;

import com.rc.model.Message;

// Different applications like Chat app can use REST API
// to push the message
public class MessageDeliverByREST implements MessageDeliver {

  @Override
  public boolean sendMessage(Message msg) {
    return false;
  }
}
