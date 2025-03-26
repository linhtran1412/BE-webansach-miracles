//package com.example.wedbansach_bakend1.controller;
//
//import com.example.wedbansach_bakend1.Service.SachService;
//import com.example.wedbansach_bakend1.dao.SachRepository;
//import com.example.wedbansach_bakend1.entity.Sach;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/sach")
//public class SachController {
//private SachService sachService;
//@Autowired
//public SachController(SachService sachService) {
//    this.sachService = sachService;
//}
//
//@GetMapping("/{maSach}")
//public ResponseEntity<Sach> getSachById(@PathVariable int id){
//    Sach sach=sachService.getSachById(id);
//if(sach!=null){
//return ResponseEntity.ok(sach);
//}
//else{
//return ResponseEntity.notFound().build();
//}
//}
//@PostMapping
//public ResponseEntity<Sach> addSach(@RequestBody Sach sach){
//    sach.setMaSach(0);
//    sach=sachService.addSach(sach);
//return ResponseEntity.status(HttpStatus.CREATED).body(sach);
//}
//@PutMapping ("/{maSach}")
//public ResponseEntity<Sach> updateSach(@PathVariable int id, @RequestBody Sach sach) {
//    Sach sachTonTai = sachService.getSachById(id);
//    if (sachTonTai != null) {
//        sachTonTai.setISBN(sach.getISBN());
//        sachTonTai.setGiaBan(sach.getGiaBan());
//        sachTonTai.setGiaNiemYet(sach.getGiaNiemYet());
//        sachTonTai.setMoTa(sach.getMoTa());
//        sachTonTai.setSoLuong(sach.getSoLuong());
//        sachTonTai.setTenSach(sach.getTenSach());
//        sachTonTai.setTenTacGia(sach.getTenTacGia());
//        sachTonTai.setTrungBinhXepHang(sach.getTrungBinhXepHang());
//
//    } else {
//        return ResponseEntity.notFound().build();
//    }
//    return null;
//}
//@DeleteMapping("/{maSach}")
//    public ResponseEntity<Void> deleteSachById(@PathVariable int id){
//Sach sachTonTai = sachService.getSachById(id);
//if (sachTonTai != null) {
//sachService.deleteSachById(id);
//
//}else{
//return ResponseEntity.notFound().build();
//}
//    return null;
//}
//
//}
