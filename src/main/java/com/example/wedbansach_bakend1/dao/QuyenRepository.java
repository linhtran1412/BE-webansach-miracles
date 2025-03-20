package com.example.wedbansach_bakend1.dao;

import com.example.wedbansach_bakend1.entity.NguoiDung;
import com.example.wedbansach_bakend1.entity.Quyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path="quyen")
public interface QuyenRepository extends JpaRepository<Quyen, Integer> {
public Quyen findByTenQuyen(String tenQuyen);
}
