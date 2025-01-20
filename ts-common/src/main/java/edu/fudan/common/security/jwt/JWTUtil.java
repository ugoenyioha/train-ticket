package edu.fudan.common.security.jwt;

import edu.fudan.common.exception.TokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.io.Decoders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fdse
 */
public class JWTUtil {

    private JWTUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtil.class);
    private static final String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public static Authentication getJWTAuthentication(ServletRequest request) {
        String token = getTokenFromHeader((HttpServletRequest) request);
        if (token != null && validateToken(token)) {
            UserDetails userDetails = new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return getRole(token).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                }

                @Override
                public String getPassword() {
                    return "";
                }

                @Override
                public String getUsername() {
                    return getUserName(token);
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
            // send to spring security
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }
        return null;
    }

    private static String getUserName(String token) {
        return getClaims(token).getPayload().getSubject();
    }

    private static List<String> getRole(String token) {
        Jws<Claims> claimsJws = getClaims(token);
        return (List<String>) (claimsJws.getPayload().get("roles", List.class));
    }

    private static String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private static boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = getClaims(token);
            return !claimsJws.getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            LOGGER.error("[validateToken][getClaims][Token expired][ExpiredJwtException: {} ]" , e);
            throw new TokenException("Token expired");
        } catch (UnsupportedJwtException e) {
            LOGGER.error("[validateToken][getClaims][Token format error][UnsupportedJwtException: {}]", e);
            throw new TokenException("Token format error");
        } catch (MalformedJwtException e) {
            LOGGER.error("[validateToken][getClaims][Token is not properly constructed][MalformedJwtException: {}]", e);
            throw new TokenException("Token is not properly constructed");
        } catch (SignatureException e) {
            LOGGER.error("[validateToken][getClaims][Signature failure][SignatureException: {}]", e);
            throw new TokenException("Signature failure");
        } catch (IllegalArgumentException e) {
            LOGGER.error("[validateToken][getClaims][Illegal parameter exception][IllegalArgumentException: {}]", e);
            throw new TokenException("Illegal parameter exception");
        }
    }

    private static Jws<Claims> getClaims(String token) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(keyBytes))
            .build()
            .parseSignedClaims(token);
    }
}
