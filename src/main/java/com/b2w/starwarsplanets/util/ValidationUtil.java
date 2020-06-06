package com.b2w.starwarsplanets.util;

import com.b2w.starwarsplanets.exceptions.BadRequestException;
import com.b2w.starwarsplanets.exceptions.PlanetAlreadyExistException;
import com.b2w.starwarsplanets.exceptions.PlanetNotFoundException;
import com.b2w.starwarsplanets.models.Planet;

public class ValidationUtil {
    public static Planet checkFound(Planet resource) {
        if (resource == null) {
            throw new PlanetNotFoundException();
        }
        return resource;
    }

    public static void checkIsValid(Planet resource) {
        if (resource == null || resource.getName() == null || resource.getClimate() == null || resource.getTerrain() == null) {
            throw new BadRequestException();
        }
    }

    public static void checkAlreadyExists(Planet resource) {
        if (resource != null) {
            String message = String.format("Planet [%s] already exist", resource.getName());
            throw new PlanetAlreadyExistException(message);
        }
    }
}
