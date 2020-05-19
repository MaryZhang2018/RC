package com.rc.service;

import java.util.List;

// The subscriber may register multiple delivery methods
// More details to be considered, the delivery methods may be
// encapsulated w/o exposure
public interface MessageSubscriber extends MessageListener {
  List<MessageDeliver> getDeliverMethods();
}
