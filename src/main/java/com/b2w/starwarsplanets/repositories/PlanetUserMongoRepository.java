package com.b2w.starwarsplanets.repositories;

import com.b2w.starwarsplanets.models.Planet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "planets", path = "planets")
public interface PlanetUserMongoRepository extends MongoRepository<Planet, String>
{
}
