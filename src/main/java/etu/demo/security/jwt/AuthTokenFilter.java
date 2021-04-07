package etu.demo.security.jwt;



import etu.demo.security.services.UserDetailsServiceImpl;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private static final Logger logger =  LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = parseJwt(httpServletRequest);
            if (jwt != null && jwtUtils.validerJwtToken(jwt))
            {
                String pseudo = jwtUtils.getPseudoDeJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(pseudo);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails
                ,null , userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e)
        {
            logger.error("il ne peut pas mettre les authentications des utilisatuers");
        }
        filterChain.doFilter(httpServletRequest , httpServletResponse);
    }


    private String parseJwt(HttpServletRequest request)
    {
        String headAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headAuth) && headAuth.startsWith("Bearer ")) {
            return headAuth.substring(7, headAuth.length());
        }
        return null;
    }

}
