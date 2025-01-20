package auth.security.jwt;

import auth.constant.InfoConstant;
import auth.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * @author fdse
 */
@Component
public class JWTProvider {

    // This is just an example key - in production, this should be externalized to a config file
    private String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    private Key key;

    private long validityInMilliseconds = 3600000;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(User user) {
        Date now = new Date();
        Date validate = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .subject(user.getUsername())
                .claim(InfoConstant.ROLES, user.getRoles())
                .claim(InfoConstant.ID, user.getUserId())
                .issuedAt(now)
                .expiration(validate)
                .signWith(key)
                .compact();
    }
}