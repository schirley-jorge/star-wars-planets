package com.b2w.starwarsplanets.controllers;

import com.b2w.starwarsplanets.models.Planet;
import com.b2w.starwarsplanets.services.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody final Planet planet) {
        if (planet.getName() == null || planet.getClimate() == null || planet.getTerrain() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter");
        }
        try {
            Planet planetCreated = planetService.createPlanet(planet);

            return new ResponseEntity<Planet>(planetCreated, HttpStatus.CREATED);
        } catch(Exception e) { // verifica as exceções enviadas pelo serviço, cada uma
            String errorMessage = String.format("Planet [%s] already exist", planet.getName());
            throw new ResponseStatusException(HttpStatus.FOUND, errorMessage);
        }
    }

    @GetMapping
    public ResponseEntity<List<Planet>> getAllPlanets() {
        List<Planet> planets = new ArrayList<Planet>(); // usar paginação

        return new ResponseEntity<List<Planet>>(planets, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        boolean result = planetService.deletePlanet(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

}
