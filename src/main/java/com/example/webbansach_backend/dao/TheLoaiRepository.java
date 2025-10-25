package com.example.webbansach_backend.dao;

import com.example.webbansach_backend.entity.TheLoai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="the-loai")
public interface TheLoaiRepository extends JpaRepository<TheLoai, Integer> {


}
