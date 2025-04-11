
package com.example.wedbansach_bakend1.controller;

import com.example.wedbansach_bakend1.dao.HinhThucGiaoHangRepository;
import com.example.wedbansach_bakend1.dao.HinhThucThanhToanRepository;
import com.example.wedbansach_bakend1.entity.HinhThucGiaoHang;
import com.example.wedbansach_bakend1.entity.HinhThucThanhToan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lookup") // Đường dẫn chung cho các dữ liệu tra cứu

public class LookupController {

    @Autowired
    private HinhThucGiaoHangRepository hinhThucGiaoHangRepository;

    @Autowired
    private HinhThucThanhToanRepository hinhThucThanhToanRepository;

    @GetMapping("/shipping-methods")
    public ResponseEntity<List<HinhThucGiaoHang>> getAllShippingMethods() {
        List<HinhThucGiaoHang> methods = hinhThucGiaoHangRepository.findAll();
        return ResponseEntity.ok(methods);
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<List<HinhThucThanhToan>> getAllPaymentMethods() {
        List<HinhThucThanhToan> methods = hinhThucThanhToanRepository.findAll();
        return ResponseEntity.ok(methods);
    }
}