package com.b2w.starwarsplanets.services;

import com.b2w.starwarsplanets.models.Planet;
import com.b2w.starwarsplanets.repositories.PlanetUserMongoRepository;

import com.b2w.starwarsplanets.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlanetService implements IPlanetService {
    @Autowired
    private PlanetUserMongoRepository mongoRepository;

    @Autowired
    private IStarWarsAPIService starWarsAPIService;

    public PlanetService() {

    }

    @Override
    public Planet createPlanet(Planet planet) {
        ValidationUtil.checkAlreadyExist(mongoRepository.findByName(planet.getName()));

        setNumberOfFilms(planet);

        return mongoRepository.save(planet);
    }

    @Override
    public List<Planet> listPlanets() {
        return new ArrayList<Planet>();
    }

    @Nullable @Override
    public Planet findById(String id) {
        Optional<Planet> optional = mongoRepository.findById(id);

        if (optional.isPresent()) {
            Planet planet = optional.get();
            setNumberOfFilms(planet);

            return planet;
        }

        return null;
    }

    @Nullable @Override
    public Planet findByName(String name) {
        Planet planet = mongoRepository.findByName(name);

        if (planet != null) {
            setNumberOfFilms(planet);
        }

        return planet;
    }

    @Override
    public void deletePlanet(String id) {
        mongoRepository.deleteById(id);
    }

    private void setNumberOfFilms(Planet planet) {
        int films = starWarsAPIService.getNumberOfFilms(planet.getName());
        planet.setFilms(films);
    }
}
