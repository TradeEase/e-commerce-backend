package com.style_haven.admin_service.service;

import com.style_haven.admin_service.domain.Admin;
import com.style_haven.admin_service.domain.Adminlog;
import com.style_haven.admin_service.model.AdminDTO;
import com.style_haven.admin_service.repos.AdminRepository;
import com.style_haven.admin_service.repos.AdminlogRepository;
import com.style_haven.admin_service.util.NotFoundException;
import com.style_haven.admin_service.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminlogRepository adminlogRepository;

    public AdminService(final AdminRepository adminRepository,
            final AdminlogRepository adminlogRepository) {
        this.adminRepository = adminRepository;
        this.adminlogRepository = adminlogRepository;
    }

    public List<AdminDTO> findAll() {
        final List<Admin> admins = adminRepository.findAll(Sort.by("adminid"));
        return admins.stream()
                .map(admin -> mapToDTO(admin, new AdminDTO()))
                .toList();
    }

    public AdminDTO get(final Long adminid) {
        return adminRepository.findById(adminid)
                .map(admin -> mapToDTO(admin, new AdminDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AdminDTO adminDTO) {
        final Admin admin = new Admin();
        mapToEntity(adminDTO, admin);
        return adminRepository.save(admin).getAdminid();
    }

    public void update(final Long adminid, final AdminDTO adminDTO) {
        final Admin admin = adminRepository.findById(adminid)
                .orElseThrow(NotFoundException::new);
        mapToEntity(adminDTO, admin);
        adminRepository.save(admin);
    }

    public void delete(final Long adminid) {
        adminRepository.deleteById(adminid);
    }

    private AdminDTO mapToDTO(final Admin admin, final AdminDTO adminDTO) {
        adminDTO.setAdminid(admin.getAdminid());
        adminDTO.setUsername(admin.getUsername());
        adminDTO.setPassword(admin.getPassword());
        adminDTO.setEmail(admin.getEmail());
        return adminDTO;
    }

    private Admin mapToEntity(final AdminDTO adminDTO, final Admin admin) {
        admin.setUsername(adminDTO.getUsername());
        admin.setPassword(adminDTO.getPassword());
        admin.setEmail(adminDTO.getEmail());
        return admin;
    }

    public ReferencedWarning getReferencedWarning(final Long adminid) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Admin admin = adminRepository.findById(adminid)
                .orElseThrow(NotFoundException::new);
        final Adminlog adminAdminlog = adminlogRepository.findFirstByAdmin(admin);
        if (adminAdminlog != null) {
            referencedWarning.setKey("admin.adminlog.admin.referenced");
            referencedWarning.addParam(adminAdminlog.getAdminlogId());
            return referencedWarning;
        }
        return null;
    }

}
