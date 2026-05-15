package org.ticketing.user.infrastructure.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserRequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String userIdHeader = request.getHeader("X-User-Id");

        boolean hasAuth = authHeader != null && !authHeader.isBlank();

        log.info(
            "[USER-IN] method={} uri={} query={} authPresent={} authLength={} xUserId={}",
            request.getMethod(),
            request.getRequestURI(),
            request.getQueryString(),
            hasAuth,
            hasAuth ? authHeader.length() : 0,
            userIdHeader
        );

        try {
            filterChain.doFilter(request, response);
        } finally {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            log.info(
                "[USER-OUT] method={} uri={} status={} authenticated={} principal={}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                authentication != null && authentication.isAuthenticated(),
                authentication != null ? authentication.getName() : null
            );
        }
    }
}