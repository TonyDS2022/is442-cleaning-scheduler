package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username).orElse(null);
    }

    public Admin addAdmin(Admin admin){
        admin.setJoinDate(LocalDate.now());
        return adminRepository.save(admin);
    }

    public Admin updateAdmin(Long adminId, Admin updatedAdmin){
        if(!adminRepository.existsById(adminId)){
            throw new IllegalArgumentException("Admin not found");
        }
        updatedAdmin.setAdminId(adminId);
        return adminRepository.save(updatedAdmin);
    }

    public Admin deactivateAdmin(Long adminId){
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        admin.setActive(false);
        admin.setDeactivatedAt(LocalDate.now());
        return adminRepository.save(admin);
    }

}
