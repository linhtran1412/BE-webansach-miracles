//package com.example.wedbansach_bakend1.Service;
//
//import com.example.wedbansach_bakend1.dao.NguoiDungRepository;
//import com.example.wedbansach_bakend1.dao.SachRepository;
//import com.example.wedbansach_bakend1.dao.SachYeuThichRepository;
//import com.example.wedbansach_bakend1.entity.NguoiDung;
//import com.example.wedbansach_bakend1.entity.Sach;
//import com.example.wedbansach_bakend1.entity.SachYeuThich;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class SachYeuThichService {
//
//    @Autowired
//    private SachYeuThichRepository sachYeuThichRepository;
//
//    @Autowired
//    private NguoiDungRepository nguoiDungRepository;
//
//    @Autowired
//    private SachRepository sachRepository;
//
//    // Lấy người dùng hiện tại
//    private NguoiDung getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);
//        if (nguoiDung == null) {
//            throw new RuntimeException("Người dùng không tồn tại hoặc chưa đăng nhập.");
//        }
//        return nguoiDung;
//    }
//
//    // Lấy danh sách Sách yêu thích của người dùng hiện tại
//    @Transactional // Cần Transactional để load Sach từ SachYeuThich
//    public List<Sach> getWishlist() {
//        NguoiDung currentUser = getCurrentUser();
//        List<SachYeuThich> wishlistItems = currentUser.getDanhSachSachYeuThich();
//        if (wishlistItems == null) {
//            return List.of(); // Trả về list rỗng nếu chưa có
//        }
//        // Lấy ra danh sách các đối tượng Sach từ danh sách SachYeuThich
//        return wishlistItems.stream()
//                .map(SachYeuThich::getSach) // Lấy đối tượng Sach
//                .filter(java.util.Objects::nonNull) // Lọc bỏ trường hợp Sach bị null (dù không nên xảy ra)
//                .collect(Collectors.toList());
//    }
//
//    // Thêm sách vào yêu thích
//    @Transactional
//    public SachYeuThich addToWishlist(int maSach) {
//        NguoiDung currentUser = getCurrentUser();
//        Sach sach = sachRepository.findById(maSach)
//                .orElseThrow(() -> new RuntimeException("Sách không tồn tại với mã: " + maSach));
//
//        // Kiểm tra xem sách đã có trong danh sách yêu thích chưa
//        boolean alreadyExists = currentUser.getDanhSachSachYeuThich() != null &&
//                currentUser.getDanhSachSachYeuThich().stream()
//                        .anyMatch(item -> item.getSach() != null && item.getSach().getMaSach() == maSach);
//
//        if (alreadyExists) {
//            throw new RuntimeException("Sách này đã có trong danh sách yêu thích.");
//        }
//
//        SachYeuThich newItem = new SachYeuThich();
//        newItem.setNguoiDung(currentUser);
//        newItem.setSach(sach);
//
//        return sachYeuThichRepository.save(newItem);
//    }
//
//    // Xóa sách khỏi yêu thích
//    @Transactional
//    public void removeFromWishlist(int maSach) {
//        NguoiDung currentUser = getCurrentUser();
//
//        // Tìm mục SachYeuThich cần xóa
//        Optional<SachYeuThich> itemToRemoveOpt = Optional.empty();
//        if (currentUser.getDanhSachSachYeuThich() != null) {
//            itemToRemoveOpt = currentUser.getDanhSachSachYeuThich().stream()
//                    .filter(item -> item.getSach() != null && item.getSach().getMaSach() == maSach)
//                    .findFirst();
//        }
//
//
//        if (itemToRemoveOpt.isPresent()) {
//            sachYeuThichRepository.delete(itemToRemoveOpt.get());
//        } else {
//            throw new RuntimeException("Sách này không có trong danh sách yêu thích của bạn.");
//        }
//    }
//
//    // Kiểm tra sách có trong yêu thích không
//    public boolean isInWishlist(int maSach) {
//        try {
//            NguoiDung currentUser = getCurrentUser();
//            if (currentUser.getDanhSachSachYeuThich() == null) {
//                return false;
//            }
//            return currentUser.getDanhSachSachYeuThich().stream()
//                    .anyMatch(item -> item.getSach() != null && item.getSach().getMaSach() == maSach);
//        } catch (RuntimeException e) {
//            // Nếu người dùng chưa đăng nhập, getCurrentUser() sẽ ném lỗi, coi như chưa yêu thích
//            return false;
//        }
//    }
//}


package com.example.webbansach_backend.Service;

import com.example.webbansach_backend.dao.NguoiDungRepository;
import com.example.webbansach_backend.dao.SachRepository;
import com.example.webbansach_backend.dao.SachYeuThichRepository;
import com.example.webbansach_backend.dto.SachSimpleDTO;
import com.example.webbansach_backend.entity.HinhAnh;
import com.example.webbansach_backend.entity.NguoiDung;
import com.example.webbansach_backend.entity.Sach;
import com.example.webbansach_backend.entity.SachYeuThich;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SachYeuThichService {

    @Autowired
    private SachYeuThichRepository sachYeuThichRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private SachRepository sachRepository;

    // Lấy người dùng hiện tại
    private NguoiDung getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            throw new RuntimeException("Người dùng chưa đăng nhập.");
        }
        String username = authentication.getName();
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username);
        if (nguoiDung == null) {
            throw new RuntimeException("Người dùng không tồn tại.");
        }
        return nguoiDung;
    }

    // Lấy danh sách yêu thích -> trả DTO gọn
    @Transactional
    public List<SachSimpleDTO> getWishlist() {
        NguoiDung currentUser = getCurrentUser();
        List<SachYeuThich> wishlistItems = currentUser.getDanhSachSachYeuThich();
        if (wishlistItems == null || wishlistItems.isEmpty()) {
            return Collections.emptyList();
        }
        return wishlistItems.stream()
                .map(SachYeuThich::getSach)
                .filter(java.util.Objects::nonNull)
                .map(this::convertToSachSimpleDTO)
                .collect(Collectors.toList());
    }

    // Chuyển đổi Sach -> SachSimpleDTO
    private SachSimpleDTO convertToSachSimpleDTO(Sach sach) {
        SachSimpleDTO dto = new SachSimpleDTO();
        dto.setMaSach(sach.getMaSach());
        dto.setTenSach(sach.getTenSach());
        dto.setGiaBan(sach.getGiaBan());

        String hinhAnhDaiDien = null;
        if (sach.getDanhSachHinhAnh() != null) {
            Optional<HinhAnh> iconOpt = sach.getDanhSachHinhAnh().stream()
                    .filter(HinhAnh::isLaIcon)
                    .findFirst();
            if (iconOpt.isPresent()) {
                hinhAnhDaiDien = iconOpt.get().getDuLieuAnh();
            } else if (!sach.getDanhSachHinhAnh().isEmpty()) {
                hinhAnhDaiDien = sach.getDanhSachHinhAnh().get(0).getDuLieuAnh();
            }
        }
        dto.setHinhAnhDaiDien(hinhAnhDaiDien);
        return dto;
    }

    // Thêm sách vào yêu thích
    @Transactional
    public SachYeuThich addToWishlist(int maSach) {
        NguoiDung currentUser = getCurrentUser();
        Sach sach = sachRepository.findById(maSach)
                .orElseThrow(() -> new RuntimeException("Sách không tồn tại với mã: " + maSach));

        // Ưu tiên kiểm tra bằng repository (nhanh và chính xác)
        boolean exists = sachYeuThichRepository
                .existsByNguoiDung_MaNguoiDungAndSach_MaSach(currentUser.getMaNguoiDung(), maSach);
        if (exists) {
            throw new RuntimeException("Sách này đã có trong danh sách yêu thích.");
        }

        SachYeuThich newItem = new SachYeuThich();
        newItem.setNguoiDung(currentUser);
        newItem.setSach(sach);
        return sachYeuThichRepository.save(newItem);
    }

    // Xóa sách khỏi yêu thích (CHỈNH: xoá theo user + maSach, không quét list)
    @Transactional
    public void removeFromWishlist(int maSach) {
        NguoiDung currentUser = getCurrentUser();

        SachYeuThich item = sachYeuThichRepository
                .findByNguoiDung_MaNguoiDungAndSach_MaSach(currentUser.getMaNguoiDung(), maSach)
                .orElseThrow(() -> new RuntimeException("Sách này không có trong danh sách yêu thích của bạn."));

        sachYeuThichRepository.delete(item); // Xoá thật trong DB
    }

    // Kiểm tra có trong yêu thích không
    public boolean isInWishlist(int maSach) {
        try {
            NguoiDung currentUser = getCurrentUser();
            // dùng repo để kiểm tra nhanh
            return sachYeuThichRepository
                    .existsByNguoiDung_MaNguoiDungAndSach_MaSach(currentUser.getMaNguoiDung(), maSach);
        } catch (RuntimeException e) {
            return false;
        }
    }
}
