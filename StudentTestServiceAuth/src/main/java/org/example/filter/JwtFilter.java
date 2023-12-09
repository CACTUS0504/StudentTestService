package org.example.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        String jwt = null;
        String login = null;

        if(token != null && token.startsWith("Bearer ")) {
            jwt = token.substring(7);
        }

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                JWT.require(Algorithm.HMAC512("SECRET KEY")).build().verify(jwt);
                login = JWT.require(Algorithm.HMAC512("SECRET KEY")).build().verify(jwt).getSubject();
            } catch (TokenExpiredException tokenExpiredException) {
                System.out.println("Токен некорректный");
            } catch (SignatureVerificationException signatureVerificationException) {
                System.out.println("Подпись некорректна");
            } catch (Exception exception) {
                System.out.println("Неверный токен");
            }
            if (login != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(login, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
