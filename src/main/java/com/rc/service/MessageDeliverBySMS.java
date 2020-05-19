package com.rc.service;

import com.rc.model.Message;

// SMS, AWS SNS, etc
public class MessageDeliverBySMS implements MessageDeliver {

  @Override
  public boolean sendMessage(Message msg) {
    return false;
  }
}
