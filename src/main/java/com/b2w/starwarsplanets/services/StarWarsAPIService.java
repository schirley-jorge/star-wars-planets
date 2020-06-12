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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class StarWarsAPIService implements IStarWarsAPIService {

    private static final Logger log = LoggerFactory.getLogger(StarWarsAPIService.class);

    @Value("${swapi.planet.search.url}")
    private String url;

    public StarWarsAPIService() {

    }

    @Cacheable(value = "planetFilms", key = "#planet.id")
    @Override
    public int getNumberOfFilms(Planet planet) {
        return getNumberOfFilmsFromSWAPI(planet.getName());
    }

    private int getNumberOfFilmsFromSWAPI(String planetName) {
        String planet = getPlanetAsString(getEncodedUrl(planetName));

        return getFilmsFromPlanet(planet);
    }

    @Nullable
    private String getPlanetAsString(String url) {
        if (url != null) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(url);
                CloseableHttpResponse response = httpClient.execute(request);

                if (response.getStatusLine().getStatusCode() >= 300) {
                    log.error(String.format("Error: received %d for %s.",
                            response.getStatusLine().getStatusCode(), request.getURI().toString()));
                    return null;
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            } catch (Exception e) {
                log.error("Error to get planet from SWAPI", e);
            }
        }

        return null;
    }

    private int getFilmsFromPlanet(String planetAsString) {
        if (planetAsString != null) {
            try {
                JSONObject planetJson = new JSONObject(planetAsString);
                JSONArray results = planetJson.getJSONArray("results");
                if (results.length() > 0) {
                    JSONArray films = results.getJSONObject(0).getJSONArray("films");
                    return films.length();
                }
            } catch (JSONException e) {
                log.error("Error to parser planet", e);
            }
        }

        return 0;
    }

    @Nullable
    private String getEncodedUrl(String planetName) {
        try {
            String encodedName = URLEncoder.encode(planetName, "UTF-8");
            return String.format(url, encodedName);
        } catch (UnsupportedEncodingException e) {
            log.error(String.format("Error to encode planet name [%s]", planetName), e);
            return null;
        }
    }
}
