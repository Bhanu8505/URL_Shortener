package com.URL.URLShortner.Controllers;

import com.URL.URLShortner.DTOs.ClickEventDTO;
import com.URL.URLShortner.DTOs.URLMappingDTO;
import com.URL.URLShortner.Models.User;
import com.URL.URLShortner.Services.URLMappingService;
import com.URL.URLShortner.Services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class URLMappingController {
    private URLMappingService urlMappingService;
    private UserService userService;

    // {"originalUrl":"https://example.com"}
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<URLMappingDTO> createShortUrl(@RequestBody Map<String,String> request, Principal principal){
        String originalUrl = request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());
        // Call service
        URLMappingDTO urlMappingDTO =  urlMappingService.createUrl(originalUrl,user);
        return ResponseEntity.ok(urlMappingDTO);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<URLMappingDTO>> getUserUrls(Principal principal){

        User user = userService.findByUsername(principal.getName());

        List<URLMappingDTO> urls = urlMappingService.getUrlsByUser(user);

        return ResponseEntity.ok(urls);
    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime start = LocalDateTime.parse(startDate,formatter);
        LocalDateTime end = LocalDateTime.parse(endDate,formatter);

        List<ClickEventDTO> clickEventDTOS = urlMappingService.getClickEventByDate(shortUrl,start,end);

        return ResponseEntity.ok(clickEventDTOS);
    }

    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(Principal principal, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        User user = userService.findByUsername(principal.getName());

        LocalDate start = LocalDate.parse(startDate,formatter);
        LocalDate end = LocalDate.parse(endDate,formatter);

        Map<LocalDate, Long> totalClicks = urlMappingService.getTotalClicksByUserAndDate(user,start,end);

        return ResponseEntity.ok(totalClicks);
    }

}
