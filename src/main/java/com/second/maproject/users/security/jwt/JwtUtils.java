package com.second.maproject.users.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.second.maproject.users.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
//    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${maProject.app.jwtSecret}")
    private String jwtSecret;

    @Value("${maProject.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${maProject.app.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    public String generateJwtToken(Authentication authentication, HttpServletResponse response) throws IOException {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());

        String access_token = JWT.create().withSubject(user.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        response.setHeader("access_token", access_token);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(access_token);
        Date dateIssued = decodedJWT.getIssuedAt();
        Date dateExpires = decodedJWT.getExpiresAt();

        System.out.println("Access_token issued: " + dateIssued);
        System.out.println("Access_token expires: " + dateExpires);

        return access_token;
    }

    public String generateRefreshJwtToken(Authentication authentication, HttpServletResponse response) throws IOException {
        UserDetailsImpl userDe = (UserDetailsImpl) authentication.getPrincipal();
        Algorithm algorithms = Algorithm.HMAC256(jwtSecret.getBytes());

        String refresh_token = JWT.create()
                .withSubject(userDe.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .sign(algorithms);
        response.setHeader("refresh_token", refresh_token);

        JWTVerifier verifier = JWT.require(algorithms).build();
        DecodedJWT decodedJWT = verifier.verify(refresh_token);
        Date dateExpires = decodedJWT.getExpiresAt();

        System.out.println("Refresh_token expires: " + dateExpires);

        return refresh_token;

    }
}
