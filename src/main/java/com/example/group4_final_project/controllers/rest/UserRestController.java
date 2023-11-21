package com.example.group4_final_project.controllers.rest;

import com.example.group4_final_project.exceptions.AuthorizationException;
import com.example.group4_final_project.exceptions.EntityDuplicateException;
import com.example.group4_final_project.exceptions.EntityNotFoundException;
import com.example.group4_final_project.helpers.AuthenticationHelper;
import com.example.group4_final_project.helpers.UserMapper;
import com.example.group4_final_project.models.*;
import com.example.group4_final_project.services.UserService;
import jakarta.validation.Valid;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;

    private final AuthenticationHelper authenticationHelper;


    public UserRestController(UserService userService, UserMapper userMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }




    @GetMapping
    public List<User> get(@RequestHeader HttpHeaders headers,
                          @RequestParam(required = false) String email,
                          @RequestParam(required = false) String firstName

    ) {
        authenticationHelper.tryGetUser(headers);
        FilterOptionsUser filterOptionsUser = new FilterOptionsUser( email,firstName);
        return userService.get(filterOptionsUser);

    }

    @GetMapping("/{id}")
    public User get(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.get(id , user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }




    @PostMapping()
    public ResponseUserDto register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        try {

            return userService.register(userRegisterDto);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseUserDto update(@RequestHeader HttpHeaders headers,
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
    public ResponseUserDto delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
           return userService.delete(id,user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException | AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
