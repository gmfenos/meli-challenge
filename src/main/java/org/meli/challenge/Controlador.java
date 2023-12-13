package org.meli.challenge;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controlador {

    private URLService urlService;

    @Autowired
    public Controlador(URLService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortURL}")
    public void getLongURL(HttpServletResponse response, @PathVariable String shortURL) {
        // TODO Si no existe la URL qué hago? 404 + mensaje descriptivo
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", urlService.getLongURL(shortURL));
    }

    @PostMapping("/api/createShortURL")
    public String createShortURL(@RequestParam String longURL) {
        return urlService.getShortURL(longURL);
    }

    @DeleteMapping("/api/deleteShortURL")
    public void deleteShortURL(@RequestParam String shortURL) {
        urlService.deleteShortURL(shortURL);
    }
}
