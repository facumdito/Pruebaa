package ar.com.rosario.realestate.api.dto;

import ar.com.rosario.realestate.core.domain.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record PropiedadDto(
    String id,
    String tenantId,
    String direccion,
    String barrio,
    TipoPropiedad tipo,
    OperacionPropiedad operacion,
    EstadoPropiedad estado,
    Double lat,
    Double lon,
    BigDecimal precioUsd,
    BigDecimal tipoCambioMep,
    LocalDate fechaCotizacion,
    BigDecimal m2Cubiertos,
    BigDecimal m2Totales,
    int ambientes,
    int dormitorios,
    int banos,
    int cocheras,
    int antiguedadAnios,
    String descripcion,
    long version,
    Instant createdAt,
    Instant updatedAt
) {
    public static PropiedadDto from(Propiedad p) {
        return new PropiedadDto(
            p.getId().value(),
            p.getTenantId().value(),
            p.getDireccion(),
            p.getBarrio() != null ? p.getBarrio().nombre() : null,
            p.getTipo(),
            p.getOperacion(),
            p.getEstado(),
            p.getCoordenadas() != null ? p.getCoordenadas().latitud() : null,
            p.getCoordenadas() != null ? p.getCoordenadas().longitud() : null,
            p.getPrecioUsd(),
            p.getTipoCambioMep(),
            p.getFechaCotizacion(),
            p.getM2Cubiertos(),
            p.getM2Totales(),
            p.getAmbientes(),
            p.getDormitorios(),
            p.getBanos(),
            p.getCocheras(),
            p.getAntiguedadAnios(),
            p.getDescripcion(),
            p.getVersion(),
            p.getCreatedAt(),
            p.getUpdatedAt()
        );
    }
}
