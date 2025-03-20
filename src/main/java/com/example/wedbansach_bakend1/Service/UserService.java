package com.example.wedbansach_bakend1.Service;
import com.example.wedbansach_bakend1.entity.NguoiDung;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

public interface UserService extends UserDetailsService {
    public NguoiDung findByUsername(String username);
}
