package com.cookiegramstudios.cookiegram.auth;


import com.cookiegramstudios.cookiegram.user.User;
import com.cookiegramstudios.cookiegram.user.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * <p>
 * This service loads user-specific data during authentication by retrieving
 * user information from the database and converting it to Spring Security's
 * UserDetails format.
 * </p>
 * <p>
 * The service uses email as the username for authentication purposes,
 * mapping the User entity to Spring Security's expected format.
 * </p>
 *
 * <p><strong>References:</strong></p>
 * <ul>
 *     <li>
 *         <a href="https://java-jedi.medium.com/spring-security-part-iii-custom-userdetailsservice-with-database-authentication-1c39d42f4d8a">
 *             Medium – Spring Security Part III: Custom UserDetailsService with Database Authentication
 *         </a>
 *     </li>
 * </ul>
 * @author Matthew Samaha
 * @date 2026-02-23
 * @version 1.1
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Loads user details by username (email in this case) for authentication.
     *
     * @param username the username identifying the user whose data is required
     * @return UserDetails object containing user authentication information
     * @throws UsernameNotFoundException if no user is found with the provided email
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from database using email as username
        User user = userRepository.findByEmail(username);

        // Throw exception if user not found
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        // Convert User entity to Spring Security UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(getAuthorities(user))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /**
     * Converts the user's role to a Spring Security GrantedAuthority.
     * <p>
     * Spring Security requires roles to be prefixed with "ROLE_".
     * For Sprint 1 authenticated users, valid roles are ADMIN and EMPLOYEE.
     * </p>
     *
     * @param user the user whose role needs to be converted
     * @return collection of GrantedAuthority objects representing the user's role
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String roleName = "ROLE_" + user.getRole().name();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        // Users have one role in Sprint 1
        return Collections.singletonList(authority);
    }


}
