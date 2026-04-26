package ar.com.rosario.realestate.core.domain;

public record Coordenadas(double latitud, double longitud) {

    public Coordenadas {
        if (latitud < -90 || latitud > 90) throw new IllegalArgumentException("latitud fuera de rango: " + latitud);
        if (longitud < -180 || longitud > 180) throw new IllegalArgumentException("longitud fuera de rango: " + longitud);
    }

    /** Haversine distance in kilometres. */
    public double distanciaKm(Coordenadas other) {
        final double R = 6371.0;
        double dLat = Math.toRadians(other.latitud - this.latitud);
        double dLon = Math.toRadians(other.longitud - this.longitud);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                 + Math.cos(Math.toRadians(this.latitud)) * Math.cos(Math.toRadians(other.latitud))
                 * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
