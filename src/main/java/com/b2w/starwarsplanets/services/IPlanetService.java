package com.b2w.starwarsplanets.services;

import com.b2w.starwarsplanets.models.Planet;

import java.util.List;

public interface IPlanetService {
    Planet createPlanet(Planet planet);
    List<Planet> listPlanets();
    Planet findById(long id);
    Planet findByName(String name);
    boolean deletePlanet(long id);
}
