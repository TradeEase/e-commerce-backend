package com.style_haven.admin_service.repos;

import com.style_haven.admin_service.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long> {
}
