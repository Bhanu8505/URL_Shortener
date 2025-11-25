package com.URL.URLShortner.Services;

import com.URL.URLShortner.DTOs.ClickEventDTO;
import com.URL.URLShortner.DTOs.URLMappingDTO;
import com.URL.URLShortner.Models.ClickEvent;
import com.URL.URLShortner.Models.UrlMapping;
import com.URL.URLShortner.Models.User;
import com.URL.URLShortner.Repository.ClickEventRepository;
import com.URL.URLShortner.Repository.URLMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class URLMappingService {

    private URLMappingRepository urlMappingRepository;

    private ClickEventRepository clickEventRepository;

    public URLMappingDTO createUrl(String originalUrl, User user) {

        String shortUrl = generateShortUrl();

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalURL(originalUrl);
        urlMapping.setUser(user);
        urlMapping.setShortURL(shortUrl);
        urlMapping.setCreatedDate(LocalDateTime.now());

        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);

        return convertToDTO(savedUrlMapping);
    }

    private URLMappingDTO convertToDTO(UrlMapping urlMapping){

        URLMappingDTO urlMappingDTO = new URLMappingDTO();

        urlMappingDTO.setId(urlMapping.getId());
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalURL());
        urlMappingDTO.setShortUrl(urlMapping.getShortURL());
        urlMappingDTO.setClickCount(urlMapping.getClickCount());
        urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDTO.setUsername(urlMapping.getUser().getUsername());

        return urlMappingDTO;
    }

    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random random = new Random();
        StringBuilder shortURl = new StringBuilder(8);

        for(int i=0;i<8;i++){
            shortURl.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortURl.toString();
    }

    public List<URLMappingDTO> getUrlsByUser(User user) {
        return urlMappingRepository.findByUser(user).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ClickEventDTO> getClickEventByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        UrlMapping urlMapping = urlMappingRepository.findByShortURL(shortUrl);

        if(urlMapping != null){
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping,start,end).stream()
                    .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()))
                    .entrySet().stream()
                    .map(entry -> {
                        ClickEventDTO clickEventDTO = new ClickEventDTO();
                        clickEventDTO.setClickDate(entry.getKey());
                        clickEventDTO.setCount(entry.getValue());
                        return clickEventDTO;
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);

        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());

        return clickEvents.stream()
                .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(),Collectors.counting()));
    }

    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortURL(shortUrl);

        if(urlMapping != null){
            urlMapping.setClickCount(urlMapping.getClickCount()+1);
            urlMappingRepository.save(urlMapping);

            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(urlMapping);

            clickEventRepository.save(clickEvent);
        }

        return urlMapping;
    }
}
