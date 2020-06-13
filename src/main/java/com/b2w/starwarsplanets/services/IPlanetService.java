package com.b2w.starwarsplanets.services;

import com.b2w.starwarsplanets.models.Planet;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;

import java.util.List;

public interface IPlanetService {
    Planet createPlanet(Planet planet);
    Page<Planet> listPlanet(int page, int size);
    @Nullable
    Planet findById(String id);
    @Nullable
    Planet findByName(String name);
    void deletePlanet(String id);
}
