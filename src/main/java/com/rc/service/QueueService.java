package com.rc.service;

import com.rc.model.Message;

import java.util.concurrent.TimeUnit;

/**
 * The producer service may connect to this queue service by REST API,
 * or WebSocket or plain TCP/IP socket which is not implemented here
 * for limit of time
 */
public interface QueueService {
  // add documents
  void createQueue(String queueName);
  boolean subscribe(String queueName, MessageSubscriber msgListener);
  boolean publish(String queueName, Message msg);
  // For poll case, it may support push directly too
  Message poll(String queueName);
  Message poll(String queueName, long timeout, TimeUnit timeUnit);
}
