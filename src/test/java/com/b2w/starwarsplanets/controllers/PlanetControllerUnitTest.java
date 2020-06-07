package com.b2w.starwarsplanets.controllers;

import com.b2w.starwarsplanets.models.Planet;
import com.b2w.starwarsplanets.services.IPlanetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class PlanetControllerUnitTest {
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
            mockMvc.perform(post("/api/planets"))
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("when some body attribute is null should throw BadRequestException")
        @Test
        public void whenMissingBodyAttributeShouldThrowBadRequestException() throws Exception {
            mockMvc.perform(post("/api/planets")
                        .content("{\"name\": \"test\", \"terrain\":\"Dessert\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

        }

        @DisplayName("when planet already exists should throw PlanetAlreadyExistException")
        @Test
        public void whenPlanetAlreadyExistShouldThrowPlanetAlreadyExistException() throws Exception {
            Planet planet = new Planet();
            planet.setName("test");

            when(serviceMock.findByName("test")).thenReturn(planet);
            mockMvc.perform(post("/api/planets")
                    .content("{\"name\": \"test\", \"climate\":\"Arid\", \"terrain\":\"Dessert\"}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());
        }

        @DisplayName("should create planet successfully")
        @Test
        public void shouldCreatePlanet() throws Exception {
            Planet resource = new Planet();
            resource.setName("test");
            resource.setClimate("Arid");
            resource.setTerrain("Dessert");

            Planet expectedPlanet = new Planet();
            expectedPlanet.setId("tsgssfsf");
            expectedPlanet.setName(resource.getName());
            expectedPlanet.setClimate(resource.getClimate());
            expectedPlanet.setTerrain(resource.getTerrain());
            expectedPlanet.setFilms(4);

            when(serviceMock.findByName(resource.getName())).thenReturn(null);
            when(serviceMock.createPlanet(resource)).thenReturn(expectedPlanet);

            mockMvc.perform(post("/api/planets")
                    .content("{\"name\": \"test\", \"climate\":\"Arid\", \"terrain\":\"Dessert\"}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(expectedPlanet.getId()))
                    .andExpect(jsonPath("$.name").value(expectedPlanet.getName()));
        }

        @DisplayName("When cannot create planet should return Internal Server Error")
        @Test
        public void onErrorToCreatePlanetShouldReturnInternalServerError() throws Exception {
            Planet resource = new Planet();
            resource.setName("test");
            resource.setClimate("Arid");
            resource.setTerrain("Dessert");

            when(serviceMock.findByName(resource.getName())).thenReturn(null);
            when(serviceMock.createPlanet(resource)).thenThrow(new RuntimeException());

            mockMvc.perform(post("/api/planets")
                    .content("{\"name\": \"test\", \"climate\":\"Arid\", \"terrain\":\"Dessert\"}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }
    }



    @Test
    public void getAllPlanetsSuccessfuly() throws Exception {
        List<Planet> expectedList = new ArrayList<Planet>();
        when(serviceMock.listPlanets()).thenReturn(expectedList);

        mockMvc.perform(get("/api/planets"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(serviceMock, times(1)).listPlanets();
    }

}
