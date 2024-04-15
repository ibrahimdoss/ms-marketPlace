package com.project.dto;

import lombok.Builder;

@Builder
public record ProductCountUpdateRequestDto(Long id, int numberOfProduct) {
}
