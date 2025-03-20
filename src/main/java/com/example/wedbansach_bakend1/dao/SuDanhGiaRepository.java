package com.example.wedbansach_bakend1.dao;

import com.example.wedbansach_bakend1.entity.NguoiDung;
import com.example.wedbansach_bakend1.entity.suDanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path="su-danh-gia")
public interface SuDanhGiaRepository extends JpaRepository<suDanhGia, Integer> {

}
