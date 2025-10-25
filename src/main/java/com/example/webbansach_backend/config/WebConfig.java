//
//package com.example.wedbansach_bakend1.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
////import org.springframework.web.servlet.config.annotation.EnableWebMvc; // Thử bỏ @EnableWebMvc nếu bước sau vẫn lỗi
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
////@EnableWebMvc // Annotation này đôi khi cần, đôi khi gây xung đột, thử cả 2 TH
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        System.out.println("=== Applying Global CORS Configuration ==="); // Thêm log để biết nó có chạy không
//        registry.addMapping("/**") // Áp dụng cho mọi đường dẫn
//                .allowedOrigins("http://localhost:3000") // Frontend origin
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các method cho phép
//                .allowedHeaders("*") // Mọi header
//                .allowCredentials(true); // Cho phép credentials
//    }
//}

package com.example.webbansach_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("=== Applying Global CORS Configuration ===");
        registry.addMapping("/**") // Áp dụng cho mọi đường dẫn
                .allowedOrigins("http://localhost:3000") // <<<--- Đảm bảo đúng địa chỉ Frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các phương thức cho phép
                .allowedHeaders("*") // Cho phép mọi header
                .allowCredentials(true); // Cho phép gửi cookie/authentication header
    }
}