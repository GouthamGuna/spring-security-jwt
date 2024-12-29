package in.dev.ggs.securityconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.dev.ggs.exception.JWTAuthTokenExpired;
import in.dev.ggs.exception.JwtAuthTokenMissing;
import in.dev.ggs.util.JwtServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthFilter(JwtServiceImpl jwtService, @Lazy UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Authorization header is missing or invalid.");
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            if (jwtService.validateToken(token)) {
                String username = jwtService.getUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(new ObjectMapper().writeValueAsString(new JwtAuthTokenMissing("Invalid JWT token.")));
                return;
            }
        } catch (ExpiredJwtException e) {
            sendResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendResponse(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(new JWTAuthTokenExpired("Your login token is no longer valid or Token has expired.")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
