package ar.com.rosario.realestate.scraper.adapter;

import ar.com.rosario.realestate.scraper.domain.RawListing;
import ar.com.rosario.realestate.scraper.port.out.ListingSource;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MercadoLibre official API adapter (fase 9).
 * Category MLA1459 = Inmuebles Argentina.
 * Phone/email of listing owners is NEVER stored — Ley 25.326.
 */
public class MercadoLibreAdapter implements ListingSource {

    private static final String SOURCE = "mercadolibre";
    private static final String BASE_URL = "https://api.mercadolibre.com";
    private static final String CATEGORY = "MLA1459";

    private final RestClient restClient;
    private final Cache<String, List<RawListing>> searchCache;

    public MercadoLibreAdapter(String accessToken) {
        this.restClient = RestClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader("Authorization", "Bearer " + accessToken)
            .build();
        this.searchCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(30))
            .maximumSize(50)
            .build();
    }

    @Override
    public String sourceName() { return SOURCE; }

    @Override
    @SuppressWarnings("unchecked")
    public List<RawListing> fetchListings(String locationQuery, int maxResults) {
        String cacheKey = locationQuery + "|" + maxResults;
        return searchCache.get(cacheKey, k -> {
            try {
                Map<String, Object> response = restClient.get()
                    .uri(u -> u.path("/sites/MLA/search")
                        .queryParam("category", CATEGORY)
                        .queryParam("q", locationQuery)
                        .queryParam("limit", Math.min(maxResults, 50))
                        .build())
                    .retrieve()
                    .body(Map.class);

                if (response == null) return List.of();

                List<Map<String, Object>> results =
                    (List<Map<String, Object>>) response.getOrDefault("results", List.of());

                List<RawListing> listings = new ArrayList<>();
                for (Map<String, Object> item : results) {
                    RawListing l = parseItem(item);
                    if (l != null) listings.add(l);
                }
                return listings;
            } catch (Exception e) {
                return List.of();
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public RawListing fetchDetail(String externalId) {
        try {
            Map<String, Object> item = restClient.get()
                .uri("/items/" + externalId)
                .retrieve()
                .body(Map.class);
            return item != null ? parseItem(item) : null;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private RawListing parseItem(Map<String, Object> item) {
        if (item == null) return null;

        String id    = String.valueOf(item.getOrDefault("id", ""));
        String title = String.valueOf(item.getOrDefault("title", ""));
        String url   = String.valueOf(item.getOrDefault("permalink", ""));

        // price
        BigDecimal precio = null;
        Object priceRaw = item.get("price");
        if (priceRaw instanceof Number n) precio = BigDecimal.valueOf(n.doubleValue());

        // currency
        String currency = String.valueOf(item.getOrDefault("currency_id", "USD"));
        if (!"USD".equals(currency)) return null; // skip ARS listings

        // attributes
        List<Map<String, Object>> attrs =
            (List<Map<String, Object>>) item.getOrDefault("attributes", List.of());

        BigDecimal m2Cub  = null, m2Tot = null;
        Integer ambientes = null, dormitorios = null, banos = null, cocheras = null;
        String tipo = "DEPARTAMENTO", operacion = "VENTA";
        Double lat = null, lon = null;

        for (Map<String, Object> attr : attrs) {
            String attrId = String.valueOf(attr.getOrDefault("id", ""));
            Object valStruct = attr.get("value_struct");
            double numVal = 0;
            if (valStruct instanceof Map<?,?> vs) {
                Object num = vs.get("number");
                if (num instanceof Number n) numVal = n.doubleValue();
            }
            String valueName = String.valueOf(attr.getOrDefault("value_name", ""));
            switch (attrId) {
                case "SURFACE_COVERED"   -> m2Cub      = BigDecimal.valueOf(numVal);
                case "SURFACE_TOTAL"     -> m2Tot      = BigDecimal.valueOf(numVal);
                case "ROOMS"             -> ambientes   = (int) numVal;
                case "BEDROOMS"          -> dormitorios = (int) numVal;
                case "BATHROOMS"         -> banos       = (int) numVal;
                case "PARKING_LOTS"      -> cocheras    = (int) numVal;
                case "PROPERTY_TYPE"     -> tipo        = mapTipo(valueName);
                case "OPERATION_SUBTYPE" -> operacion   = mapOperacion(valueName);
                default                  -> { /* ignore other attrs */ }
            }
        }

        // location
        Object locRaw = item.get("location");
        @SuppressWarnings("unchecked")
        Map<String, Object> loc = locRaw instanceof Map<?,?> ? (Map<String, Object>) locRaw : Map.of();
        Object neighRaw = loc.get("neighborhood");
        String barrio = neighRaw instanceof Map<?,?> nb ? String.valueOf(nb.get("name")) : "";
        String direccion = String.valueOf(loc.getOrDefault("address_line", title));
        Object geoRaw = loc.get("geo_coordinates");
        if (geoRaw instanceof Map<?,?> geoMap) {
            Object latRaw = geoMap.get("latitude"), lonRaw = geoMap.get("longitude");
            if (latRaw instanceof Number ln) lat = ln.doubleValue();
            if (lonRaw instanceof Number ln) lon = ln.doubleValue();
        }

        String hash = id + "|" + (precio != null ? precio.toPlainString() : "");

        return new RawListing(
            SOURCE, id, url, title, "",
            direccion, barrio, tipo, operacion,
            precio, m2Cub, m2Tot, ambientes, dormitorios, banos, cocheras,
            lat, lon, hash, Instant.now()
        );
    }

    private String mapTipo(String mlName) {
        return switch (mlName.toLowerCase()) {
            case "departamento"        -> "DEPARTAMENTO";
            case "casa"                -> "CASA";
            case "ph"                  -> "PH";
            case "local comercial"     -> "LOCAL";
            case "cochera"             -> "COCHERA";
            case "terreno"             -> "TERRENO";
            case "oficina"             -> "OFICINA";
            default                    -> "DEPARTAMENTO";
        };
    }

    private String mapOperacion(String mlName) {
        if (mlName.toLowerCase().contains("alquiler")) return "ALQUILER";
        return "VENTA";
    }
}
