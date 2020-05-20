package com.rc.security.jwt;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;
import java.util.List;
import java.util.Map;

//@Data
public class PayLoad {
  private String issuer;
  private String subject;
  private List<String> audience;
  private Date expiresAt;
  private Date notBefore;
  private Date issuedAt;
  private String jwtId;
  private Map<String, JsonNode> claims;

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public List<String> getAudience() {
    return audience;
  }

  public void setAudience(List<String> audience) {
    this.audience = audience;
  }

  public Date getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Date expiresAt) {
    this.expiresAt = expiresAt;
  }

  public Date getNotBefore() {
    return notBefore;
  }

  public void setNotBefore(Date notBefore) {
    this.notBefore = notBefore;
  }

  public Date getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(Date issuedAt) {
    this.issuedAt = issuedAt;
  }

  public String getJwtId() {
    return jwtId;
  }

  public void setJwtId(String jwtId) {
    this.jwtId = jwtId;
  }

  public Map<String, JsonNode> getClaims() {
    return claims;
  }

  public void setClaims(Map<String, JsonNode> claims) {
    this.claims = claims;
  }
}
