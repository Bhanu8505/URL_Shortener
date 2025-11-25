package com.URL.URLShortner.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class URLMappingDTO {
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private LocalDateTime createdDate;
    private String username;
}
