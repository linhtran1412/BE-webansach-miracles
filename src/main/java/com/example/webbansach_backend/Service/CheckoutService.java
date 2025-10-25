package com.example.webbansach_backend.Service;

import com.example.webbansach_backend.dto.CheckoutRequestDTO;
// SỬA IMPORT VÀ KIỂU TRẢ VỀ
import com.example.webbansach_backend.dto.DonHangDTO;

public interface CheckoutService {
    DonHangDTO placeOrder(CheckoutRequestDTO checkoutRequestDto, String username); // Sửa thành DonHangDTO
}