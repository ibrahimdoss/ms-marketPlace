package com.project.controller;

import com.project.dto.UserInfoResponseDto;
import com.project.dto.UserSaveRequestDto;
import com.project.dto.UserUpdateRequestDto;
import com.project.dto.UserUpdateResponseDto;
import com.project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public void save(@RequestBody UserSaveRequestDto userSaveRequestDto){
        userService.save(userSaveRequestDto);
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponseDto> info(@RequestParam Long userId){
        return ResponseEntity.ok().body(userService.info(userId));
    }

    @GetMapping("/infoByNumber")
    public ResponseEntity<UserInfoResponseDto> infoByPhoneNumber(@RequestParam String phoneNumber){
        return ResponseEntity.ok().body(userService.infoByPhoneNumber(phoneNumber));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDto> update(@PathVariable Long id, @RequestBody UserUpdateRequestDto userUpdateDto){
        UserUpdateResponseDto userUpdateResponseDto = userService.updateUser(id, userUpdateDto);
        if (userUpdateResponseDto != null) {
            return ResponseEntity.ok(userUpdateResponseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
