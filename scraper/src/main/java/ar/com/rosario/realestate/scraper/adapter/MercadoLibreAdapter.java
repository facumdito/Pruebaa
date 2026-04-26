package ar.com.rosario.realestate.scraper.adapter;

import ar.com.rosario.realestate.scraper.domain.RawListing;
import ar.com.rosario.realestate.scraper.port.out.ListingSource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * MercadoLibre official API adapter.
 * Phase 9 implementation: uses ML API (api.mercadolibre.com/sites/MLA/search?category=MLA1459)
 * Phone/email of listing owners is NEVER stored (Ley 25.326).
 */
public class MercadoLibreAdapter implements ListingSource {

    private static final String SOURCE = "mercadolibre";

    private final String accessToken;

    public MercadoLibreAdapter(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String sourceName() {
        return SOURCE;
    }

    @Override
    public List<RawListing> fetchListings(String locationQuery, int maxResults) {
        // Phase 9 full implementation:
        // 1. GET api.mercadolibre.com/sites/MLA/search?category=MLA1459&q={locationQuery}&limit={maxResults}
        // 2. Parse JSON, map attributes: SURFACE_COVERED, SURFACE_TOTAL, ROOMS, BEDROOMS, BATHROOMS, PARKING_LOTS
        // 3. Build RawListing — never include seller contact info
        return List.of();
    }

    @Override
    public RawListing fetchDetail(String externalId) {
        // Phase 9 full implementation:
        // GET api.mercadolibre.com/items/{externalId}
        return new RawListing(
            SOURCE, externalId, "https://inmueble.mercadolibre.com.ar/" + externalId,
            "", "", "", "", "DEPARTAMENTO", "VENTA",
            BigDecimal.ZERO, null, null, null, null, null, null, null, null,
            "", Instant.now()
        );
    }
}
