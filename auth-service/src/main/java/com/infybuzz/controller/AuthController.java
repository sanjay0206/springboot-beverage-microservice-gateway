package com.infybuzz.controller;

import com.infybuzz.entity.UserCredEntity;
import com.infybuzz.request.AuthRequest;
import com.infybuzz.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthService service;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public UserCredEntity addNewUser(@RequestBody UserCredEntity user) {
        logger.info("Inside addNewUser: " + user);
        return service.saveUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest) {
        logger.info("Inside getToken: " + authRequest);
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return ResponseEntity.ok(service.generateToken(authRequest.getUsername()));
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token) {
        logger.info("Inside validateToken: " + token);
        return ResponseEntity.ok(service.validateToken(token));
    }
}
