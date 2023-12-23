package com.github.mullerdaniil.chatusbackend.dto;


import java.time.LocalDateTime;

public record MessageReadDTO(String text,
                             String author,
                             String timestamp) {
}
