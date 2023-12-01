package com.example.group4_final_project.controllers.rest;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.helpers.ImageHelper;
import com.example.group4_final_project.models.DTOs.ResponseUser;
import com.example.group4_final_project.models.DTOs.UserRegisterDto;
import com.example.group4_final_project.models.DTOs.UserUpdateDto;
import com.example.group4_final_project.models.filtering.FilterOptionsUser;
import com.example.group4_final_project.models.models.User;
import com.example.group4_final_project.services.contracts.UserService;
import jakarta.validation.Valid;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;

    private final AuthenticationHelper authenticationHelper;

    private final ImageHelper imageHelper;


    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, ImageHelper imageHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.imageHelper = imageHelper;
    }


    @GetMapping
    public Page<ResponseUser> get(@RequestHeader HttpHeaders headers,
                                  @RequestParam(required = false) String email,
                                  @RequestParam(required = false) String firstName,
                                  @RequestParam(required = false) String lastName,
                                  // @SortDefault(sort = "update_at", direction = Sort.Direction.DESC)
                                  Pageable pageable) {
        authenticationHelper.tryGetUser(headers);
        FilterOptionsUser filterOptionsUser = new FilterOptionsUser(email, firstName, lastName);

        return userService.get(filterOptionsUser, pageable);

    }

    @GetMapping("/{id}")
    public ResponseUser get(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            ResponseUser responseUser = userService.get(id, user);
            return userService.get(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }


    @PostMapping()
    public ResponseUser register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        try {

            return userService.register(userRegisterDto);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseUser update(@RequestHeader HttpHeaders headers,
                               @PathVariable int id,

                               @RequestBody @Valid UserUpdateDto userUpdateDto) {

        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.update(user, id, userUpdateDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseUser delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);

            return userService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/picture")
    public ResponseUser picture(@RequestHeader HttpHeaders headers,
                                @RequestBody MultipartFile multipartFile) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            String url = imageHelper.uploadImage(multipartFile);
            return userService.addPicture(user, url);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @PostMapping("/approved/{id}")
    public void approvedTeacher(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);

            userService.approvedTeacher(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }


    }

}


