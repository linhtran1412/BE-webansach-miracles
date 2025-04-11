package com.example.wedbansach_bakend1.Security;


public class Endpoints {
    public static final String front_end_host = "http://localhost:3000";
    public static final String[] PUBLIC_GET_ENDPOINS = {
            "/students",
            "/students/**",
            "/sach",
            "/sach/**",
            "/hinh-anh",
            "/hinh-anh/**",
            "/nguoi-dung/search/existsByTenDangNhap",
            "/nguoi-dung/search/existsByEmail",
            "/tai-khoan/kich-hoat",
            "/don-hang",
    };

    public static final String[] PUBLIC_POST_ENDPOINS = {
            "/students",
            "/tai-khoan/dang-ky",
            "/tai-khoan/dang-nhap",
            "/don-hang",
    };

    public static final String[] ADMIN_GET_ENDPOINS = {
            "/nguoi-dung",
            "/nguoi-dung/**",
    };
    public static final String[] ADMIN_POST_ENDPOINS = {
            "/sach",
    };
}