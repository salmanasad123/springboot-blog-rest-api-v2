package com.springboot.blog.security;

import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("User not found with username or email" + usernameOrEmail);
                });

        org.springframework.security.core.userdetails.User returnValue =
                new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                        mapRolesToAuthorities(user.getRoles()));

        return returnValue;
    }

    // we need to convert our roles we have in user entity into granted authorities because
    // spring userDetails object takes in granted authorities.
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {

        List<SimpleGrantedAuthority> grantedAuthorities = roles.stream()
                .map((Role role) -> {
                    return new SimpleGrantedAuthority(role.getName());
                }).collect(Collectors.toList());

        return grantedAuthorities;
    }
}
