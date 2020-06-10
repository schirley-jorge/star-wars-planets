package com.b2w.starwarsplanets.services;

import com.b2w.starwarsplanets.models.Planet;
import org.springframework.lang.Nullable;

import java.util.List;

public interface IPlanetService {
    Planet createPlanet(Planet planet);
    List<Planet> listPlanets();
    @Nullable
    Planet findById(String id);
    @Nullable
    Planet findByName(String name);
    void deletePlanet(String id);
}
