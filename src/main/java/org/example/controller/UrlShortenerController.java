package org.example.controller;

import org.example.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/url")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService service;

    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody Map<String, String> body) {
        String longUrl = body.get("longUrl");
        String key = service.createShortUrl(longUrl);
        return ResponseEntity.ok("http://localhost:8080/" + key);
    }


    @GetMapping("/{key}")
    public ResponseEntity<Void> redirect(@PathVariable("key") String key) {
        String longUrl = service.resolveUrl(key);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }
}

