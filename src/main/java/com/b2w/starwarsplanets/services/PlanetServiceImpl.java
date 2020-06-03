package com.b2w.starwarsplanets.services;

import com.b2w.starwarsplanets.models.Planet;

import java.util.List;

public class PlanetServiceImpl implements PlanetService {

    public Planet createPlanet(Planet planet) {
        // verifica se o planet já existe e retorna erro caso positivo

        // vai na API externa pegar a qt filmes

        // salva o planeta no banco

        return planet;
    }

    @Override
    public List<Planet> listPlanets() {
        return null;
    }

    @Override
    public Planet findPlanet(long id) {
        return null;
    }

    @Override
    public boolean deletePlanet(long id) {
        return false;
    }
}
