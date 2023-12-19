package org.meli.challenge.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.meli.challenge.exception.ManagedException;
import org.meli.challenge.service.URLService;
import org.meli.challenge.exception.UnknownURLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.web.bind.annotation.*;

@RestController
public class URLController {

    private final URLService urlService;

    @Autowired
    public URLController(URLService urlService) {
        this.urlService = urlService;
    }

    /**
     * Redirije a una url larga a partir del código de url corta
     */
    @GetMapping("/{shortURL}")
    public void getLongURL(HttpServletResponse response, @PathVariable String shortURL) throws UnknownURLException {
        String longURL = urlService.getLongURL(shortURL);
        if (longURL != null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", urlService.getLongURL(shortURL));
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            throw new UnknownURLException();
        }
    }

    /**
     * Crea una url corta a partir de una larga
     * NOTA: Debería tener algún tipo de autenticación, ya que sólo el getter debería ser de uso público
     */
    @PostMapping("/api/create-short-url")
    public String createShortURL(@RequestParam String longURL) {
        // TODO Verificar retorno. Quiero que sea una url completa?
        return urlService.getShortURL(longURL);
    }

    /**
     * Elimina una url corta
     * NOTA: Debería tener algún tipo de autenticación, ya que sólo el getter debería ser de uso público
     */
    @DeleteMapping("/api/delete-short-url")
    public void deleteShortURL(@RequestParam String shortURL) {
        urlService.deleteShortURL(shortURL);
    }

    @ExceptionHandler(Exception.class)
    String handleExceptions(HttpServletRequest request, Exception ex) {
        request.setAttribute(ErrorAttributes.ERROR_ATTRIBUTE, ex);
        String message;
        try {
            throw ex;
        } catch (ManagedException e) {
            message = e.getMessage();
        } catch (Exception e) {
            message = "Se ha producido un error inesperado.";
        }
        return message;
    }
}
