package com.example.webbansach_backend.rest;

import com.example.webbansach_backend.dao.ChiTietDonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    private ChiTietDonHangRepository repository;
    @Autowired
    public testController(ChiTietDonHangRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/")
    public void test(){
        repository.findAll().forEach(System.out::println);

    }

}
