package com.b2w.starwarsplanets.services;

import com.b2w.starwarsplanets.exceptions.PlanetAlreadyExistException;
import com.b2w.starwarsplanets.models.Planet;
import com.b2w.starwarsplanets.repositories.PlanetMongoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PlanetServiceTest {
    @InjectMocks
    private IPlanetService planetService = new PlanetService();

    @Mock
    private PlanetMongoRepository repositoryMock;

    @Mock
    private IStarWarsAPIService swapiServiceMock;

    @DisplayName("On create planet")
    @Nested
    class CreateTest {

        @DisplayName("when planet already exists should throw PlanetAlreadyExistException")
        @Test
        public void whenPlanetAlreadyExistShouldThrowPlanetAlreadyExistException() {
            Planet planet = new Planet();
            planet.setName("test");

            when(repositoryMock.findByName("test")).thenReturn(planet);
            Exception exception = assertThrows(PlanetAlreadyExistException.class, () ->
                    planetService.createPlanet(planet));
        }

        @DisplayName("when planet already exists should throw PlanetAlreadyExistException")
        @Test
        public void whenThereIsNoFilmAttributeFilmsShouldBeNull() {
            Planet resource = new Planet();
            resource.setName("test");
            resource.setClimate("Arid");
            resource.setTerrain("Dessert");

            Planet expectedPlanet = new Planet();
            expectedPlanet.setId("qualquer id");
            expectedPlanet.setName(resource.getName());
            expectedPlanet.setClimate(resource.getClimate());
            expectedPlanet.setTerrain(resource.getTerrain());
            expectedPlanet.setFilms(null);

            when(repositoryMock.findByName("test")).thenReturn(null);
            when(swapiServiceMock.getNumberOfFilms(resource)).thenReturn(null);
            when(repositoryMock.save(resource)).thenReturn(expectedPlanet);

            Planet result = planetService.createPlanet(resource);

            assertNull(result.getFilms());
        }

        @DisplayName("should create planet properly")
        @Test
        public void shouldCreatePlanet() {
            int films = 1;
            String name = "test";

            Planet resource = new Planet();
            resource.setName(name);
            resource.setClimate("Arid");
            resource.setTerrain("Dessert");

            Planet expectedPlanet = new Planet();
            expectedPlanet.setId("qualquer id");
            expectedPlanet.setName(resource.getName());
            expectedPlanet.setClimate(resource.getClimate());
            expectedPlanet.setTerrain(resource.getTerrain());
            expectedPlanet.setFilms(films);

            when(repositoryMock.findByName(name)).thenReturn(null);
            when(swapiServiceMock.getNumberOfFilms(resource)).thenReturn(films);
            when(repositoryMock.save(resource)).thenReturn(expectedPlanet);

            Planet result = planetService.createPlanet(resource);

            assertEquals(result, expectedPlanet);
            verify(repositoryMock, times(1)).save(resource);
        }
    }

    @DisplayName("On list planet ")
    @Nested
    class ListPlanetTest {
        @DisplayName("Should return all planets")
        @Test
        public void shouldReturnAllPlanets() {
            int page = 0;
            int size = 5;

            Planet planet = new Planet();
            planet.setId("id");
            planet.setName("test");

            Page<Planet> pagedResponse = new PageImpl<>(Collections.singletonList(planet));

            when(repositoryMock.findAll(any(Pageable.class))).thenReturn(pagedResponse);
            when(swapiServiceMock.getNumberOfFilms(any(Planet.class))).thenReturn(1);

            Page<Planet> result = planetService.listPlanet(page, size);

            assertEquals(result.getTotalElements(), 1);
            assertEquals(result.getContent().get(0).getName(), planet.getName());

            verify(repositoryMock, times(1)).findAll(any(Pageable.class));
            verify(swapiServiceMock, times(1)).getNumberOfFilms(any(Planet.class));
        }
    }

    @DisplayName("On find planet by id")
    @Nested
    class FindByIdTest {
        @DisplayName("When planet is not found should return null")
        @Test
        public void whenPlanetIsNotFoundShouldReturnNull() {
            String id = "id";
            Optional<Planet> planet = Optional.ofNullable(null);

            when(repositoryMock.findById(id)).thenReturn(planet);

            Planet result = planetService.findById(id);

            assertNull(result);
            verify(swapiServiceMock, never()).getNumberOfFilms(any(Planet.class));
        }

        @DisplayName("When planet is found should return planet")
        @Test
        public void whenPlanetIsFoundShouldReturnPlanet() {
            String id = "id";
            int films = 1;

            Planet planet = new Planet();
            planet.setId(id);
            planet.setName("test");
            planet.setClimate("Arid");
            planet.setTerrain("Dessert");
            planet.setFilms(films);
            Optional<Planet> optionalPlanet = Optional.ofNullable(planet);

            when(repositoryMock.findById(id)).thenReturn(optionalPlanet);
            when(swapiServiceMock.getNumberOfFilms(planet)).thenReturn(films);

            Planet result = planetService.findById(id);

            assertNotNull(result);
            assertEquals(result, planet);
            verify(swapiServiceMock, times(1)).getNumberOfFilms(planet);
        }
    }

    @DisplayName("On find planet by name")
    @Nested
    class FindByNameTest {
        @DisplayName("When planet is not found should return null")
        @Test
        public void whenPlanetIsNotFoundShouldReturnNull() {
            String name = "name";

            when(repositoryMock.findByName(name)).thenReturn(null);

            Planet result = planetService.findByName(name);

            assertNull(result);
            verify(swapiServiceMock, never()).getNumberOfFilms(any(Planet.class));
        }

        @DisplayName("When planet is found should return planet")
        @Test
        public void whenPlanetIsFoundShouldReturnPlanet() {
            String name = "test";
            int films = 1;

            Planet planet = new Planet();
            planet.setId("qualquer");
            planet.setName(name);
            planet.setClimate("Arid");
            planet.setTerrain("Dessert");
            planet.setFilms(films);

            when(repositoryMock.findByName(name)).thenReturn(planet);
            when(swapiServiceMock.getNumberOfFilms(planet)).thenReturn(films);

            Planet result = planetService.findByName(name);

            assertNotNull(result);
            assertEquals(result, planet);
            verify(swapiServiceMock, times(1)).getNumberOfFilms(planet);
        }
    }

    @DisplayName("On delete planet")
    @Nested
    class DeleteTest {
        @DisplayName("Should delete planet from the database")
        @Test
        public void whenRequestDeleteShouldDeletePlanetFromDatabase() throws Exception {
            String id = "teste";

            doAnswer((i) -> {
                assertTrue(id.equals(i.getArgument(0)));
                return null;
            }).when(repositoryMock).deleteById(id);

            planetService.deletePlanet(id);

            verify(repositoryMock, times(1)).deleteById(id);
        }
    }
}
