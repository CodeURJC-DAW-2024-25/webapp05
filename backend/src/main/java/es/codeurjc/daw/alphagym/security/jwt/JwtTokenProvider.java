package es.codeurjc.daw.alphagym.security.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {
    private Key key;

    @Value("${jwt.secret}") // We get the key from the application.properties file
    private String secretKey;

    @PostConstruct
    public void init() {
        // Decoding the key from Base64 and converting it to a signing key
        this.key = new SecretKeySpec(Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS256.getJcaName());
    }

    /* 
    private static final String SECRET_KEY = "miClaveSecretaSeguraParaJWT123456"; // Debe ser segura y larga
    private final Key key = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY),
            SignatureAlgorithm.HS256.getJcaName());
    */

    public String tokenStringFromHeaders(HttpServletRequest req) {
        String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken == null) {
            throw new IllegalArgumentException("Missing Authorization header");
        }
        if (!bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header does not start with Bearer: " + bearerToken);
        }
        return bearerToken.substring(7);
    }

    private String tokenStringFromCookies(HttpServletRequest request) {
        var cookies = request.getCookies();
        if (cookies == null) {
            throw new IllegalArgumentException("No cookies found in request");
        }

        for (Cookie cookie : cookies) {
            if (TokenType.ACCESS.cookieName.equals(cookie.getName())) {
                String accessToken = cookie.getValue();
                if (accessToken == null) {
                    throw new IllegalArgumentException("Cookie %s has null value".formatted(TokenType.ACCESS.cookieName));
                }

                return accessToken;
            }
        }
        throw new IllegalArgumentException("No access token cookie found in request");
    }

    public Claims validateToken(HttpServletRequest req, boolean fromCookie) {
        var token = fromCookie ? tokenStringFromCookies(req) : tokenStringFromHeaders(req);
        return validateToken(token);
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(TokenType.ACCESS, userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(TokenType.REFRESH, userDetails);
    }

    private String buildToken(TokenType tokenType, UserDetails userDetails) {
        var currentDate = new Date();
        var expiryDate = new Date(System.currentTimeMillis() + tokenType.duration.toMillis());

        return Jwts.builder()
                .claim("roles", userDetails.getAuthorities())
                .claim("type", tokenType.name())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
