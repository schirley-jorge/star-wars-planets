package com.b2w.starwarsplanets.controllers;

import com.b2w.starwarsplanets.common.PlanetSearchType;
import com.b2w.starwarsplanets.exceptions.PlanetAlreadyExistException;
import com.b2w.starwarsplanets.models.Planet;
import com.b2w.starwarsplanets.services.IPlanetService;
import com.b2w.starwarsplanets.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public Planet create(@NonNull @RequestBody final Planet resource) {
        ValidationUtil.checkIsValid(resource);

        try {
            return service.createPlanet(resource);
        } catch (PlanetAlreadyExistException e) {
            log.error(e.getMessage(), e);
            throw e;
        } catch(Exception e) {
            log.error("Error on creating planet", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on creating planet");
        }
    }

    @GetMapping(params = { "page", "size" })
    public Page<Planet> listPlanets(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        return service.listPlanets(page, size);
    }

    @GetMapping(value = "/search")
    public Planet searchPlanet(@RequestParam String searchBy,
                          @RequestParam String value) {
        ValidationUtil.validateSearchByParam(searchBy);

        if (PlanetSearchType.get(searchBy) == PlanetSearchType.BY_ID) {
            return ValidationUtil.checkFound(service.findById(value));
        }

        return ValidationUtil.checkFound(service.findByName(value));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        service.deletePlanet(id);
    }
}
