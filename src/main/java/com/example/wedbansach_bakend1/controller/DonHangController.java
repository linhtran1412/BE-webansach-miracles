package com.example.wedbansach_bakend1.controller;

import com.example.wedbansach_bakend1.dao.DonHangRepository;
import com.example.wedbansach_bakend1.entity.DonHang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/don-hang")
@CrossOrigin("*")
public class DonHangController {

    @Autowired
    private DonHangRepository donHangRepository;

    // Lấy danh sách tất cả đơn hàng
    @GetMapping
    public List<DonHang> getAllDonHang() {
        return donHangRepository.findAll();
    }

    // Lấy thông tin đơn hàng theo ID
    @GetMapping("/{id}")
    public Optional<DonHang> getDonHangById(@PathVariable Long id) {
        return donHangRepository.findById(id);
    }

    // Thêm mới đơn hàng
    @PostMapping
    public DonHang createDonHang(@RequestBody DonHang donHang) {
        return donHangRepository.save(donHang);
    }

    // Cập nhật đơn hàng
    @PutMapping("/{id}")
    public DonHang updateDonHang(@PathVariable Long id, @RequestBody DonHang donHangMoi) {
        return donHangRepository.findById(id).map(donHang -> {
            donHang.setNgayTao(donHangMoi.getNgayTao());
            donHang.setDiaChiMuaHang(donHangMoi.getDiaChiMuaHang());
            donHang.setDiaChiNhanHang(donHangMoi.getDiaChiNhanHang());
            donHang.setTongTienSanPham(donHangMoi.getTongTienSanPham());
            donHang.setChiPhiGiaoHang(donHangMoi.getChiPhiGiaoHang());
            donHang.setChiPhiThanhToan(donHangMoi.getChiPhiThanhToan());
            donHang.setTongTien(donHangMoi.getTongTien());
            return donHangRepository.save(donHang);
        }).orElseGet(() -> donHangRepository.save(donHangMoi));
    }

    // Xóa đơn hàng
    @DeleteMapping("/{id}")
    public void deleteDonHang(@PathVariable Long id) {
        donHangRepository.deleteById(id);
    }
}

