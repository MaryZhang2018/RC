package com.rc.service;

import com.rc.model.Message;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

@Component
public class QueueServiceImpl implements QueueService {
  // The content are in memory per requirement, o/w
  // DB/stores with persistence support should be used
  private Set<String> queueNames; // or topic names
  private Map<String, Set<MessageSubscriber>> subscribers;
  private Map<String, ConcurrentLinkedQueue<Message>> queues;

  // should have config about max queues memory allowed Or
  // other monitoring to prevent OOM
  public QueueServiceImpl() {
    queueNames = new CopyOnWriteArraySet<>();
    // Collections.synchronizedSet(new HashSet<>());
    subscribers = new ConcurrentHashMap<>();
    queues = new ConcurrentHashMap<>();
  }

  @Override
  public void createQueue(String queueName) {
    if(queueNames.contains(queueName)) return;

    queueNames.add(queueName);
    // assume not many subscribers per queue, o/w,
    // other concurrent set like the keySet from ConcurrentHashMap may be used
    subscribers.put(queueName, new CopyOnWriteArraySet<>());
    queues.put(queueName, new ConcurrentLinkedQueue<>());
  }

  @Override
  public boolean publish(String queueName, Message msg) {
    // The controller part should have done authN and authZ checks already
    if(!queueNames.contains(queueName)) {
      // log or exception
      return false;
    }

    ConcurrentLinkedQueue<Message> queue = queues.get(queueName);
    boolean ret = queue.add(msg);
    if(1 == queue.size()) { // add some blockingQueue feature, may per config
      queue.notify();
    }

    return ret;
  }

  @Override
  public Message poll(String queueName) {
    if(!queueNames.contains(queueName)) {
      // log or exception
      return null;
    }

    return queues.get(queueName).poll();
  }

  @Override
  public Message poll(String queueName, long timeout, TimeUnit timeUnit) {
    if(!queueNames.contains(queueName)) {
      // log or exception
      return null;
    }

    ConcurrentLinkedQueue<Message> queue = queues.get(queueName);
    if (!queue.isEmpty() || 0 >= timeout) return queue.poll();

    long remainingTimeInMs = TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
    long waitEndTimeInMs = Instant.now().toEpochMilli() + remainingTimeInMs;

    while((remainingTimeInMs = waitEndTimeInMs - Instant.now().toEpochMilli()) > 0) {
      try {
        queue.wait(remainingTimeInMs);
      } catch (InterruptedException iex) {
        continue; // just continue for now
      }
    }

    return queue.poll();
  }

  // If the distribution service is remote or not in same process of the queueing
  // service, then we don't need the listener here.
  //
  @Override
  public boolean subscribe(String queueName, MessageSubscriber subscriber) {
    if(!queueNames.contains(queueName)) {
      // log or throw exception about unknown queue
      return false;
    }

    // ConcurrentHashMap.newKeySet();
    Set<MessageSubscriber> listeners = subscribers.get(queueName);
    listeners.add(subscriber);

    return false;
  }
}
