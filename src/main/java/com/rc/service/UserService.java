package com.rc.service;

import com.rc.model.User;

import java.util.List;

public interface UserService {
  List<User> getUsers(String accountId);
  List<User> saveUsers(List<User> users);
  User saveUser(User user);
}
