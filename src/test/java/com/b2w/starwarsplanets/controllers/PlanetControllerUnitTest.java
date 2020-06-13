package com.b2w.starwarsplanets.controllers;

import com.b2w.starwarsplanets.models.Planet;
import com.b2w.starwarsplanets.services.IPlanetService;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class PlanetControllerUnitTest {
    private final static String URL = "/api/planets";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPlanetService serviceMock;

    @DisplayName("Given a post request")
    @Nested
    class PostTest {

        @DisplayName("when there is no body should throw BadRequestException")
        @Test
        public void whenNoBodyShouldThrowBadRequestException() throws Exception {
            mockMvc.perform(post(URL))
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("when name attribute is null should throw BadRequestException")
        @Test
        public void whenMissingBodyAttributeShouldThrowBadRequestException() throws Exception {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("climate", "Arid");
            jsonObject.put("terrain", "Dessert");

            String myJson = jsonObject.toString();
            mockMvc.perform(post(URL)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

        }

        @DisplayName("should create planet successfully")
        @Test
        public void shouldCreatePlanet() throws Exception {
            Planet resource = new Planet();
            resource.setName("test");
            resource.setClimate("Arid");
            resource.setTerrain("Dessert");

            Planet expectedPlanet = new Planet();
            expectedPlanet.setId("qualquer id");
            expectedPlanet.setName(resource.getName());
            expectedPlanet.setClimate(resource.getClimate());
            expectedPlanet.setTerrain(resource.getTerrain());
            expectedPlanet.setFilms(4);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", resource.getName());
            jsonObject.put("climate", resource.getClimate());
            jsonObject.put("terrain", resource.getTerrain());

            when(serviceMock.createPlanet(resource)).thenReturn(expectedPlanet);

            mockMvc.perform(post(URL)
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value(expectedPlanet.getName()))
                    .andExpect(jsonPath("$.climate").value(expectedPlanet.getClimate()))
                    .andExpect(jsonPath("$.terrain").value(expectedPlanet.getTerrain()));
        }
    }

    @DisplayName("Given a search planet request")
    @Nested
    class SearchTest {

        @DisplayName("when searchBy parameter is invalid should throw BadRequestException")
        @Test
        public void whenInvalidSearchByParamShouldThrowBadRequestException() throws Exception {
            mockMvc.perform(get(URL + "/search?searchBy=invalid&value=nada"))
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("when search by id find the planet should return the planet found")
        @Test
        public void whenSearchByIdFindThePlanetShouldReturnThePlanetFound() throws Exception {
            String id = "some_id";

            Planet expectedPlanet = new Planet();
            expectedPlanet.setId(id);
            expectedPlanet.setName("name");
            expectedPlanet.setClimate("climate");
            expectedPlanet.setTerrain("terrain");
            expectedPlanet.setFilms(4);

            when(serviceMock.findById(id)).thenReturn(expectedPlanet);
            mockMvc.perform(get(URL + "/search?searchBy=id&value=" + id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(expectedPlanet.getId()))
                    .andExpect(jsonPath("$.name").value(expectedPlanet.getName()))
                    .andExpect(jsonPath("$.climate").value(expectedPlanet.getClimate()))
                    .andExpect(jsonPath("$.terrain").value(expectedPlanet.getTerrain()));
        }

        @DisplayName("when search by name find the planet should return the planet found")
        @Test
        public void whenSearchByNameFindThePlanetShouldReturnThePlanetFound() throws Exception {
            String name = "some_id";

            Planet expectedPlanet = new Planet();
            expectedPlanet.setId("id");
            expectedPlanet.setName(name);
            expectedPlanet.setClimate("climate");
            expectedPlanet.setTerrain("terrain");
            expectedPlanet.setFilms(4);

            when(serviceMock.findByName(name)).thenReturn(expectedPlanet);
            mockMvc.perform(get(URL + "/search?searchBy=name&value=" + name))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(expectedPlanet.getId()))
                    .andExpect(jsonPath("$.name").value(expectedPlanet.getName()))
                    .andExpect(jsonPath("$.climate").value(expectedPlanet.getClimate()))
                    .andExpect(jsonPath("$.terrain").value(expectedPlanet.getTerrain()));
        }

        @DisplayName("when search request does not find the planet should throw PlanetNotFoundException")
        @Test
        public void whenSearchDoesNotFindThePlanetShouldthrowPlanetNotFoundException() throws Exception {
            String name = "some_id";

            when(serviceMock.findByName(name)).thenReturn(null);
            mockMvc.perform(get(URL + "/search?searchBy=name&value=" + name))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("Given a list planets request")
    @Nested
    class listPlanetsTest {
        @DisplayName("Should return all planets in the database")
        @Test
        public void whenRequestListPlanetsShouldReturnAllPlanetsFromDatabase() throws Exception {
            int page = 0;
            int size = 5;
            String getUrl = String.format("%s?page=%d&size=%d", URL, page, size);

            Planet planet = new Planet();
            planet.setId("id");
            planet.setName("test");

            Page<Planet> pagedResponse = new PageImpl<>(Collections.singletonList(planet));
            when(serviceMock.listPlanets(page, size)).thenReturn(pagedResponse);
            mockMvc.perform(get(getUrl))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").value(planet.getName()));
        }
    }

    @DisplayName("Given a delete planet request")
    @Nested
    class deletePlanetTest {
        @DisplayName("Should delete the planet from the database")
        @Test
        public void whenRequestDeleteShouldDeletePlanetFromDatabase() throws Exception {
            String id = "teste";
            doAnswer((i) -> {
                assertTrue(id.equals(i.getArgument(0)));
                return null;
            }).when(serviceMock).deletePlanet(anyString());

            mockMvc.perform(delete(URL+ "/" + id))
                .andExpect(status().isOk());

            verify(serviceMock, times(1)).deletePlanet(id);
        }
    }
}
