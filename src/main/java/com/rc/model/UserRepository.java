package com.rc.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  // From the old code, not sure the relationship between user and account
  // It's many to one or one to one? Assume many to one per the old API.
  @Query("SELECT u FROM User u WHERE u.accountId = :accountId")
  List<User> findUserByAccountId(@Param("accountId") String accountId);
}
