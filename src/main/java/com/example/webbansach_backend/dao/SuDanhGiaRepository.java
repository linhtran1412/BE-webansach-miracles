package com.example.webbansach_backend.dao;

import com.example.webbansach_backend.entity.suDanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="su-danh-gia")
public interface SuDanhGiaRepository extends JpaRepository<suDanhGia, Integer> {

}
