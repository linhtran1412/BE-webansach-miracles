package com.example.wedbansach_bakend1.Service;

import com.example.wedbansach_bakend1.dto.CheckoutRequestDTO;
// SỬA IMPORT VÀ KIỂU TRẢ VỀ
import com.example.wedbansach_bakend1.dto.DonHangDTO;

public interface CheckoutService {
    DonHangDTO placeOrder(CheckoutRequestDTO checkoutRequestDto, String username); // Sửa thành DonHangDTO
}