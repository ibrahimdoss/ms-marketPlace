package com.project.dto;

public record UserInfoResponseDto(Long id, String name, String lastName, String email, String phoneNumber, boolean premium) {
}
