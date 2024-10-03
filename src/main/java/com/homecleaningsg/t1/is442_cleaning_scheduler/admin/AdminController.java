package com.homecleaningsg.t1.is442_cleaning_scheduler.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "api/v0.1/admins")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }


    @GetMapping("/{id}")
    public Admin getAdminById(@PathVariable("id") long id) {
        return adminService.getAdminById(id);
    }
}
