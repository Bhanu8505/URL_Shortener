package com.URL.URLShortner.Repository;

import com.URL.URLShortner.Models.UrlMapping;
import com.URL.URLShortner.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface URLMappingRepository extends JpaRepository<UrlMapping,Long> {
    UrlMapping findByShortURL(String shortURL);
    List<UrlMapping> findByUser(User user);
}
