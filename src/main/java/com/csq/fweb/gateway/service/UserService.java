package com.csq.fweb.gateway.service;

import com.csq.fweb.gateway.entity.User;

import java.util.Optional;

public interface UserService {
    User register(User user);
    Optional<User> findByUsername(String username);
    String generateToken(User user);
    String refreshToken(String oldToken);
}    