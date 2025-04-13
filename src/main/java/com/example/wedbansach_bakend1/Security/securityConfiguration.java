//package com.example.wedbansach_bakend1.Security;
//
//import com.example.wedbansach_bakend1.Service.UserService;
//import com.example.wedbansach_bakend1.filter.JwtFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//// import org.springframework.web.cors.CorsConfiguration; // Đã comment out, tốt!
//// import java.util.Arrays; // Đã comment out, tốt!
//
//@Configuration
//public class securityConfiguration {
//
//    @Autowired
//    private JwtFilter jwtFilter;
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider(UserService userService){
//        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
//        dap.setUserDetailsService(userService);
//        dap.setPasswordEncoder(passwordEncoder());
//        return dap;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(
//                config -> config
//                        // ----- QUY TẮC ƯU TIÊN TỪ TRÊN XUỐNG -----
//
//                        // 1. LUÔN CHO PHÉP: OPTIONS request (cho CORS)
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//                        // 2. CHO PHÉP PUBLIC (permitAll): Không cần đăng nhập
//                        .requestMatchers(HttpMethod.GET, Endpoints.PUBLIC_GET_ENDPOINS).permitAll() // Các endpoint GET public từ Endpoints.java
//                        .requestMatchers(HttpMethod.POST, Endpoints.PUBLIC_POST_ENDPOINS).permitAll() // Các endpoint POST public từ Endpoints.java (bao gồm /dang-ky, /dang-nhap)
//                        .requestMatchers(HttpMethod.GET, "/api/lookup/**").permitAll() // Cho phép lấy danh sách hình thức GH/TT
//
//                        // 3. YÊU CẦU ĐĂNG NHẬP (authenticated): Cho User thường
//                        .requestMatchers("/gio-hang/**").authenticated() // Tất cả API giỏ hàng
//                        .requestMatchers(HttpMethod.GET, "/api/don-hang/my-orders").authenticated() // Xem đơn hàng của tôi
//                        .requestMatchers(HttpMethod.POST, "/api/checkout/place-order").authenticated() // Đặt hàng
//
//                        // 4. YÊU CẦU QUYỀN (hasAuthority): Cho Admin/Staff
//                        .requestMatchers(HttpMethod.GET, Endpoints.ADMIN_GET_ENDPOINS).hasAuthority("ADMIN") // VD: Xem danh sách người dùng
//                        .requestMatchers(HttpMethod.POST, Endpoints.ADMIN_POST_ENDPOINS).hasAuthority("ADMIN") // VD: Thêm sách (có thể đổi thành STAFF sau)
//                        .requestMatchers(HttpMethod.GET, "/api/don-hang/{id}").hasAuthority("ADMIN") // VD: Xem đơn hàng bất kỳ (Admin)
//                        .requestMatchers(HttpMethod.PUT, "/api/don-hang/{id}").hasAnyAuthority("ADMIN", "STAFF") // VD: Cập nhật đơn hàng (Admin/Staff) - Sẽ cần thêm role STAFF
//                        .requestMatchers(HttpMethod.DELETE, "/api/don-hang/{id}").hasAuthority("ADMIN") // VD: Xóa đơn hàng (Admin)
//                        // --> Thêm các quy tắc cho Admin/Staff khác vào đây (TRƯỚC anyRequest) <--
//
//                        // 5. BẮT TẤT CẢ CÒN LẠI (anyRequest - PHẢI ĐẶT CUỐI CÙNG): Yêu cầu đăng nhập
//                        .anyRequest().authenticated()
//
//        ); // <<<---- Đóng ngoặc của config -> config
//
//        // --- Các cấu hình còn lại ---
//        // 6. CORS: Dùng WebConfig.java (giữ nguyên)
//        /* http.cors(...) */
//
//        // 7. JWT Filter: Thêm bộ lọc JWT vào trước bộ lọc username/password
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//
//        // 8. Session, HTTP Basic, CSRF: Giữ nguyên cấu hình stateless
//        http.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.httpBasic(Customizer.withDefaults());
//        http.csrf(csrf -> csrf.disable());
//
//        return http.build();
//    }
//
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//}


package com.example.wedbansach_bakend1.Security;

import com.example.wedbansach_bakend1.Service.UserService;
import com.example.wedbansach_bakend1.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration; // Đã comment out, tốt!
// import java.util.Arrays; // Đã comment out, tốt!

@Configuration
public class securityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService){
        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
        dap.setUserDetailsService(userService);
        dap.setPasswordEncoder(passwordEncoder());
        return dap;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                config -> config
                        // ----- QUY TẮC ƯU TIÊN TỪ TRÊN XUỐNG -----

                        // 1. LUÔN CHO PHÉP: OPTIONS request (cho CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. CHO PHÉP PUBLIC (permitAll): Không cần đăng nhập
                        .requestMatchers(HttpMethod.GET, Endpoints.PUBLIC_GET_ENDPOINS).permitAll() // Các endpoint GET public từ Endpoints.java [cite: 1186]
                        .requestMatchers(HttpMethod.POST, Endpoints.PUBLIC_POST_ENDPOINS).permitAll() // Các endpoint POST public từ Endpoints.java (bao gồm /dang-ky, /dang-nhap) [cite: 1186]
                        .requestMatchers(HttpMethod.GET, "/api/lookup/**").permitAll() // Cho phép lấy danh sách hình thức GH/TT [cite: 1186]

                        // 3. YÊU CẦU ĐĂNG NHẬP (authenticated): Cho User thường
                        .requestMatchers("/gio-hang/**").authenticated() // Tất cả API giỏ hàng [cite: 1187]
                        .requestMatchers(HttpMethod.GET, "/api/don-hang/my-orders").authenticated() // Xem đơn hàng của tôi [cite: 1188]
                        .requestMatchers(HttpMethod.POST, "/api/checkout/place-order").authenticated() // Đặt hàng [cite: 1188]

                        // 4. YÊU CẦU QUYỀN (hasAuthority): Cho Admin/Staff
                        .requestMatchers(HttpMethod.GET, Endpoints.ADMIN_GET_ENDPOINS).hasAuthority("ADMIN") // VD: Xem danh sách người dùng [cite: 1189]
                        .requestMatchers(HttpMethod.POST, Endpoints.ADMIN_POST_ENDPOINS).hasAuthority("ADMIN") // VD: Thêm sách (POST /sach) [cite: 1189]

                        // === THÊM BẢO MẬT CHO SỬA/XÓA SÁCH (ADMIN) ===
                        .requestMatchers(HttpMethod.PUT, "/sach/**").hasAuthority("ADMIN")     // Bảo vệ API sửa sách (PUT /sach/{id})
                        .requestMatchers(HttpMethod.PATCH, "/sach/**").hasAuthority("ADMIN")  // Bảo vệ API sửa sách (PATCH /sach/{id}, nếu có)
                        .requestMatchers(HttpMethod.DELETE, "/sach/**").hasAuthority("ADMIN") // Bảo vệ API xóa sách (DELETE /sach/{id})
                        // === KẾT THÚC THÊM ===

                        .requestMatchers(HttpMethod.GET, "/api/don-hang/{id}").hasAuthority("ADMIN") // VD: Xem đơn hàng bất kỳ (Admin) [cite: 1189]
                        .requestMatchers(HttpMethod.PUT, "/api/don-hang/{id}").hasAnyAuthority("ADMIN", "STAFF") // VD: Cập nhật đơn hàng (Admin/Staff) - Sẽ cần thêm role STAFF [cite: 1190]
                        .requestMatchers(HttpMethod.DELETE, "/api/don-hang/{id}").hasAuthority("ADMIN") // VD: Xóa đơn hàng (Admin) [cite: 1190]
                        // --> Thêm các quy tắc cho Admin/Staff khác vào đây (TRƯỚC anyRequest) <--

                        // 5. BẮT TẤT CẢ CÒN LẠI (anyRequest - PHẢI ĐẶT CUỐI CÙNG): Yêu cầu đăng nhập
                        .anyRequest().authenticated() // [cite: 1191]

        ); // <<<---- Đóng ngoặc của config -> config

        // --- Các cấu hình còn lại ---
        // 6. CORS: Dùng WebConfig.java (giữ nguyên)
        /* http.cors(...) */

        // 7. JWT Filter: Thêm bộ lọc JWT vào trước bộ lọc username/password
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // [cite: 1192]

        // 8. Session, HTTP Basic, CSRF: Giữ nguyên cấu hình stateless
        http.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // [cite: 1193]
        http.httpBasic(Customizer.withDefaults()); // [cite: 1194]
        http.csrf(csrf -> csrf.disable()); // [cite: 1194]

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // [cite: 1195]
    }
}