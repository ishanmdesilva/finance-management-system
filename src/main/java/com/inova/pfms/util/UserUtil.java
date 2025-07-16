package com.inova.pfms.util;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.inova.pfms.entity.User;
import com.inova.pfms.exception.ObjectDoesNotExistException;
import com.inova.pfms.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static com.inova.pfms.constants.LogMessages.GET_LOGGED_IN_USER_CALLED;
import static com.inova.pfms.constants.LogMessages.USER_NOT_AUTHENTICATED;
import static com.inova.pfms.constants.LogMessages.GET_LOGGED_IN_USER_COMPLETED;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class UserUtil {

	private static final String USER_NOT_FOUND_WITH_EMAIL = "User not found with email {0}";
    private UserRepository userRepository;
    public User getLoggedInUser() {
        log.info(GET_LOGGED_IN_USER_CALLED);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            log.info(GET_LOGGED_IN_USER_COMPLETED);
            return userRepository.findByEmail(email)
            		.orElseThrow(() -> new ObjectDoesNotExistException(email, MessageFormat.format(USER_NOT_FOUND_WITH_EMAIL, email)));
        } else {
            throw new RuntimeException(USER_NOT_AUTHENTICATED);
        }
    }
}
