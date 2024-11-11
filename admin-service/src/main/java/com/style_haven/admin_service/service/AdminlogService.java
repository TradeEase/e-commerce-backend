package com.style_haven.admin_service.service;

import com.style_haven.admin_service.domain.Admin;
import com.style_haven.admin_service.domain.Adminlog;
import com.style_haven.admin_service.model.AdminlogDTO;
import com.style_haven.admin_service.repos.AdminRepository;
import com.style_haven.admin_service.repos.AdminlogRepository;
import com.style_haven.admin_service.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AdminlogService {

    private final AdminlogRepository adminlogRepository;
    private final AdminRepository adminRepository;

    public AdminlogService(final AdminlogRepository adminlogRepository,
            final AdminRepository adminRepository) {
        this.adminlogRepository = adminlogRepository;
        this.adminRepository = adminRepository;
    }

    public List<AdminlogDTO> findAll() {
        final List<Adminlog> adminlogs = adminlogRepository.findAll(Sort.by("adminlogId"));
        return adminlogs.stream()
                .map(adminlog -> mapToDTO(adminlog, new AdminlogDTO()))
                .toList();
    }

    public AdminlogDTO get(final Integer adminlogId) {
        return adminlogRepository.findById(adminlogId)
                .map(adminlog -> mapToDTO(adminlog, new AdminlogDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AdminlogDTO adminlogDTO) {
        final Adminlog adminlog = new Adminlog();
        mapToEntity(adminlogDTO, adminlog);
        return adminlogRepository.save(adminlog).getAdminlogId();
    }

    public void update(final Integer adminlogId, final AdminlogDTO adminlogDTO) {
        final Adminlog adminlog = adminlogRepository.findById(adminlogId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(adminlogDTO, adminlog);
        adminlogRepository.save(adminlog);
    }

    public void delete(final Integer adminlogId) {
        adminlogRepository.deleteById(adminlogId);
    }

    private AdminlogDTO mapToDTO(final Adminlog adminlog, final AdminlogDTO adminlogDTO) {
        adminlogDTO.setAdminlogId(adminlog.getAdminlogId());
        adminlogDTO.setTimestamp(adminlog.getTimestamp());
        adminlogDTO.setAction(adminlog.getAction());
        adminlogDTO.setAdmin(adminlog.getAdmin() == null ? null : adminlog.getAdmin().getAdminid());
        return adminlogDTO;
    }

    private Adminlog mapToEntity(final AdminlogDTO adminlogDTO, final Adminlog adminlog) {
        adminlog.setTimestamp(adminlogDTO.getTimestamp());
        adminlog.setAction(adminlogDTO.getAction());
        final Admin admin = adminlogDTO.getAdmin() == null ? null : adminRepository.findById(adminlogDTO.getAdmin())
                .orElseThrow(() -> new NotFoundException("admin not found"));
        adminlog.setAdmin(admin);
        return adminlog;
    }

    public boolean adminExists(final Long adminid) {
        return adminlogRepository.existsByAdminAdminid(adminid);
    }

}
