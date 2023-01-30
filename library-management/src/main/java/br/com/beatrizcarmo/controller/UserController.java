package br.com.beatrizcarmo.controller;

import br.com.beatrizcarmo.dto.UserDto;
import br.com.beatrizcarmo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.insertUser(userDto);
    }
    
    @GetMapping
    public List<UserDto> getAllUsers() {
    	return userService.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public UserDto getUserById(@PathVariable(value = "id") UUID id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable(value = "id") UUID id, @RequestBody UserDto newUserDto) {
        return userService.updateUser(newUserDto, id);
    }

    @DeleteMapping("/{id}")
    public void deletUser(@PathVariable(value = "id") UUID id) {
    	userService.deletUser(id);
    }
}
