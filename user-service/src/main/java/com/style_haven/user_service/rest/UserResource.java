package com.style_haven.user_service.rest;

import com.style_haven.user_service.model.UserDTO;
import com.style_haven.user_service.service.UserService;
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
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserResource {

    private final UserService userService;

    public UserResource(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{userid}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "userid") final Integer userid) {
        return ResponseEntity.ok(userService.get(userid));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createUser(@RequestBody @Valid final UserDTO userDTO) {
        final Integer createdUserid = userService.create(userDTO);
        return new ResponseEntity<>(createdUserid, HttpStatus.CREATED);
    }

    @PutMapping("/{userid}")
    public ResponseEntity<Integer> updateUser(@PathVariable(name = "userid") final Integer userid,
            @RequestBody @Valid final UserDTO userDTO) {
        userService.update(userid, userDTO);
        return ResponseEntity.ok(userid);
    }

    @DeleteMapping("/{userid}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userid") final Integer userid) {
        userService.delete(userid);
        return ResponseEntity.noContent().build();
    }

}
