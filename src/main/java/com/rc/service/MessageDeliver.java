package com.rc.service;

import com.rc.model.Message;

public interface MessageDeliver {
  boolean sendMessage(Message msg);
}
