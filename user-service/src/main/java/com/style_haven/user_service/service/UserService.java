package com.style_haven.user_service.service;

import com.style_haven.user_service.domain.User;
import com.style_haven.user_service.model.UserDTO;
import com.style_haven.user_service.repos.UserRepository;
import com.style_haven.user_service.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userid"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Integer userid) {
        return userRepository.findById(userid)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getUserid();
    }

    public void update(final Integer userid, final UserDTO userDTO) {
        final User user = userRepository.findById(userid)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Integer userid) {
        userRepository.deleteById(userid);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUserid(user.getUserid());
        userDTO.setFname(user.getFname());
        userDTO.setLname(user.getLname());
        userDTO.setState(user.getState());
        userDTO.setCity(user.getCity());
        userDTO.setStreet(user.getStreet());
        userDTO.setPostalCode(user.getPostalCode());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setFname(userDTO.getFname());
        user.setLname(userDTO.getLname());
        user.setState(userDTO.getState());
        user.setCity(userDTO.getCity());
        user.setStreet(userDTO.getStreet());
        user.setPostalCode(userDTO.getPostalCode());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        return user;
    }

}
