package com.lms.config;

import com.lms.model.User;
import com.lms.repository.UserRepository;
import com.lms.util.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Extract Google profile
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        // Role from query param
        String role = (String) request.getSession().getAttribute("role");
        if (role == null) role = "LEARNER";

        logger.info(request.getRequestURL().toString());

        logger.info("Role = "+role);

        // Check or create a user
        User existingUser = userRepository.findUserByMail(email);
        if (existingUser == null) {
            User newUser = new User();
            newUser.setEmailId(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setRole(role != null ? role : "LEARNER");
            newUser.setPassword("");
            newUser.setStatus("ACTIVE");
            newUser.setRegisterType("GOOGLE");
            userRepository.add(newUser);
        }

        // Load updated user
        User user = userRepository.findUserByMail(email);
        logger.info("User = "+user.getRole()+" "+user.getId()+" "+user.getFirstName()+" "+user.getLastName()+" "+user.getEmailId()+" "+user.getStatus()+" "+user.getMentorDetailId()+" "+user.getAmount()+" ");


        // Create UserDetails for JWT
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmailId())
                        .password("") // not used
                        .roles(user.getRole())
                        .build();

        // Generate JWT
        String token = jwtTokenUtil.generateToken(userDetails);

        // Redirect to the frontend with required params
        response.sendRedirect("http://localhost:5173/redirect"
                + "?token=" + token
                + "&role=" + user.getRole()
                + "&userId=" + user.getId()
                + "&firstName=" + user.getFirstName()
                + "&lastName=" + user.getLastName());
    }
}