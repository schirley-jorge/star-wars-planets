package com.b2w.starwarsplanets.controllers;

import com.b2w.starwarsplanets.common.PlanetSearchType;
import com.b2w.starwarsplanets.models.Planet;
import com.b2w.starwarsplanets.services.IPlanetService;
import com.b2w.starwarsplanets.util.ValidationUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Planet create(@RequestBody Planet resource) {
        ValidationUtil.checkIsValid(resource);

        return service.createPlanet(resource);
    }

    @ApiOperation(value = "List all planets in the data base",
            notes = "The result is sorted ascending by name")
    @GetMapping(params = { "page", "size" })
    public Page<Planet> listPlanets(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        return service.listPlanet(page, size);
    }

    @ApiOperation(value = "Search planet by name or id",
                    notes = "searchBy parameter must to be 'id' or 'name'")
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
