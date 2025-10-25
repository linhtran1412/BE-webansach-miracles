package com.example.webbansach_backend.Service;

import com.example.webbansach_backend.dao.SachRepository;
import com.example.webbansach_backend.entity.Sach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SachServiceImpl implements SachService {
    private SachRepository sachRepository;
    @Autowired
    public void setSachRepository(SachRepository sachRepository) {
        this.sachRepository = sachRepository;
    }


    @Override
    public Sach addSach(Sach sach) {
return sachRepository.save(sach);
    }

    @Override
    public Sach updateStudent(Sach student) {
        return sachRepository.saveAndFlush(student);
    }

    @Override
    public void deleteSachById(int id) {
sachRepository.deleteById(id);
    }
}
