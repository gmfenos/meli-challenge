package org.meli.challenge.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meli.challenge.service.RedisService;
import org.meli.challenge.service.URLService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class URLServiceTest {
    private final String SHORT_URL = "b";
    private final String SHORT_URL_KEY_PREFIX = "URL:";
    private final String LONG_URL = "https://eletronicos.mercadolivre.com.br/seguranca-casa/#menu=categories";

    private RedisService redisServiceMock;
    private URLService service;

    @BeforeEach
    public void beforeEach() {
        redisServiceMock = mock(RedisService.class);
        service = new URLService(redisServiceMock);
    }

    @Test
    public void shouldCreateAndReturnTheFirstShortURL() {
        when(redisServiceMock.get("current-id")).thenReturn(null);

        String shortURL = service.getShortURL(LONG_URL);

        assertThat(shortURL).isEqualTo(SHORT_URL);
        verify(redisServiceMock).save("current-id", "1");
        verify(redisServiceMock).save(SHORT_URL_KEY_PREFIX + SHORT_URL, LONG_URL);
    }

    @Test
    public void shouldCreateAndReturnAShortURL() {
        String expectedShortURL = "tm";
        when(redisServiceMock.get("current-id")).thenReturn("999");

        String shortURL = service.getShortURL(LONG_URL);

        assertThat(shortURL).isEqualTo(expectedShortURL);
        verify(redisServiceMock).save("current-id", "1000");
        verify(redisServiceMock).save(SHORT_URL_KEY_PREFIX + expectedShortURL, LONG_URL);
    }

    @Test
    public void shouldGetALongURL() {
        when(redisServiceMock.get(SHORT_URL_KEY_PREFIX + SHORT_URL)).thenReturn(LONG_URL);

        String longURL = service.getLongURL(SHORT_URL);

        assertThat(longURL).isEqualTo(LONG_URL);
    }

    @Test
    public void shouldDeleteAShortURL() {
        doNothing().when(redisServiceMock).delete(SHORT_URL_KEY_PREFIX + SHORT_URL);

        service.deleteShortURL(SHORT_URL);

        verify(redisServiceMock).delete(SHORT_URL_KEY_PREFIX + SHORT_URL);
    }
}
