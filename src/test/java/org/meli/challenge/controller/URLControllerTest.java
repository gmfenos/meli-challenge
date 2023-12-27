package org.meli.challenge.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meli.challenge.service.URLService;
import org.meli.challenge.exception.UnknownURLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class URLControllerTest {
    private final String SHORT_URL_KEY = "XXYYZZ";
    private final String LONG_URL = "https://eletronicos.mercadolivre.com.br/seguranca-casa/#menu=categories";

    private URLService urlServiceMock;
    private URLController controller;

    @BeforeEach
    public void beforeEach() {
        urlServiceMock = mock(URLService.class);
        controller = new URLController(urlServiceMock, mock());
    }

    @Test
    public void shouldGetALongURL() throws UnknownURLException {
        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        when(urlServiceMock.getLongURL(SHORT_URL_KEY)).thenReturn(LONG_URL);

        controller.getLongURL(responseMock, SHORT_URL_KEY);

        verify(responseMock).setStatus(HttpServletResponse.SC_FOUND);
        verify(responseMock).setHeader("Location", LONG_URL);
    }

    @Test
    public void shouldThrowAnExceptionIfShortURLDoesNotExist() {
        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        when(urlServiceMock.getLongURL(SHORT_URL_KEY)).thenReturn(null);

        assertThrows(UnknownURLException.class, () -> {
            controller.getLongURL(responseMock, SHORT_URL_KEY);
        });

        verify(responseMock).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void shouldCreateAShortURL() {
        when(urlServiceMock.getShortURL(LONG_URL)).thenReturn(SHORT_URL_KEY);

        String shortURL = controller.createShortURL(LONG_URL);

        assertThat(shortURL).isEqualTo(SHORT_URL_KEY);
    }

    @Test
    public void shouldDeleteAShortURL() {
        doNothing().when(urlServiceMock).deleteShortURL(SHORT_URL_KEY);

        controller.deleteShortURL(SHORT_URL_KEY);

        verify(urlServiceMock).deleteShortURL(SHORT_URL_KEY);
    }
}
