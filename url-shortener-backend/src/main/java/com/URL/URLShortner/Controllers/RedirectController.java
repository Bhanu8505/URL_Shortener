package com.URL.URLShortner.Controllers;

import com.URL.URLShortner.Models.UrlMapping;
import com.URL.URLShortner.Services.URLMappingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RedirectController {

    private URLMappingService urlMappingService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl){
        UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);

        if(urlMapping != null){

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Location",urlMapping.getOriginalURL());

            return ResponseEntity.status(302).headers(httpHeaders).build();
        } else{
            return ResponseEntity.notFound().build();
        }

    }
}
