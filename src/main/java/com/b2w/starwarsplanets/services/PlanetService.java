package com.b2w.starwarsplanets.services;

import com.b2w.starwarsplanets.models.Planet;

import java.util.List;

public interface PlanetService {
    Planet createPlanet(Planet planet);
    List<Planet> listPlanets();
    Planet findPlanet(long id);
    boolean deletePlanet(long id); // ou retornar um boolean
}
