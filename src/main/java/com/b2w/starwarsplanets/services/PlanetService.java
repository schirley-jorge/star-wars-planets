package com.b2w.starwarsplanets.services;

import com.b2w.starwarsplanets.models.Planet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanetService implements IPlanetService {

    public Planet createPlanet(Planet planet) {
        // verifica se o planet já existe e retorna erro caso positivo

        // vai na API externa pegar a qt filmes

        // salva o planeta no banco

        return planet;
    }

    @Override
    public List<Planet> listPlanets() {
        return new ArrayList<Planet>();
    }

    @Override
    public Planet findById(long id) {
        return null;
    }

    @Override
    public Planet findByName(String name) {
        return null;
    }

    @Override
    public boolean deletePlanet(long id) {
        return false;
    }
}
