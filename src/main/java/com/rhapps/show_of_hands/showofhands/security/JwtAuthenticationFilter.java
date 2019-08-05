package com.rhapps.show_of_hands.showofhands.security;

import com.rhapps.show_of_hands.showofhands.config.SecurityConfiguration;
import com.rhapps.show_of_hands.showofhands.service.CustomUserDetailService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.rhapps.show_of_hands.showofhands.security.SecurityConstants.*;

//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private AuthenticationManager authenticationManager;
//
//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
//            Users user = new ObjectMapper().readValue(request.getInputStream(), Users.class);
//            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authResult) throws IOException, ServletException {
//        ZonedDateTime expirationTimeUTC = ZonedDateTime.now(ZoneOffset.UTC).plus(EXPIRATION_TIME, ChronoUnit.MILLIS);
//        String token = Jwts.builder().setSubject(((CustomUserDetails)authResult.getPrincipal()).getUsername()).setExpiration(Date.from(expirationTimeUTC.toInstant()))
//                .signWith(SignatureAlgorithm.HS256,SECRET)
//                .compact();
//        response.getWriter().write(token);
//        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
//    }
//}
//
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailService customUserDetailsService;

//    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailService customUserDetailsService) {
//        this.tokenProvider = tokenProvider;
//        this.customUserDetailsService = customUserDetailsService;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("In FILTER : ");

        try {
            String jwt = getJwtFromRequest(httpServletRequest);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // String username = tokenProvider.getUsernameFromJWT(jwt);

                //UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                //UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                UsernamePasswordAuthenticationToken usernamePasswordAuth = getAuthenticationToken(httpServletRequest);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuth);
                // authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                // SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("IN FILTER: " + SecurityConfiguration.getUser());
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_STRING);
        System.out.println("TOKEN RECEIVED: " + bearerToken);
        if (bearerToken == null) return null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.replace(TOKEN_PREFIX, "");
        }
        return null;
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token == null) return null;
        String username = Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        System.out.println("JWT AUTHORIZATION: " + userDetails.getUsername() + " " + userDetails.getPassword());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = username != null ? new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()) : null;
        System.out.println("JWT AUTHORIZATION: " + usernamePasswordAuthenticationToken);
        return usernamePasswordAuthenticationToken;
    }
}
