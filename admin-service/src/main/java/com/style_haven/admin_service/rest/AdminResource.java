package com.style_haven.admin_service.rest;

import com.style_haven.admin_service.model.AdminDTO;
import com.style_haven.admin_service.service.AdminService;
import com.style_haven.admin_service.util.ReferencedException;
import com.style_haven.admin_service.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/admin/admins", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminResource {

    private final AdminService adminService;

    public AdminResource(final AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.findAll());
    }

    @GetMapping("/{adminid}")
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable(name = "adminid") final Long adminid) {
        return ResponseEntity.ok(adminService.get(adminid));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAdmin(@RequestBody @Valid final AdminDTO adminDTO) {
        final Long createdAdminid = adminService.create(adminDTO);
        return new ResponseEntity<>(createdAdminid, HttpStatus.CREATED);
    }

    @PutMapping("/{adminid}")
    public ResponseEntity<Long> updateAdmin(@PathVariable(name = "adminid") final Long adminid,
            @RequestBody @Valid final AdminDTO adminDTO) {
        adminService.update(adminid, adminDTO);
        return ResponseEntity.ok(adminid);
    }

    @DeleteMapping("/{adminid}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAdmin(@PathVariable(name = "adminid") final Long adminid) {
        final ReferencedWarning referencedWarning = adminService.getReferencedWarning(adminid);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        adminService.delete(adminid);
        return ResponseEntity.noContent().build();
    }

}
