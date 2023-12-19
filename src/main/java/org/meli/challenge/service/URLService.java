package org.meli.challenge.service;

import org.springframework.stereotype.Service;

import static java.lang.Integer.parseInt;

@Service
public class URLService {

    private final RedisService redisService;

    public URLService(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * Crea una url corta a partir de una larga. Nada evita que se genere más de una url corta para una misma url larga.
     */
    public String getShortURL(String longURL) {
        String shortURL = generateShortURL();
        redisService.save(getShortURLKey(shortURL), longURL);
        return shortURL;
    }

    /**
     * Obtiene una url larga a partir de una corta generada previamente
     */
    public String getLongURL(String shortURL) {
        return redisService.get(getShortURLKey(shortURL));
    }

    /**
     * Borra una url corta en redis
     */
    public void deleteShortURL(String shortURL) {
        redisService.delete(getShortURLKey(shortURL));
    }

    /**
     * Genera una url corta a partir de un id numérico autoincremental
     */
    private String generateShortURL() {
        int currentId = getNextId();
        return idToShortURL(currentId);
    }

    /**
     * Construye la key a usar para persistir en redis el par de url corta y larga
     */
    private String getShortURLKey(String shortURL) {
        return "URL:" + shortURL;
    }

    /**
     * Obtiene el id que corresponde a una nueva url corta
     * NOTA: se asume que no serían muchos los usuarios que administren las urls cortas, sino puede haber colisiones
     * y se debe optar por otra estrategia para manejar lecturas/escrituras simultáneas
     */
    private int getNextId() {
        String currentIdAsString = redisService.get("current-id");
        if(currentIdAsString == null) {
            currentIdAsString = "0";
        }
        int nextId = parseInt(currentIdAsString) + 1;
        redisService.save("current-id", Integer.toString(nextId));
        return nextId;
    }

    /**
     * Transforma un id entero a un valor hexadecimal en base 52 (letras minúsculas y mayúsculas)
     */
    private String idToShortURL(int n)
    {
        char[] map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        StringBuilder shortURL = new StringBuilder();
        while (n > 0) {
            shortURL.append(map[n % 52]);
            n = n / 52;
        }
        return shortURL.reverse().toString();
    }
}
