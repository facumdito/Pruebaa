package ar.com.rosario.realestate.core.domain;

import java.util.Objects;

public record Barrio(String nombre) {

    public Barrio {
        Objects.requireNonNull(nombre, "nombre must not be null");
        if (nombre.isBlank()) throw new IllegalArgumentException("nombre de barrio must not be blank");
    }

    public static Barrio of(String nombre) {
        return new Barrio(nombre.strip());
    }
}
