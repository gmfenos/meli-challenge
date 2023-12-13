package org.meli.challenge;

import org.springframework.stereotype.Service;

import static java.lang.Integer.parseInt;

@Service
public class URLService {

    private RedisService redisService;

    public URLService(RedisService redisService) {
        this.redisService = redisService;
    }

    public String getShortURL(String longURL) {
        // TODO Si existe recuperarla, sino crearla?
        String shortURL = generateShortURL();
        redisService.save(getShortURLKey(shortURL), longURL);
        return shortURL;
    }

    private String generateShortURL() {
        int currentId = getNextId();
        return idToShortURL(currentId);
    }

    public String getLongURL(String shortURL) {
        return redisService.get(getShortURLKey(shortURL));
    }

    public void deleteShortURL(String shortURL) {
        redisService.delete(shortURL);
    }

    private String getShortURLKey(String shortURL) {
        return "URL:" + shortURL;
    }

    // TODO lock por concurrencia? Tendría que haber otro nodo (docker) centralizado para manejar la persistencia
    private int getNextId() {
        String currentIdAsString = redisService.get("current-id");
        if(currentIdAsString == null) {
            currentIdAsString = "0";
        }
        int nextId = parseInt(currentIdAsString) + 1;
        redisService.save("current-id", Integer.toString(nextId));
        return nextId;
    }

    // TODO Revisar, devuelve 1 sólo caracter con ids cortos
    private String idToShortURL(int n)
    {
        char[] map = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

        StringBuilder shorturl = new StringBuilder();
        while (n > 0) {
            shorturl.append(map[n % 62]);
            n = n / 62;
        }
        return shorturl.reverse().toString();
    }
}
