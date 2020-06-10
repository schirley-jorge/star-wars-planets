package com.b2w.starwarsplanets.common;

import java.util.HashMap;
import java.util.Map;

public enum PlanetSearchType {
    BY_NAME("name"),
    BY_ID("id");

    private String type;
    private static final Map<String, PlanetSearchType> lookup = new HashMap<String, PlanetSearchType>();

    PlanetSearchType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    static {
        for (PlanetSearchType notification : PlanetSearchType.values()) {
            lookup.put(notification.getType(), notification);
        }
    }

    public static PlanetSearchType get(String type) {
        return lookup.get(type);
    }
}
