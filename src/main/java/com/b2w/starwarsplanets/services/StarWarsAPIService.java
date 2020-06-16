package com.b2w.starwarsplanets.services;

import com.b2w.starwarsplanets.models.Planet;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.IllegalFormatException;

@Service
public class StarWarsAPIService implements IStarWarsAPIService {

    private static final Logger log = LoggerFactory.getLogger(StarWarsAPIService.class);

    private final CloseableHttpClient httpClient;

//    @Value("${swapi.planet.search.url}")
    private static final String SWAPI_URL = "https://swapi.dev/api/planets/?search=%s";

    public StarWarsAPIService() {
        this(HttpClients.createDefault());
    }

    public StarWarsAPIService(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Nullable
    @Cacheable(value = "planetFilms", key = "#planet.id", unless="#result == null")
    @Override
    public Integer getNumberOfFilms(Planet planet) {
        JSONObject planetJson = findPlanet(getEncodedUrl(planet.getName()));
        return getFilmsFromPlanet(planetJson, planet.getName());
    }

    @Nullable
    private JSONObject findPlanet(String url) {
        if (url != null) {
            HttpGet request = new HttpGet(url);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() >= 300) {
                    log.error(String.format("Error: received %d for %s.",
                            response.getStatusLine().getStatusCode(), request.getURI().toString()));
                    return null;
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    return new JSONObject(result);
                }
            } catch (Exception e) {
                log.error("Error to get planet from SWAPI", e);
            }
        }

        return null;
    }

    @Nullable
    private Integer getFilmsFromPlanet(JSONObject planetJson, String planetName) {
        if (planetJson != null) {
            try {
                int count = planetJson.getInt("count");
                JSONArray results = planetJson.getJSONArray("results");

                for(int i = 0; i < count; i++) {
                    String name = results.getJSONObject(i).getString("name");

                    if (name.equalsIgnoreCase(planetName)) {
                        JSONArray films = results.getJSONObject(i).getJSONArray("films");
                        return films.length();
                    }
                }
            } catch (JSONException e) {
                log.error("Error to parser planet", e);
            }
        }

        return null;
    }

    @Nullable
    private String getEncodedUrl(String planetName) {
        try {
            String encodedName = URLEncoder.encode(planetName, "UTF-8");

            return String.format(SWAPI_URL, encodedName);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalFormatException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
