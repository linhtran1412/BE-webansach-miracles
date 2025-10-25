package com.example.webbansach_backend.dao;

import com.example.webbansach_backend.entity.Quyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="quyen")
public interface QuyenRepository extends JpaRepository<Quyen, Integer> {
public Quyen findByTenQuyen(String tenQuyen);
}
