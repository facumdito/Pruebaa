package ar.com.rosario.realestate.core.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordenadasTest {

    // Rosario centro: -32.9442, -60.6505
    // Buenos Aires (Obelisco): -34.6037, -58.3816
    // Distancia real ~278 km

    @Test
    void distanciaEntreElMismoPuntoEsCero() {
        Coordenadas rosario = new Coordenadas(-32.9442, -60.6505);
        assertEquals(0.0, rosario.distanciaKm(rosario), 0.001);
    }

    @Test
    void distanciaRosarioBuenosAiresAproximada() {
        Coordenadas rosario = new Coordenadas(-32.9442, -60.6505);
        Coordenadas buenosAires = new Coordenadas(-34.6037, -58.3816);
        double km = rosario.distanciaKm(buenosAires);
        // Haversine gives ~278 km, accept ±5 km tolerance
        assertTrue(km > 270 && km < 290,
            "Distancia Rosario-BA esperada ~278km, fue " + km);
    }

    @Test
    void distanciaEsSimetrica() {
        Coordenadas a = new Coordenadas(-32.9442, -60.6505);
        Coordenadas b = new Coordenadas(-33.0, -60.7);
        assertEquals(a.distanciaKm(b), b.distanciaKm(a), 0.001);
    }

    @Test
    void latitudInvalidaNegativaLanza() {
        assertThrows(IllegalArgumentException.class, () -> new Coordenadas(-91, 0));
    }

    @Test
    void latitudInvalidaPositivaLanza() {
        assertThrows(IllegalArgumentException.class, () -> new Coordenadas(91, 0));
    }

    @Test
    void longitudInvalidaLanza() {
        assertThrows(IllegalArgumentException.class, () -> new Coordenadas(0, 181));
        assertThrows(IllegalArgumentException.class, () -> new Coordenadas(0, -181));
    }

    @Test
    void coordenadasLimitesValidasSonAceptadas() {
        assertDoesNotThrow(() -> new Coordenadas(-90, -180));
        assertDoesNotThrow(() -> new Coordenadas(90, 180));
        assertDoesNotThrow(() -> new Coordenadas(0, 0));
    }
}
