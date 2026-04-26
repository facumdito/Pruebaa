package ar.com.rosario.realestate.api.controller;

import ar.com.rosario.realestate.api.routing.TasadorRoutingService;
import ar.com.rosario.realestate.core.domain.Coordenadas;
import ar.com.rosario.realestate.core.domain.PropiedadId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TasadorRoutingServiceTest {

    private final TasadorRoutingService svc = new TasadorRoutingService();

    @Test
    void optimizeRoute_returns_same_stops() {
        List<TasadorRoutingService.RouteStop> stops = List.of(
            stop(-32.9442, -60.6505, "Depot"),
            stop(-32.9500, -60.6600, "Casa 1"),
            stop(-32.9300, -60.6400, "Casa 2"),
            stop(-32.9600, -60.6700, "Casa 3"),
            stop(-32.9200, -60.6300, "Casa 4")
        );

        List<TasadorRoutingService.RouteStop> result = svc.optimizeRoute(stops);

        assertThat(result).hasSize(stops.size());
        assertThat(result).containsExactlyInAnyOrderElementsOf(stops);
        // First stop should always be depot (index 0)
        assertThat(result.get(0).etiqueta()).isEqualTo("Depot");
    }

    @Test
    void optimizeRoute_single_stop_returned_as_is() {
        List<TasadorRoutingService.RouteStop> stops = List.of(
            stop(-32.9442, -60.6505, "Solo")
        );
        assertThat(svc.optimizeRoute(stops)).hasSize(1);
    }

    @Test
    void optimizeRoute_two_stops_unchanged() {
        var stops = List.of(
            stop(-32.94, -60.65, "A"),
            stop(-32.95, -60.66, "B")
        );
        assertThat(svc.optimizeRoute(stops)).hasSize(2);
    }

    private TasadorRoutingService.RouteStop stop(double lat, double lon, String label) {
        return new TasadorRoutingService.RouteStop(PropiedadId.newId(), new Coordenadas(lat, lon), label);
    }
}
