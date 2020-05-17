package com.rc.model;

import javax.persistence.*;

@Entity
//@Data
//@NoArgsConstructor
@Table(name = "USER")
public class User {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "first_name", nullable=false)
  private String firstName;

  @Column(name = "last_name", nullable=false)
  private String lastName;

  @Column(nullable=false)
  private String email;

  @Column(name="account_id", nullable = false)
  private String accountId;

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  // Comment this out since we don't need use Account for now
  /*
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "account_id")
  private Account account;
  */
}
