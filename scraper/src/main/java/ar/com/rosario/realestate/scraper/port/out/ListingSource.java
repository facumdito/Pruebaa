package ar.com.rosario.realestate.scraper.port.out;

import ar.com.rosario.realestate.scraper.domain.RawListing;
import ar.com.rosario.realestate.shared.TenantId;

import java.util.List;

/**
 * Hexagonal port for listing data sources (MercadoLibre API, Argenprop, ZonaProp, etc.).
 */
public interface ListingSource {

    String sourceName();

    List<RawListing> fetchListings(String locationQuery, int maxResults);

    RawListing fetchDetail(String externalId);
}
