package etu.etu.security.jwt;


import etu.etu.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static final String jwtSecret = "JeuSecret";
    private static final int jwtExpiration = 864000000;


    public String genereJwtToken(Authentication authentication)
    {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512 , jwtSecret)
                .compact();
    }
    public String getPseudoDeJwtToken(String token)
    {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validerJwtToken(String authToken)
    {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e)
        {
            logger.error("Signature de Jwt n'est pas valable" , e.getMessage());
        } catch (MalformedJwtException e)
        {
            logger.error("Jwt n'est pas valable" , e.getMessage());
        } catch (ExpiredJwtException e)
        {
            logger.error("Jwt est  expiré" , e.getMessage());
        } catch (IllegalArgumentException  e)
        {
            logger.error("Jwt est  vide" , e.getMessage());
        }
       return false;
    }

}
