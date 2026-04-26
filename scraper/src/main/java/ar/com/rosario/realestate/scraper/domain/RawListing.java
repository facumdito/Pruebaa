package ar.com.rosario.realestate.scraper.domain;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Raw listing data before normalization and deduplication.
 * Phone/email of listing owners must NEVER be stored here (Ley 25.326).
 */
public record RawListing(
    String source,
    String externalId,
    String sourceUrl,
    String titulo,
    String descripcion,
    String direccion,
    String barrio,
    String tipo,
    String operacion,
    BigDecimal precioUsd,
    BigDecimal m2Cubiertos,
    BigDecimal m2Totales,
    Integer ambientes,
    Integer dormitorios,
    Integer banos,
    Integer cocheras,
    Double latitud,
    Double longitud,
    String contentHash,
    Instant scrapedAt
) {}
