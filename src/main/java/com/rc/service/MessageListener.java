package com.rc.service;

import com.rc.model.Message;

public interface MessageListener {
  void onMessage(Message msg);
}
