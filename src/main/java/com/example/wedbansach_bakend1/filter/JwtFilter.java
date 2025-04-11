package com.example.wedbansach_bakend1.filter;

import com.example.wedbansach_bakend1.Service.JwtService;
import com.example.wedbansach_bakend1.Service.UserService;
import io.jsonwebtoken.ExpiredJwtException; // Thêm import này
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod; // Thêm import này
import org.springframework.lang.NonNull; // Thêm import này (tốt hơn cho tham số filterChain)
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
    private UserService userService; // Giữ tên này nhất quán với khai báo bean

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // --- BỎ QUA REQUEST OPTIONS (CHO CORS PREFLIGHT) ---
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            // System.out.println("JwtFilter: Bỏ qua request OPTIONS: " + request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_OK); // Trả về 200 OK cho OPTIONS
            // Không cần gọi filterChain.doFilter() ở đây nếu muốn dừng hẳn,
            // nhưng gọi tiếp để các filter CORS khác (như của WebConfig) có thể chạy thì tốt hơn
            filterChain.doFilter(request, response);
            return;
        }
        // --- KẾT THÚC BỎ QUA OPTIONS ---

        // Logic xử lý token cho các request khác (GET, POST, PUT, DELETE...)
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Nếu không có token, cho đi tiếp để các luật security khác xử lý (permitAll hoặc authenticated)
            // System.out.println("JwtFilter: Không tìm thấy Bearer Token cho " + request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwt);

            // Nếu có username và chưa có ai được xác thực trong context
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // System.out.println("JwtFilter: Đang tải UserDetails cho: " + username);
                UserDetails userDetails = userService.loadUserByUsername(username);

                // System.out.println("JwtFilter: Đang xác thực token cho: " + username);
                if (jwtService.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // credentials
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    // Xác thực thành công, đặt vào context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    // System.out.println("JwtFilter: Đã xác thực thành công user: " + username + " với quyền: " + userDetails.getAuthorities());
                } else {
                    System.out.println("JwtFilter: Token không hợp lệ cho user: " + username);
                }
            } else {
                // Có username nhưng đã có Authentication trong context rồi, bỏ qua
                // Hoặc username là null (từ token lỗi)
                // System.out.println("JwtFilter: Username null hoặc Authentication đã tồn tại.");
            }

        } catch (ExpiredJwtException e) {
            System.err.println("JwtFilter: JWT Token đã hết hạn: " + e.getMessage());
            // Không set Authentication, cho filter chain đi tiếp, các luật .authenticated() sẽ từ chối sau
        } catch (UsernameNotFoundException e) {
            System.err.println("JwtFilter: Không tìm thấy user trong DB: " + e.getMessage());
            // Không set Authentication
        } catch (Exception e) {
            System.err.println("JwtFilter: Lỗi xử lý JWT không mong muốn: " + e.getMessage());
            // Không set Authentication
        }

        // Luôn gọi filterChain để đi tiếp, dù có xác thực được hay không
        // Các luật trong securityConfiguration sẽ quyết định cuối cùng
        filterChain.doFilter(request, response);
    }
}