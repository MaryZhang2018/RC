package com.rc.security.jwt;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Header implements Serializable {
  private String alg;
  private String typ;
  private String cty;
  private String keyId;
  private Map<String, JsonNode> customParams = new HashMap<>();

  public String getAlg() {
    return alg;
  }

  public String getTyp() {
    return typ;
  }

  public String getCty() {
    return cty;
  }

  public String getKeyId() {
    return keyId;
  }

  public Map<String, JsonNode> getCustomParams() {
    return customParams;
  }

  public void setAlg(String alg) {
    this.alg = alg;
  }

  public void setTyp(String typ) {
    this.typ = typ;
  }

  public void setCty(String cty) {
    this.cty = cty;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

  public void setCustomParams(Map<String, JsonNode> customParams) {
    this.customParams = customParams;
  }
}
