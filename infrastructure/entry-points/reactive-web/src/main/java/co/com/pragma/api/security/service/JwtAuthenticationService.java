package co.com.pragma.api.security.service;


import co.com.pragma.api.dto.UserPrincipal;
import co.com.pragma.model.solicitudes.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import co.com.pragma.api.security.jwt.JwtProvider;

import java.util.Collection;

import static constants.MessageExceptions.MSG_FORBIDDEN;

@Component
@AllArgsConstructor
public class JwtAuthenticationService {

    private JwtProvider jwtProvider;

    public Mono<Authentication> getAuthentication(String accessToken) {
        try {
            if (jwtProvider.validTToken(accessToken)) {
                Claims claims = jwtProvider.getBody(accessToken);
                Collection<? extends GrantedAuthority> roles = jwtProvider.getRoles(claims);
                String documentNumber = jwtProvider.getDocumentNumber(claims);
                if (claims.getSubject() == null) {
                    return Mono.error(new IllegalArgumentException("Token subject is null"));
                }

                UserPrincipal userPrincipal = new UserPrincipal(claims.getSubject(), documentNumber);
                UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, roles);
                return Mono.just(userToken);
            } else {
                return Mono.error(new InvalidTokenException(MSG_FORBIDDEN));
            }

        } catch (Exception e) {
            return Mono.error(new AuthenticationServiceException("Token validation failed: " + e.getMessage()));
        }

    }
}
