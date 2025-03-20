package com.example.wedbansach_bakend1.dao;

import com.example.wedbansach_bakend1.entity.ChiTietDonHang;
import com.example.wedbansach_bakend1.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path="don-hang")
public interface DonHangRepository extends JpaRepository<DonHang, Long> {

}
