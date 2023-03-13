package com.springboot.blog.security;

import antlr.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    CustomUserDetailService customUserDetailService;

    // this method runs one time for every request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // get token from the request
        String jwtToken = getJwtTokenFromRequest(request);

        // validate token
        if (jwtToken != null && jwtTokenProvider.validateJwtToken(jwtToken)) {
            // get username from token
            String username = jwtTokenProvider.getUserNameFromJwtToken(jwtToken);

            // load user associated with token

            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            // pass request details to UsernamePasswordAuthenticationToken
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // set spring security
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // invoke next filter in the chain
        filterChain.doFilter(request, response);
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
