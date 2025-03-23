package com.csq.fweb.gateway.controller;

import com.csq.fweb.gateway.entity.RoleResource;
import com.csq.fweb.gateway.mapper.RoleResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{serviceName}/{path}")
    public ResponseEntity<?> forwardGetRequest(@PathVariable String serviceName, @PathVariable String path) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        List<RoleResource> roleResources = roleResourceMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<RoleResource>().eq("role", role));
        boolean hasPermission = roleResources.stream().anyMatch(rr -> rr.getResourceUrl().contains(path));

        if (hasPermission) {
            String url = "http://" + serviceName + "/" + path;
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + getTokenFromAuthentication(authentication));
            HttpEntity<String> entity = new HttpEntity<>(headers);
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } else {
            return ResponseEntity.status(403).body("Forbidden");
        }
    }

    private String getTokenFromAuthentication(Authentication authentication) {
        // 这里需要根据实际情况从 authentication 中获取 token
        return "";
    }
}    