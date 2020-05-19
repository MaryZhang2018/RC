package com.rc.service;

import com.rc.model.Message;

// The email server info should be configured by the class
// while the Message should carry the dest email address
public class MessageDeliverByEmail implements MessageDeliver {

  @Override
  public boolean sendMessage(Message msg) {
    // JavaMail API may be used for email the msg
    return false;
  }
}
