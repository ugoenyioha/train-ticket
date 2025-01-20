package edu.fudan.common.security.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Author: fdse
 */
public class JWTFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JWTFilter.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        logger.info("" + httpServletRequest.getRequestURI());

        try {
            Authentication authentication = JWTUtil.getJWTAuthentication(httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (JwtException e) {
            logger.warning("JWTFilter: Authentication failed for URI: " + httpServletRequest.getRequestURI() + " - " + e.getMessage());
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
