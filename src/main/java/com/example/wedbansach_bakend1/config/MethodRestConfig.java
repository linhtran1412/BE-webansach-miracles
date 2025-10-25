//package com.example.wedbansach_bakend1.config;
//
//
//import com.example.wedbansach_bakend1.entity.NguoiDung;
//import com.example.wedbansach_bakend1.entity.TheLoai;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.metamodel.Type;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
//import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
//import org.springframework.http.HttpMethod;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//
//
//@Configuration
//public class MethodRestConfig implements RepositoryRestConfigurer {
//    private String url = "http://localhost:3000";
//    @Autowired
//    private EntityManager entityManager;
//    @Override
//    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
//
//
//        // expose ids
//        // Cho phép trả về id
//        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));
//        // config.exposeIdsFor(TheLoai.class);
//
//        //cors
//cors.addMapping("/**").allowedOrigins(url).allowedMethods("GET", "POST", "PUT", "DELETE");
//        // Chặn các methods
//        HttpMethod[] chanCacPhuongThuc ={
//                HttpMethod.POST,
//                HttpMethod.PUT,
//                HttpMethod.PATCH,
//                HttpMethod.DELETE,
//        };
//        disableHttpMethods(TheLoai.class, config, chanCacPhuongThuc);
//
//        // Chặn các method DELETE
//        HttpMethod[] phuongThucDelete = {
//                HttpMethod.DELETE
//        };
//        disableHttpMethods(NguoiDung.class, config,phuongThucDelete );
//    }
//
//    private void disableHttpMethods(Class c,
//                                    RepositoryRestConfiguration config,
//                                    HttpMethod[] methods){
//        config.getExposureConfiguration()
//                .forDomainType(c)
//                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(methods))
//                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(methods));
//    }
//}


package com.example.wedbansach_bakend1.config;

import com.example.wedbansach_bakend1.entity.NguoiDung;
import com.example.wedbansach_bakend1.entity.Sach;
import com.example.wedbansach_bakend1.entity.TheLoai;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MethodRestConfig implements RepositoryRestConfigurer {
    private String url = "http://localhost:3000"; // Địa chỉ Frontend

    @Autowired
    private EntityManager entityManager;

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // Expose IDs cho tất cả các entity
        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));

        // === CẤU HÌNH CORS CHO SPRING DATA REST ===
        cors.addMapping("/**") // Áp dụng cho mọi endpoint do Spring Data REST quản lý
                .allowedOrigins(url) // Chỉ cho phép origin từ Frontend
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"); // Thêm OPTIONS
        // === KẾT THÚC CẤU HÌNH CORS ===

        // --- Định nghĩa các bộ phương thức cần chặn ---
        HttpMethod[] chanTatCaCrud = { // Chặn Tạo, Sửa, Xóa
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.PATCH,
                HttpMethod.DELETE,
        };

        HttpMethod[] chanXoa = { // Chỉ chặn Xóa
                HttpMethod.DELETE
        };

        // --- Áp dụng chặn ---
        // Chặn Tạo, Sửa, Xóa cho Sach (vì đã có SachAdminController)
        disableHttpMethods(Sach.class, config, chanTatCaCrud);

        // Chặn Tạo, Sửa, Xóa cho TheLoai (theo yêu cầu ban đầu)
        disableHttpMethods(TheLoai.class, config, chanTatCaCrud);

        // Chặn Xóa cho NguoiDung (theo yêu cầu ban đầu)
        disableHttpMethods(NguoiDung.class, config, chanXoa );
    }

    /**
     * Hàm tiện ích để vô hiệu hóa các phương thức HTTP cho một Entity cụ thể.
     */
    private void disableHttpMethods(Class<?> domainType,
                                    RepositoryRestConfiguration config,
                                    HttpMethod[] methodsToDisable){
        config.getExposureConfiguration()
                .forDomainType(domainType)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(methodsToDisable))
                .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(methodsToDisable));
    }
}

