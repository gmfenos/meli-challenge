package org.meli.challenge.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.meli.challenge.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class RedisServiceTest {

    private final String KEY = "URL:XXYYZZ";
    private final String VALUE = "https://eletronicos.mercadolivre.com.br/seguranca-casa/#menu=categories";

    private RedisTemplate redisTemplateMock;
    private RedisService service;

    @BeforeEach
    public void beforeEach() {
        redisTemplateMock = mock(RedisTemplate.class, RETURNS_DEEP_STUBS);
        service = new RedisService(redisTemplateMock);
    }

    @Test
    public void shouldSaveAnEntry() {
        ValueOperations valueOperationsMock = mock(ValueOperations.class);
        when(redisTemplateMock.opsForValue()).thenReturn(valueOperationsMock);
        doNothing().when(valueOperationsMock).set(KEY, VALUE);

        service.save(KEY, VALUE);

        verify(valueOperationsMock).set(KEY, VALUE);
    }

    @Test
    public void shouldGetAnEntry() {
        when(redisTemplateMock.opsForValue().get(KEY)).thenReturn(VALUE);

        String valueFound = service.get(KEY);

        assertThat(valueFound).isEqualTo(VALUE);
    }

    @Test
    public void shouldDeleteAnEntry() {
        when(redisTemplateMock.delete(KEY)).thenReturn(true);

        service.delete(KEY);

        verify(redisTemplateMock).delete(KEY);
    }
}
