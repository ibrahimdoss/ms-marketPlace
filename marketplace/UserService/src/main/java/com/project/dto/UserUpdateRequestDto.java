package com.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {

    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean isActive;
    private boolean isPremium;
}
