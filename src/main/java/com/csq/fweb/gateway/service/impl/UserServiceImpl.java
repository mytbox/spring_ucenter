package com.csq.fweb.gateway.service.impl;

import com.csq.fweb.gateway.entity.User;
import com.csq.fweb.gateway.mapper.UserMapper;
import com.csq.fweb.gateway.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public UserServiceImpl(UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>().eq("username", username)));
    }

    @Override
    public String generateToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
               .setSubject(user.getUsername())
               .claim("role", user.getRole())
               .setIssuedAt(now)
               .setExpiration(expirationDate)
               .signWith(SignatureAlgorithm.HS512, secret)
               .compact();
    }

    @Override
    public String refreshToken(String oldToken) {
        // 解析旧 token
        String username = Jwts.parser()
               .setSigningKey(secret)
               .parseClaimsJws(oldToken)
               .getBody()
               .getSubject();

        // 查找用户
        Optional<User> userOptional = findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 生成新 token
            Date now = new Date();
            Date expirationDate = new Date(now.getTime() + refreshExpiration);

            return Jwts.builder()
                   .setSubject(user.getUsername())
                   .claim("role", user.getRole())
                   .setIssuedAt(now)
                   .setExpiration(expirationDate)
                   .signWith(SignatureAlgorithm.HS512, secret)
                   .compact();
        }
        return null;
    }
}    