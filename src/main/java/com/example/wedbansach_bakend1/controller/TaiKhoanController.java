//package com.example.wedbansach_bakend1.controller;
//
//import com.example.wedbansach_bakend1.Security.JwtResponse;
//import com.example.wedbansach_bakend1.Security.LoginRequest;
//import com.example.wedbansach_bakend1.Service.JwtService;
//import com.example.wedbansach_bakend1.Service.TaiKhoanService;
//import com.example.wedbansach_bakend1.Service.UserService;
//import com.example.wedbansach_bakend1.entity.NguoiDung;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.rest.core.annotation.RepositoryRestResource;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//
//
//@RestController
////@CrossOrigin(origins = "http://localhost:3000")
//@RequestMapping("/tai-khoan")
//public class TaiKhoanController {
//    @Autowired
//    private TaiKhoanService taiKhoanService;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private JwtService jwtService;
//
//
//    @PostMapping("dang-ky")
//    public ResponseEntity<?> dangKyNguoiDung(@Validated @RequestBody NguoiDung nguoiDung) {
//        ResponseEntity<?> response=taiKhoanService.dangKyNguoiDung(nguoiDung);
//        return response;
//    }
//    @GetMapping("/kich-hoat")
//    public ResponseEntity<?>kichHoatTaiKhoan (@RequestParam String email,@RequestParam String maKichHoat) {
//        ResponseEntity<?> response=taiKhoanService.kichHoatTaiKHoan(email,maKichHoat);
//        return response;
//    }
//
//
//    @PostMapping("/dang-nhap")
//    public ResponseEntity<?> dangNhap(@RequestBody LoginRequest loginRequest){
//        // Xác thực người dùng bằng tên đăng nhập và mật khẩu
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//            );
//            // Nếu xác thực thành công, tạo token JWT
//            if(authentication.isAuthenticated()){
//                final String jwt = jwtService.generateToken(loginRequest.getUsername());
//                return ResponseEntity.ok(new JwtResponse(jwt));
//            }
//        }catch (AuthenticationException e){
//            // Xác thực không thành công, trả về lỗi hoặc thông báo
//            return ResponseEntity.badRequest().body("Tên đăng nhập hặc mật khẩu không chính xác.");
//        }
//        return ResponseEntity.badRequest().body("Xác thực không thành công.");
//    }
//}




package com.example.wedbansach_bakend1.controller;

// === CÁC IMPORT CẦN THIẾT ===
import com.example.wedbansach_bakend1.Security.JwtResponse;
import com.example.wedbansach_bakend1.Security.LoginRequest;
import com.example.wedbansach_bakend1.Service.JwtService;
import com.example.wedbansach_bakend1.Service.TaiKhoanService;
import com.example.wedbansach_bakend1.Service.UserService;
import com.example.wedbansach_bakend1.dao.NguoiDungRepository; // <<< Phải có
import com.example.wedbansach_bakend1.dto.UserInfoDTO;        // <<< Phải có
import com.example.wedbansach_bakend1.entity.NguoiDung;
import com.example.wedbansach_bakend1.entity.Quyen;            // <<< Phải có
import com.example.wedbansach_bakend1.entity.ThongBao;        // <<< Phải có
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // <<< Phải có
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder; // <<< Phải có
import org.springframework.security.core.userdetails.UserDetails;     // <<< Phải có
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors; // <<< Phải có
import java.util.List;              // <<< Phải có
// === KẾT THÚC IMPORT ===


@RestController
@RequestMapping("/tai-khoan")
public class TaiKhoanController {
    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    // === PHẢI CÓ INJECT NÀY ===
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    // === KẾT THÚC INJECT ===


    @PostMapping("/dang-ky")
    public ResponseEntity<?> dangKyNguoiDung(@Validated @RequestBody NguoiDung nguoiDung) {
        ResponseEntity<?> response=taiKhoanService.dangKyNguoiDung(nguoiDung);
        return response;
    }

    @GetMapping("/kich-hoat")
    public ResponseEntity<?>kichHoatTaiKhoan (@RequestParam String email,@RequestParam String maKichHoat) {
        ResponseEntity<?> response=taiKhoanService.kichHoatTaiKHoan(email,maKichHoat);
        return response;
    }

    @PostMapping("/dang-nhap")
    public ResponseEntity<?> dangNhap(@RequestBody LoginRequest loginRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            if(authentication.isAuthenticated()){
                final String jwt = jwtService.generateToken(loginRequest.getUsername());
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        }catch (AuthenticationException e){
            // Nên trả về 401 cho lỗi xác thực
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Tên đăng nhập hoặc mật khẩu không chính xác."));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Xác thực không thành công."));
    }

    // === PHẢI CÓ API NÀY ===
    @GetMapping("/thongtin")
    public ResponseEntity<?> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Yêu cầu xác thực. Vui lòng đăng nhập lại."));
        }

        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
            if ("anonymousUser".equals(username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ThongBao("Phiên làm việc không hợp lệ."));
            }
        } else {
            System.err.println("Loại Principal không xác định: " + principal.getClass());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi xác định người dùng."));
        }

        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);

        if (nguoiDung == null) {
            System.err.println("Lỗi: Không tìm thấy người dùng trong DB ứng với token hợp lệ: " + username);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ThongBao("Lỗi đồng bộ dữ liệu người dùng."));
        }

        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setMaNguoiDung(nguoiDung.getMaNguoiDung());
        userInfo.setTenDangNhap(nguoiDung.getTenDangNhap());
        userInfo.setEmail(nguoiDung.getEmail());
        userInfo.setHoDem(nguoiDung.getHoDem());
        userInfo.setTen(nguoiDung.getTen());

        List<String> roles = nguoiDung.getDanhSachQuyen()
                .stream()
                .map(Quyen::getTenQuyen) // Lấy tên quyền
                .collect(Collectors.toList());
        userInfo.setRoles(roles);

        return ResponseEntity.ok(userInfo);
    }
    // === KẾT THÚC API ===
}