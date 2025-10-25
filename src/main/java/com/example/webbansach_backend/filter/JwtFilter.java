//package com.example.wedbansach_bakend1.filter;
//
//import com.example.wedbansach_bakend1.Service.JwtService;
//import com.example.wedbansach_bakend1.Service.UserService;
//import io.jsonwebtoken.ExpiredJwtException; // Thêm import này
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod; // Thêm import này
//import org.springframework.lang.NonNull; // Thêm import này (tốt hơn cho tham số filterChain)
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException; // Thêm import này
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtService jwtService;
//
//    @Autowired
//    private UserService userService; // Giữ tên này nhất quán với khai báo bean
//
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain) throws ServletException, IOException {
//
//
//
//        // Logic xử lý token cho các request khác (GET, POST, PUT, DELETE...)
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        final String username;
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            // Nếu không có token, cho đi tiếp để các luật security khác xử lý (permitAll hoặc authenticated)
//            // System.out.println("JwtFilter: Không tìm thấy Bearer Token cho " + request.getRequestURI());
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        jwt = authHeader.substring(7);
//        try {
//            username = jwtService.extractUsername(jwt);
//
//            // Nếu có username và chưa có ai được xác thực trong context
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                // System.out.println("JwtFilter: Đang tải UserDetails cho: " + username);
//                UserDetails userDetails = userService.loadUserByUsername(username);
//
//                // System.out.println("JwtFilter: Đang xác thực token cho: " + username);
//                if (jwtService.validateToken(jwt, userDetails)) {
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null, // credentials
//                            userDetails.getAuthorities()
//                    );
//                    authToken.setDetails(
//                            new WebAuthenticationDetailsSource().buildDetails(request)
//                    );
//                    // Xác thực thành công, đặt vào context
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                    // System.out.println("JwtFilter: Đã xác thực thành công user: " + username + " với quyền: " + userDetails.getAuthorities());
//                } else {
//                    System.out.println("JwtFilter: Token không hợp lệ cho user: " + username);
//                }
//            } else {
//                // Có username nhưng đã có Authentication trong context rồi, bỏ qua
//                // Hoặc username là null (từ token lỗi)
//                // System.out.println("JwtFilter: Username null hoặc Authentication đã tồn tại.");
//            }
//
//        } catch (ExpiredJwtException e) {
//            System.err.println("JwtFilter: JWT Token đã hết hạn: " + e.getMessage());
//            // Không set Authentication, cho filter chain đi tiếp, các luật .authenticated() sẽ từ chối sau
//        } catch (UsernameNotFoundException e) {
//            System.err.println("JwtFilter: Không tìm thấy user trong DB: " + e.getMessage());
//            // Không set Authentication
//        } catch (Exception e) {
//            System.err.println("JwtFilter: Lỗi xử lý JWT không mong muốn: " + e.getMessage());
//            // Không set Authentication
//        }
//
//        // Luôn gọi filterChain để đi tiếp, dù có xác thực được hay không
//        // Các luật trong securityConfiguration sẽ quyết định cuối cùng
//        filterChain.doFilter(request, response);
//    }
//}


package com.example.webbansach_backend.filter; // <<< KIỂM TRA LẠI PACKAGE NÀY

import com.example.webbansach_backend.Service.JwtService;
import com.example.webbansach_backend.Service.UserService;
import io.jsonwebtoken.ExpiredJwtException; // Thêm import này
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull; // Thêm import này
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Thêm import này
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // === THÊM LOG URI REQUEST ===
        System.out.println("[JwtFilter] Request URI: " + request.getMethod() + " " + request.getRequestURI());

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("[JwtFilter] No valid Bearer token found in Authorization header."); // Log khi không có token
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwt);
            System.out.println("[JwtFilter] Token received for user: " + username); // Log username

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("[JwtFilter] Loading UserDetails for: " + username); // Log trước khi load
                UserDetails userDetails = userService.loadUserByUsername(username);
                System.out.println("[JwtFilter] UserDetails loaded for: " + username + ", Authorities from UserDetails: " + userDetails.getAuthorities()); // Log quyền từ UserDetails

                System.out.println("[JwtFilter] Validating token for: " + username); // Log trước khi validate
                if (jwtService.validateToken(jwt, userDetails)) {
                    System.out.println("[JwtFilter] Token validation SUCCESSFUL for: " + username); // Log validate thành công
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // credentials
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    // === LOG SAU KHI SET AUTHENTICATION ===
                    System.out.println("[JwtFilter] Authentication SET in SecurityContext for: " + username + ", Authorities SET: " + authToken.getAuthorities());
                    // === KẾT THÚC LOG ===
                } else {
                    System.out.println("[JwtFilter] Token validation FAILED for user: " + username); // Log khi validate fail
                }
            } else if (username != null) {
                System.out.println("[JwtFilter] Authentication already present in SecurityContext for user: " + username); // Log khi đã có authentication
            } else {
                System.out.println("[JwtFilter] Username extracted from token is NULL."); // Log khi username null
            }

        } catch (ExpiredJwtException e) {
            System.err.println("[JwtFilter] JWT Token EXPIRED: " + e.getMessage()); // Log rõ lỗi hết hạn
        } catch (UsernameNotFoundException e) {
            System.err.println("[JwtFilter] USER NOT FOUND in DB during filter: " + e.getMessage()); // Log rõ lỗi không tìm thấy user
        } catch (Exception e) {
            System.err.println("[JwtFilter] UNEXPECTED error processing JWT: " + e.getMessage()); // Log lỗi khác
            // e.printStackTrace(); // In stack trace nếu cần debug sâu hơn
        }

        // Luôn gọi filterChain để đi tiếp
        filterChain.doFilter(request, response);
        System.out.println("[JwtFilter] Filter chain continued for: " + request.getRequestURI()); // Log khi đi tiếp
    }
}
