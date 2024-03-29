//package com.rhapps.show_of_hands.showofhands.security;
//
//import com.rhapps.show_of_hands.showofhands.config.SecurityConfiguration;
//import com.rhapps.show_of_hands.showofhands.service.CustomUserDetailService;
//import io.jsonwebtoken.Jwts;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//import static com.rhapps.show_of_hands.showofhands.security.SecurityConstants.*;
//
//public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
//
//    private CustomUserDetailService customUserDetailService;
//
//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, CustomUserDetailService customUserDetailService) {
//        super(authenticationManager);
//        this.customUserDetailService = customUserDetailService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//        String header = request.getHeader(HEADER_STRING);
//        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
//            System.out.println("null Header");
//            chain.doFilter(request, response);
//            return;
//        }
//        UsernamePasswordAuthenticationToken usernamePasswordAuth= getAuthenticationToken(request);
//        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuth);
//        chain.doFilter(request, response);
//
//    }
//
//    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
//        String token = request.getHeader(HEADER_STRING);
//        if(token == null) return  null;
//        String username = Jwts.parser().setSigningKey(SECRET)
//                .parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
//        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
//        System.out.println("JWT AUTHORIZATION: " + userDetails.getUsername() + " " + userDetails.getPassword() );
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken  =  username != null ? new UsernamePasswordAuthenticationToken(userDetails,null) : null;
//        System.out.println("JWT AUTHORIZATION: " + usernamePasswordAuthenticationToken);
//        return usernamePasswordAuthenticationToken;
//    }
//}
