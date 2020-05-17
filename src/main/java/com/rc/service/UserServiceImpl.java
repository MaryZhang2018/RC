package com.rc.service;

import com.rc.model.User;
import com.rc.model.UserRepository;
import org.hibernate.Session;
import org.springframework.data.domain.Example;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Not sure about original logic, usually OAuth2 annotation is used to
   * authorize Rest API access for the API caller, e.g.,
   * @PreAuthorize("hasAuthority('read:user')"),
   * For sure, for multi-tenant case, further check of tenant match may be
   * needed.
   *
   * Guess the authorization check may be done first to filter out users
   * not authorized and then call repo.saveAll() directly once for list of
   * user case.
   *
   * @param accountId the accountId of the user to be accessed
   * @return true if access is allowed, false o/w
   */
  private boolean canAccessUsers(String accountId) {
    Session sess = HibernateUtil.getSession();
    UserAccess ua = sess.getUserAccess(accountId, SecurityContext.getCurrentUserId());
    if (ua != null && ua.permissions.indexOf("READ") > -1) {
      return true;
    }

    return false;
  }

  // If normal access check is not used, another way to check
  // using security context and authorities assuming the accountId
  // is included like company domain
  private boolean canAccessUsersBySC(String accountId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if(auth.isAuthenticated()) {
      // It is not checked which CRUD operations are allowed
      // Just assume all access if the accountId is listed in the authorities
      return auth.getAuthorities().parallelStream().anyMatch(a -> a.getAuthority().contains(accountId));
    }

    return false;
  }

  // An example by using query by example approach
  public List<User> getUsersByExample(String accountId) {

    if(false == canAccessUsers(accountId)) {
      throw new AccessDeniedException("No access rights to the account");
    }

    User user = new User();
    user.setAccountId(accountId);

    Example<User> example = Example.of(user);
    return userRepository.findAll(example);
  }

  @Override
  public List<User> getUsers(String accountId) {
    if(false == canAccessUsers(accountId)) {
      throw new AccessDeniedException("No access rights to the account");
    }

    return userRepository.findUserByAccountId(accountId);
  }

  @Override
  public List<User> saveUsers(List<User> users) {
    // filter out allowed users only first before save
    List<User> allowedUsers = users.parallelStream()
            .filter(u -> canAccessUsers(u.getAccountId())).collect(Collectors.toList());

    return userRepository.saveAll(allowedUsers);
  }

  @Override
  public User saveUser(User user) {
    if(false == canAccessUsers(user.getAccountId())) {
      throw new AccessDeniedException("No access rights to the account");
    }

    return userRepository.save(user);
  }
}
