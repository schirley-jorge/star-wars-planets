package com.b2w.starwarsplanets.services;

import com.b2w.starwarsplanets.models.Planet;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@SpringBootTest
public class StarWarsAPIServiceTest {
    @Mock
    private CloseableHttpClient httpClient;

    private IStarWarsAPIService swapiService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        swapiService = new StarWarsAPIService(httpClient);
    }

    @DisplayName("On get number of films")
    @Nested
    class GetNumberOfFims {

        @DisplayName("When status code is error should return null")
        @Test
        public void whenStatusCodeIsErrorShouldReturnNull() throws IOException {
            int statusCodeError = 400;
            Planet planet = new Planet();
            planet.setName("name");

            CloseableHttpResponse responseMock = mock(CloseableHttpResponse.class);
            StatusLine statusLineMock = mock(StatusLine.class);

            when(httpClient.execute(any(HttpGet.class))).thenReturn(responseMock);
            when(responseMock.getStatusLine()).thenReturn(statusLineMock);
            when(statusLineMock.getStatusCode()).thenReturn(statusCodeError);

            Integer films = swapiService.getNumberOfFilms(planet);

            assertNull(films);
            verify(responseMock, never()).getEntity();
        }

        @DisplayName("When response has no entity should return null")
        @Test
        public void whenNoEntityShouldReturnNull() throws IOException {
            int statusCodeOk = 200;
            Planet planet = new Planet();
            planet.setName("name");

            CloseableHttpResponse responseMock = mock(CloseableHttpResponse.class);
            StatusLine statusLineMock = mock(StatusLine.class);

            when(httpClient.execute(any(HttpGet.class))).thenReturn(responseMock);
            when(responseMock.getStatusLine()).thenReturn(statusLineMock);
            when(statusLineMock.getStatusCode()).thenReturn(statusCodeOk);
            when(responseMock.getEntity()).thenReturn(null);

            Integer films = swapiService.getNumberOfFilms(planet);

            assertNull(films);
            verify(responseMock, times(1)).getEntity();
        }

        @DisplayName("Should return number of films properly")
        @Test
        public void shouldReturnNumberOfFims() throws IOException {
            int statusCodeOk = 200;
            Planet planet = new Planet();
            planet.setName("name");

            CloseableHttpResponse responseMock = mock(CloseableHttpResponse.class);
            StatusLine statusLineMock = mock(StatusLine.class);
            HttpEntity entityMock = mock(HttpEntity.class);

            when(httpClient.execute(any(HttpGet.class))).thenReturn(responseMock);
            when(responseMock.getStatusLine()).thenReturn(statusLineMock);
            when(statusLineMock.getStatusCode()).thenReturn(statusCodeOk);
            when(responseMock.getEntity()).thenReturn(entityMock);

            String result = "{\"count\":1,\"results\":[{\"name\":\"name\",\"films\":[\"films/1/\",\"films/6/\"]}]}";
            InputStream stream = new ByteArrayInputStream(result.getBytes());
            when(entityMock.getContent()).thenReturn(stream);

            Integer films = swapiService.getNumberOfFilms(planet);

            assertEquals(2, films);
            verify(responseMock, times(1)).getEntity();
        }
    }
}
