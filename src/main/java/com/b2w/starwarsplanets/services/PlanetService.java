package com.b2w.starwarsplanets.services;

import com.b2w.starwarsplanets.models.Planet;
import com.b2w.starwarsplanets.repositories.PlanetMongoRepository;

import com.b2w.starwarsplanets.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanetService implements IPlanetService {
    @Autowired
    private PlanetMongoRepository mongoRepository;

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
    public Page<Planet> listPlanets(int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("name"));
        Page<Planet> result = mongoRepository.findAll(paging);

        return result.map(planet -> setNumberOfFilms(planet));
    }

    @Nullable @Override
    public Planet findById(String id) {
        Optional<Planet> optional = mongoRepository.findById(id);

        if (optional.isPresent()) {
            Planet planet = optional.get();

            return setNumberOfFilms(planet);
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

    @CacheEvict(value = "planetFilms", allEntries=true)
    @Override
    public void deletePlanet(String id) {
        mongoRepository.deleteById(id);
    }

    private Planet setNumberOfFilms(Planet planet) {
        Integer films = starWarsAPIService.getNumberOfFilms(planet);
        planet.setFilms(films);

        return planet;
    }
}
