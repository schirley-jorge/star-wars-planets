package com.b2w.starwarsplanets.controllers;

import com.b2w.starwarsplanets.models.Planet;
import com.b2w.starwarsplanets.services.IPlanetService;
import com.b2w.starwarsplanets.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {
    private static final Logger log = LoggerFactory.getLogger(PlanetController.class);

    private final IPlanetService service;

    public PlanetController(IPlanetService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Planet create(@RequestBody final Planet resource) {
        ValidationUtil.checkIsValid(resource);
        ValidationUtil.checkAlreadyExists(service.findByName(resource.getName()));

        try {
            return service.createPlanet(resource);
        } catch(Exception e) { // verifica as exceções enviadas pelo serviço, cada uma
            log.error("Error on creating planet", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on creating planet");
        }
    }

    @GetMapping
    public List<Planet> getAllPlanets() {
        // usar paginação
        return service.listPlanets();
    }

    @GetMapping(value = "/{id}")
    public Planet getById(@PathVariable("id") Long id) {
        return ValidationUtil.checkFound(service.findById(id));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable Long id) {
        return service.deletePlanet(id);
    }
}
