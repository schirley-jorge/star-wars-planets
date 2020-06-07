package com.b2w.starwarsplanets.models;

//import org.hibernate.annotations.UpdateTimestamp;

//import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Objects;

//@Entity(name = "planets")
public class Planet {
    @Id
    private String id;

//    @NotNull
    private String name;

//    @NotNull
    private String climate;

//    @NotNull
    private String terrain;

    private int films;

//    @Column(name = "updated_at")
//    @UpdateTimestamp
//    private LocalDateTime updatedAt;

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

//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planet planet = (Planet) o;
        return id == planet.id &&
                films == planet.films &&
                Objects.equals(name, planet.name) &&
                Objects.equals(climate, planet.climate) &&
                Objects.equals(terrain, planet.terrain); //&&
//                Objects.equals(updatedAt, planet.updatedAt);
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
//                ", updatedAt=" + updatedAt +
                '}';
    }
}
