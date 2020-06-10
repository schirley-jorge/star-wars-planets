package com.b2w.starwarsplanets.models;

//import javax.validation.constraints.NotNull;
import com.mongodb.lang.Nullable;
import com.mongodb.lang.NonNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "planets")
public class Planet {
    @Id
    private String id;

    @Nullable
    @Indexed(unique = true)
    private String name;

    @NonNull
    private String climate;

    @NonNull
    private String terrain;

    private int films;

    public Planet() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public int getFilms() {
        return films;
    }

    public void setFilms(int films) {
        this.films = films;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planet planet = (Planet) o;
        return id == planet.id &&
                films == planet.films &&
                Objects.equals(name, planet.name) &&
                Objects.equals(climate, planet.climate) &&
                Objects.equals(terrain, planet.terrain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, climate, terrain, films/*, updatedAt*/);
    }

    @Override
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", climate='" + climate + '\'' +
                ", terrain='" + terrain + '\'' +
                ", films=" + films +
                '}';
    }
}
