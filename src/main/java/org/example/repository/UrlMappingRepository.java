package org.example.repository;


import org.example.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, String> {
    UrlMapping findByLongUrl(String longUrl);
}

