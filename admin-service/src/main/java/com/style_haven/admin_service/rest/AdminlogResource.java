package com.style_haven.admin_service.rest;

import com.style_haven.admin_service.model.AdminlogDTO;
import com.style_haven.admin_service.service.AdminlogService;
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
@RequestMapping(value = "/api/admin/adminlogs", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminlogResource {

    private final AdminlogService adminlogService;

    public AdminlogResource(final AdminlogService adminlogService) {
        this.adminlogService = adminlogService;
    }

    @GetMapping
    public ResponseEntity<List<AdminlogDTO>> getAllAdminlogs() {
        return ResponseEntity.ok(adminlogService.findAll());
    }

    @GetMapping("/{adminlogId}")
    public ResponseEntity<AdminlogDTO> getAdminlog(
            @PathVariable(name = "adminlogId") final Integer adminlogId) {
        return ResponseEntity.ok(adminlogService.get(adminlogId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAdminlog(
            @RequestBody @Valid final AdminlogDTO adminlogDTO) {
        final Integer createdAdminlogId = adminlogService.create(adminlogDTO);
        return new ResponseEntity<>(createdAdminlogId, HttpStatus.CREATED);
    }

    @PutMapping("/{adminlogId}")
    public ResponseEntity<Integer> updateAdminlog(
            @PathVariable(name = "adminlogId") final Integer adminlogId,
            @RequestBody @Valid final AdminlogDTO adminlogDTO) {
        adminlogService.update(adminlogId, adminlogDTO);
        return ResponseEntity.ok(adminlogId);
    }

    @DeleteMapping("/{adminlogId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAdminlog(
            @PathVariable(name = "adminlogId") final Integer adminlogId) {
        adminlogService.delete(adminlogId);
        return ResponseEntity.noContent().build();
    }

}
