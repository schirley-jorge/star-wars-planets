package com.b2w.starwarsplanets.controllers;

import com.b2w.starwarsplanets.models.Planet;
import com.b2w.starwarsplanets.services.IPlanetService;
import com.b2w.starwarsplanets.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    private final IPlanetService service;

    public PlanetController(IPlanetService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Planet create(@RequestBody final Planet resource) {
        ValidationUtil.checkIsValid(resource);
//        ValidationUtil.checkAlreadyExists(service.getByName(resource.getName()));
        try {
            return service.createPlanet(resource);
        } catch(Exception e) { // verifica as exceções enviadas pelo serviço, cada uma
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on creating planet");
        }
    }

    @GetMapping
    public List<Planet> getAllPlanets() {
        // usar paginação
        return service.listPlanets();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable Long id) {
        return service.deletePlanet(id);
    }

}
