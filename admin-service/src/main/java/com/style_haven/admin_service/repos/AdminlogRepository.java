package com.style_haven.admin_service.repos;

import com.style_haven.admin_service.domain.Admin;
import com.style_haven.admin_service.domain.Adminlog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminlogRepository extends JpaRepository<Adminlog, Integer> {

    Adminlog findFirstByAdmin(Admin admin);

    boolean existsByAdminAdminid(Long adminid);

}
