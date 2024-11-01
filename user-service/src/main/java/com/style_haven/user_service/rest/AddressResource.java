package com.style_haven.user_service.rest;

import com.style_haven.user_service.model.AddressDTO;
import com.style_haven.user_service.service.AddressService;
import com.style_haven.user_service.util.ReferencedException;
import com.style_haven.user_service.util.ReferencedWarning;
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
@RequestMapping(value = "/api/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressResource {

    private final AddressService addressService;

    public AddressResource(final AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        return ResponseEntity.ok(addressService.findAll());
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> getAddress(
            @PathVariable(name = "addressId") final Integer addressId) {
        return ResponseEntity.ok(addressService.get(addressId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAddress(@RequestBody @Valid final AddressDTO addressDTO) {
        final Integer createdAddressId = addressService.create(addressDTO);
        return new ResponseEntity<>(createdAddressId, HttpStatus.CREATED);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Integer> updateAddress(
            @PathVariable(name = "addressId") final Integer addressId,
            @RequestBody @Valid final AddressDTO addressDTO) {
        addressService.update(addressId, addressDTO);
        return ResponseEntity.ok(addressId);
    }

    @DeleteMapping("/{addressId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable(name = "addressId") final Integer addressId) {
        final ReferencedWarning referencedWarning = addressService.getReferencedWarning(addressId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        addressService.delete(addressId);
        return ResponseEntity.noContent().build();
    }

}
