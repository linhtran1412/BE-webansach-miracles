//package com.example.wedbansach_bakend1.Service;
//
//import com.example.wedbansach_bakend1.dao.NguoiDungRepository;
//import com.example.wedbansach_bakend1.dao.QuyenRepository;
//import com.example.wedbansach_bakend1.entity.NguoiDung;
//import com.example.wedbansach_bakend1.entity.Quyen;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class UserServiceImpl implements UserService {
//    private NguoiDungRepository nguoiDungRepository;
//    private QuyenRepository quyenRepository;
//
//    @Autowired
//    public UserServiceImpl(NguoiDungRepository nguoiDungRepository, QuyenRepository quyenRepository) {
//        this.nguoiDungRepository = nguoiDungRepository;
//        this.quyenRepository = quyenRepository;
//    }
//
//    @Override
//    public NguoiDung findByUsername(String tenDangNhap) {
//        return nguoiDungRepository.findByTenDangNhap(tenDangNhap);
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        NguoiDung nguoiDung = findByUsername(username);
//        if (nguoiDung == null) {
//            throw new UsernameNotFoundException("Tài khoản không tồn tại!");
//        }
//        return new User(nguoiDung.getTenDangNhap(), nguoiDung.getMatKhau(), rolesToAuthorities(nguoiDung.getDanhSachQuyen()));
//
//    }
//
//    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Quyen> quyens) {
//        return quyens.stream().map(quyen -> new SimpleGrantedAuthority(quyen.getTenQuyen())).collect(Collectors.toList());
//    }
//}


package com.example.wedbansach_bakend1.Service;

import com.example.wedbansach_bakend1.dao.NguoiDungRepository;
import com.example.wedbansach_bakend1.dao.QuyenRepository;
import com.example.wedbansach_bakend1.entity.NguoiDung;
import com.example.wedbansach_bakend1.entity.Quyen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections; // Import Collections
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private NguoiDungRepository nguoiDungRepository;
    private QuyenRepository quyenRepository;

    @Autowired
    public UserServiceImpl(NguoiDungRepository nguoiDungRepository, QuyenRepository quyenRepository) {
        this.nguoiDungRepository = nguoiDungRepository;
        this.quyenRepository = quyenRepository;
    }

    @Override
    public NguoiDung findByUsername(String tenDangNhap) {
        return nguoiDungRepository.findByTenDangNhap(tenDangNhap);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = findByUsername(username);
        if (nguoiDung == null) {
            System.err.println("[UserServiceImpl] User not found: " + username); // Log lỗi
            throw new UsernameNotFoundException("Tài khoản không tồn tại!");
        }
        // === THÊM LOG ĐỂ XEM QUYỀN TRƯỚC KHI TRẢ VỀ ===
        Collection<? extends GrantedAuthority> authorities = rolesToAuthorities(nguoiDung.getDanhSachQuyen());
        System.out.println("[UserServiceImpl] User found: " + username + ", Authorities loaded: " + authorities); // Log quyền
        // === KẾT THÚC THÊM LOG ===
        // Sửa hàm khởi tạo User để bao gồm trạng thái kích hoạt
        return new User(
                nguoiDung.getTenDangNhap(),
                nguoiDung.getMatKhau(),
                nguoiDung.isDaKichHoat(), // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities // Sử dụng authorities đã log
        );
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(Collection<Quyen> quyens) {
        // === THÊM LOG ĐỂ XEM TÊN QUYỀN TRONG DB ===
        if (quyens == null || quyens.isEmpty()) { // Kiểm tra cả rỗng
            System.out.println("[UserServiceImpl] rolesToAuthorities: Input 'quyens' is null or empty!");
            return Collections.emptyList(); // Trả về danh sách rỗng nếu null hoặc rỗng
        }
        System.out.println("[UserServiceImpl] rolesToAuthorities: Converting roles from DB: " + quyens.stream().map(Quyen::getTenQuyen).collect(Collectors.toList())); // Log tên quyền gốc
        // === KẾT THÚC THÊM LOG ===
        return quyens.stream()
                .map(quyen -> new SimpleGrantedAuthority(quyen.getTenQuyen()))
                .collect(Collectors.toList());
    }
}
