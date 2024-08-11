package ru.stmlabs.ticketservice.service.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.stmlabs.ticketservice.security.JwtUtils;
import ru.stmlabs.ticketservice.security.MyUserDetailsService;
import ru.stmlabs.ticketservice.service.AuthService;
@Slf4j
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
    public String login(String username, String password) {
        log.info("Login attempt for user: " + username);
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username or password is missing for login attempt");
        }
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            log.error("User not found with username: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        if (!encoder.matches(password, userDetails.getPassword())) {
            log.warn("Invalid credentials provided for user: {}", username);
            throw new BadCredentialsException("Invalid credentials");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtUtils.generateJwtToken(userDetails);
        log.info("Successfully logged in user " + username);
        return jwtToken;
    }
}