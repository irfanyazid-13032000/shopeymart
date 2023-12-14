package com.enigma.shopeymart.security;

import com.enigma.shopeymart.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
//            validasi token jwt
            String headerAuth = request.getHeader("Authorization");
            String token = null;

            if (headerAuth != null && headerAuth.startsWith("Bearer")){
                token = headerAuth.substring(7);
            }

            if (token != null && jwtUtil.verifyJWTToken(token)){
//                set auth ke spring security
                Map<String,String> userInfo = jwtUtil.getUserInfoByToken(token);
                UserDetails userDetails = userService.loadUserById(userInfo.get("userId"));
//                validasi token
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//                menambahkan informasi berupa alamat ip ke hos dalam bentuk security
                authenticationToken.setDetails(new WebAuthenticationDetailsSource());

//                menyimpan auth ke spring context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        filterChain.doFilter(request,response);
    }
}
