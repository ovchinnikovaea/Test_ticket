package ru.stmlabs.ticketservice.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.stmlabs.ticketservice.security.JwtUtils;
import ru.stmlabs.ticketservice.security.MyUserDetailsService;
import ru.stmlabs.ticketservice.service.AuthService;
@Service
public class AuthServiceImpl implements AuthService {
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(MyUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.encoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    @Transactional
    public String login(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        if (!encoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return jwtUtils.generateJwtToken(userDetails);
    }
}
