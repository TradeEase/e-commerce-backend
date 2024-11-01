package com.style_haven.user_service.service;

import com.style_haven.user_service.domain.Address;
import com.style_haven.user_service.domain.User;
import com.style_haven.user_service.model.AddressDTO;
import com.style_haven.user_service.repos.AddressRepository;
import com.style_haven.user_service.repos.UserRepository;
import com.style_haven.user_service.util.NotFoundException;
import com.style_haven.user_service.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(final AddressRepository addressRepository,
            final UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<AddressDTO> findAll() {
        final List<Address> addresses = addressRepository.findAll(Sort.by("addressId"));
        return addresses.stream()
                .map(address -> mapToDTO(address, new AddressDTO()))
                .toList();
    }

    public AddressDTO get(final Integer addressId) {
        return addressRepository.findById(addressId)
                .map(address -> mapToDTO(address, new AddressDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AddressDTO addressDTO) {
        final Address address = new Address();
        mapToEntity(addressDTO, address);
        return addressRepository.save(address).getAddressId();
    }

    public void update(final Integer addressId, final AddressDTO addressDTO) {
        final Address address = addressRepository.findById(addressId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(addressDTO, address);
        addressRepository.save(address);
    }

    public void delete(final Integer addressId) {
        addressRepository.deleteById(addressId);
    }

    private AddressDTO mapToDTO(final Address address, final AddressDTO addressDTO) {
        addressDTO.setAddressId(address.getAddressId());
        addressDTO.setUserId(address.getUserId());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setCity(address.getCity());
        addressDTO.setState(address.getState());
        addressDTO.setPostalCode(address.getPostalCode());
        return addressDTO;
    }

    private Address mapToEntity(final AddressDTO addressDTO, final Address address) {
        address.setUserId(addressDTO.getUserId());
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPostalCode(addressDTO.getPostalCode());
        return address;
    }

    public ReferencedWarning getReferencedWarning(final Integer addressId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Address address = addressRepository.findById(addressId)
                .orElseThrow(NotFoundException::new);
        final User addressUser = userRepository.findFirstByAddress(address);
        if (addressUser != null) {
            referencedWarning.setKey("address.user.address.referenced");
            referencedWarning.addParam(addressUser.getUserId());
            return referencedWarning;
        }
        return null;
    }

}
