package com.b2w.starwarsplanets.util;

import com.b2w.starwarsplanets.common.PlanetSearchType;
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
        if ((resource.getName() == null)
                || (resource.getClimate() == null)
                || (resource.getTerrain() == null)) {
            throw new BadRequestException();
        }
    }

    public static void checkAlreadyExist(Planet resource) {
        if (resource != null) {
            throw new PlanetAlreadyExistException();
        }
    }

    public static void validateSearchByParam(String searchBy) {
        PlanetSearchType searchType = PlanetSearchType.get(searchBy);

        if ((searchType != PlanetSearchType.BY_ID) && (searchType != PlanetSearchType.BY_NAME)) {
            throw new BadRequestException();
        }
    }
}
