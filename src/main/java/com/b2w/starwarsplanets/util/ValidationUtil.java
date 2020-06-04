package com.b2w.starwarsplanets.util;

import com.b2w.starwarsplanets.exceptions.BadRequestException;
import com.b2w.starwarsplanets.exceptions.PlanetNotFoundException;
import com.b2w.starwarsplanets.models.Planet;

public class ValidationUtil {
    public static Planet checkNotNull(Planet resource) {
        if (resource == null) {
            throw new PlanetNotFoundException();
        }
        return resource;
    }

    public static Planet checkIsValid(Planet resource) {
        if (resource.getName() == null || resource.getClimate() == null || resource.getTerrain() == null) {
            throw new BadRequestException();
        }
        return resource;
    }

    public static Planet checkAlreadyExists(Planet resource) {
        if (resource != null) {
            throw new PlanetNotFoundException();
        }
        return resource;
    }
}
