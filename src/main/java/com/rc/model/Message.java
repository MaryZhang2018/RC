package com.rc.model;

import java.io.Serializable;

public class Message implements Serializable {
  // The destination may carry more info about dispatching
  // like email, SMS, or REST API directly
  // Or above delivery methods can be registered by subscribers at higher level
  // instead
  private String destination; // Queue name or delivery methods?
  // private String deliverMethod; // optional, can be enum
  private String content; // maybe JSON string

  // Lombock may be used
  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
