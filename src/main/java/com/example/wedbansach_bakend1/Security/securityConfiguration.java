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
//                        .requestMatchers(HttpMethod.GET, Endpoints.PUBLIC_GET_ENDPOINS).permitAll() // Các endpoint GET public từ Endpoints.java [cite: 1186]
//                        .requestMatchers(HttpMethod.POST, Endpoints.PUBLIC_POST_ENDPOINS).permitAll() // Các endpoint POST public từ Endpoints.java (bao gồm /dang-ky, /dang-nhap) [cite: 1186]
//                        .requestMatchers(HttpMethod.GET, "/api/lookup/**").permitAll() // Cho phép lấy danh sách hình thức GH/TT [cite: 1186]
//
//                        // 3. YÊU CẦU ĐĂNG NHẬP (authenticated): Cho User thường
//                        .requestMatchers("/gio-hang/**").authenticated() // Tất cả API giỏ hàng [cite: 1187]
//                        .requestMatchers(HttpMethod.GET, "/api/don-hang/my-orders").authenticated() // Xem đơn hàng của tôi [cite: 1188]
//                        .requestMatchers(HttpMethod.POST, "/api/checkout/place-order").authenticated() // Đặt hàng [cite: 1188]
//
//                        // 4. YÊU CẦU QUYỀN (hasAuthority): Cho Admin/Staff
//                        .requestMatchers(HttpMethod.GET, Endpoints.ADMIN_GET_ENDPOINS).hasAuthority("ADMIN") // VD: Xem danh sách người dùng [cite: 1189]
//                        .requestMatchers(HttpMethod.POST, Endpoints.ADMIN_POST_ENDPOINS).hasAuthority("ADMIN") // VD: Thêm sách (POST /sach) [cite: 1189]
//
//                        // === THÊM BẢO MẬT CHO SỬA/XÓA SÁCH (ADMIN) ===
//                        .requestMatchers(HttpMethod.PUT, "/sach/**").hasAuthority("ADMIN")     // Bảo vệ API sửa sách (PUT /sach/{id})
//                        .requestMatchers(HttpMethod.PATCH, "/sach/**").hasAuthority("ADMIN")  // Bảo vệ API sửa sách (PATCH /sach/{id}, nếu có)
//                        .requestMatchers(HttpMethod.DELETE, "/sach/**").hasAuthority("ADMIN") // Bảo vệ API xóa sách (DELETE /sach/{id})
//                        // === KẾT THÚC THÊM ===
//
//                        .requestMatchers(HttpMethod.GET, "/api/don-hang/{id}").hasAuthority("ADMIN") // VD: Xem đơn hàng bất kỳ (Admin) [cite: 1189]
//                        .requestMatchers(HttpMethod.PUT, "/api/don-hang/{id}").hasAnyAuthority("ADMIN", "STAFF") // VD: Cập nhật đơn hàng (Admin/Staff) - Sẽ cần thêm role STAFF [cite: 1190]
//                        .requestMatchers(HttpMethod.DELETE, "/api/don-hang/{id}").hasAuthority("ADMIN") // VD: Xóa đơn hàng (Admin) [cite: 1190]
//                        // --> Thêm các quy tắc cho Admin/Staff khác vào đây (TRƯỚC anyRequest) <--
//
//                        // 5. BẮT TẤT CẢ CÒN LẠI (anyRequest - PHẢI ĐẶT CUỐI CÙNG): Yêu cầu đăng nhập
//
//                        .requestMatchers(HttpMethod.GET, "/tai-khoan/thongtin").authenticated() // Yêu cầu phải đăng nhập để lấy thông tin
//                        .anyRequest().authenticated() // [cite: 1191]
//
//        ); // <<<---- Đóng ngoặc của config -> config
//
//        // --- Các cấu hình còn lại ---
//        // 6. CORS: Dùng WebConfig.java (giữ nguyên)
//        /* http.cors(...) */
//
//        // 7. JWT Filter: Thêm bộ lọc JWT vào trước bộ lọc username/password
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // [cite: 1192]
//
//        // 8. Session, HTTP Basic, CSRF: Giữ nguyên cấu hình stateless
//        http.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // [cite: 1193]
//        http.httpBasic(Customizer.withDefaults()); // [cite: 1194]
//        http.csrf(csrf -> csrf.disable()); // [cite: 1194]
//
//        return http.build();
//    }
//
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager(); // [cite: 1195]
//    }
//}


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
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//
//@EnableMethodSecurity
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
//                        // 1. OPTIONS request (CORS)
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//
//                        // 2. PUBLIC (permitAll)
//                        .requestMatchers(HttpMethod.GET, Endpoints.PUBLIC_GET_ENDPOINS).permitAll()
//                        .requestMatchers(HttpMethod.POST, Endpoints.PUBLIC_POST_ENDPOINS).permitAll()
//                        .requestMatchers(HttpMethod.GET, "/api/lookup/**").permitAll()
//                        // Cho phép xem thể loại công khai (nếu chưa có trong Endpoints.PUBLIC_GET_ENDPOINS)
//                        .requestMatchers(HttpMethod.GET, "/the-loai", "/the-loai/**").permitAll()
//
//
//                        // 3. YÊU CẦU ĐĂNG NHẬP (authenticated) - User hoặc Admin đều được
//                        .requestMatchers("/gio-hang/**").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/api/checkout/place-order").authenticated()
//                        .requestMatchers(HttpMethod.GET, "/tai-khoan/thongtin").authenticated()
//                        // === SỬA ĐƯỜNG DẪN API LẤY ĐƠN HÀNG CỦA TÔI ===
//                        .requestMatchers(HttpMethod.GET, "/don-hang/my-orders").authenticated() // Bỏ /api/
//                        // === KẾT THÚC SỬA ===
//
//
//                        // 4. YÊU CẦU QUYỀN ADMIN (hasAuthority("admin"))
//                        .requestMatchers(HttpMethod.GET, Endpoints.ADMIN_GET_ENDPOINS).hasAuthority("admin") // Đảm bảo quyền là "admin" (thường) nếu DB lưu là "admin"
//                        .requestMatchers(HttpMethod.POST, Endpoints.ADMIN_POST_ENDPOINS).hasAuthority("admin") // POST /sach
//
//                        // --- Bảo mật API Sách ---
//                        .requestMatchers(HttpMethod.PUT, "/sach/**").hasAuthority("admin")
//                        .requestMatchers(HttpMethod.PATCH, "/sach/**").hasAuthority("admin")
//                        .requestMatchers(HttpMethod.DELETE, "/sach/**").hasAuthority("admin")
//
//                        // --- Bảo mật API Đơn hàng (Cho Admin) ---
//                        // === SỬA ĐƯỜNG DẪN CHO KHỚP VỚI CONTROLLER ===
//                        .requestMatchers(HttpMethod.GET, "/don-hang").hasAuthority("admin") // GET /don-hang (lấy tất cả)
//                        .requestMatchers(HttpMethod.GET, "/don-hang/{id}").hasAuthority("admin") // GET /don-hang/id
//                        .requestMatchers(HttpMethod.PUT, "/don-hang/{id}").hasAnyAuthority("admin", "STAFF") // PUT /don-hang/id (Thêm role STAFF nếu cần)
//                        .requestMatchers(HttpMethod.DELETE, "/don-hang/{id}").hasAuthority("admin") // DELETE /don-hang/id
//                        .requestMatchers("/api/admin/images/**").hasAuthority("admin")
//                        .requestMatchers("/api/admin/reports/**").hasAuthority("admin")
//
//                        // === KẾT THÚC SỬA ===
//                        // POST /don-hang không cần bảo vệ ở đây nếu chỉ gọi từ service
//
//                        // --- Bảo mật API Thể loại (Cho Admin) ---
//                        .requestMatchers("/api/admin/the-loai/**").hasAuthority("admin") // Bảo vệ API quản lý thể loại
//
//
//                        // 5. BẮT TẤT CẢ CÒN LẠI (anyRequest): Yêu cầu đăng nhập
//                        .anyRequest().authenticated()
//
//        ); // <<<---- Đóng config
//        http.cors(Customizer.withDefaults());
//        // --- Các cấu hình còn lại ---
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        http.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        // http.httpBasic(Customizer.withDefaults());
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
// <<< THÊM IMPORT NÀY >>>
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableMethodSecurity // <<< ĐẢM BẢO CÓ ANNOTATION NÀY
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
                        // 1. OPTIONS request (CORS) - Cho phép tất cả để xử lý preflight
                        .requestMatchers(HttpMethod.OPTIONS , "/**").permitAll()

                        // 2. PUBLIC (permitAll) - Các endpoint ai cũng truy cập được
                        .requestMatchers(HttpMethod.GET , Endpoints.PUBLIC_GET_ENDPOINS ).permitAll() // GET sách, hình ảnh,...
                        .requestMatchers(HttpMethod.POST , Endpoints.PUBLIC_POST_ENDPOINS ).permitAll() // Đăng ký, đăng nhập
                        .requestMatchers(HttpMethod.GET , "/api/lookup/**").permitAll() // API lấy danh mục (giao hàng, thanh toán)
                        .requestMatchers(HttpMethod.GET , "/the-loai", "/the-loai/**").permitAll() // API xem thể loại

                        // 3. YÊU CẦU ĐĂNG NHẬP (authenticated) - Bất kỳ ai đã đăng nhập (User, Staff, Admin)
                        .requestMatchers("/gio-hang/**").authenticated() // Xem, thêm, sửa, xóa giỏ hàng
                        .requestMatchers(HttpMethod.POST , "/api/checkout/place-order").authenticated() // Đặt hàng
                        .requestMatchers(HttpMethod.GET , "/tai-khoan/thongtin").authenticated() // Lấy thông tin tài khoản hiện tại
                        .requestMatchers(HttpMethod.GET , "/don-hang/my-orders").authenticated() // Xem đơn hàng của tôi
                        .requestMatchers("/api/yeu-thich/**").authenticated()
                        // 4. YÊU CẦU QUYỀN ADMIN hoặc STAFF (hasAnyAuthority) - Các chức năng quản lý chung
                        // --- Quản lý Đơn hàng (xem list, xem chi tiết, sửa trạng thái) ---
                        .requestMatchers(HttpMethod.GET, "/don-hang").hasAnyAuthority("admin", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/don-hang/{id}").hasAnyAuthority("admin", "STAFF")
                        .requestMatchers(HttpMethod.PUT, "/don-hang/{id}").hasAnyAuthority("admin", "STAFF")
                        // --- API Quản lý Sách MỚI (Thêm/Sửa qua Controller) ---
                        .requestMatchers("/api/admin/sach/**").hasAnyAuthority("admin", "STAFF")
                        // --- API Upload Ảnh (liên quan đến sách) ---
                        .requestMatchers("/api/admin/images/**").hasAnyAuthority("admin", "STAFF")
                        // --- API Xóa Sách MẶC ĐỊNH (của Spring Data REST) ---
                        .requestMatchers(HttpMethod.DELETE, "/sach/**").hasAnyAuthority("admin", "STAFF") // Cho phép cả Staff xóa

                        // 5. YÊU CẦU QUYỀN ADMIN (Strict - hasAuthority) - Chỉ Admin mới được làm
                        // --- Xóa Đơn hàng ---
                        .requestMatchers(HttpMethod.DELETE , "/don-hang/{id}").hasAuthority("admin")
                        // --- Quản lý Người dùng (xem danh sách, chi tiết) ---
                        .requestMatchers(HttpMethod.GET , Endpoints.ADMIN_GET_ENDPOINS ).hasAuthority("admin")
                        // --- API Báo cáo ---
                        .requestMatchers("/api/admin/reports/**").hasAuthority("admin")
                        // --- API Quản lý Thể loại (Ví dụ) ---
                        .requestMatchers("/api/admin/the-loai/**").hasAuthority("admin")

                        // 6. BẮT TẤT CẢ CÁC YÊU CẦU CÒN LẠI (anyRequest): Phải đăng nhập
                        .anyRequest().authenticated()
        );

        // --- Các cấu hình khác ---
        http.cors(Customizer.withDefaults()); // Áp dụng cấu hình CORS từ WebConfig
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Thêm bộ lọc JWT
        http.sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Không dùng session
        // http.httpBasic(Customizer.withDefaults()); // Đã vô hiệu hóa HTTP Basic
        http.csrf(csrf -> csrf.disable()); // Tắt CSRF (phổ biến với API + JWT)

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
