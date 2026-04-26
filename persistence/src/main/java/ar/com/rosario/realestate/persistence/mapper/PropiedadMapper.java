package ar.com.rosario.realestate.persistence.mapper;

import ar.com.rosario.realestate.core.domain.*;
import ar.com.rosario.realestate.persistence.entity.JpaPropiedad;
import ar.com.rosario.realestate.shared.TenantId;
import org.springframework.stereotype.Component;

@Component
public class PropiedadMapper {

    public JpaPropiedad toJpa(Propiedad p) {
        JpaPropiedad j = new JpaPropiedad();
        j.setId(p.getId().value());
        j.setTenantId(p.getTenantId().value());
        j.setDireccion(p.getDireccion());
        j.setBarrio(p.getBarrio() != null ? p.getBarrio().nombre() : null);
        j.setTipo(p.getTipo());
        j.setOperacion(p.getOperacion());
        j.setEstado(p.getEstado());
        if (p.getCoordenadas() != null) {
            j.setLatitud(p.getCoordenadas().latitud());
            j.setLongitud(p.getCoordenadas().longitud());
        }
        j.setPrecioUsd(p.getPrecioUsd());
        j.setTipoCambioMep(p.getTipoCambioMep());
        j.setFechaCotizacion(p.getFechaCotizacion());
        j.setM2Cubiertos(p.getM2Cubiertos());
        j.setM2Totales(p.getM2Totales());
        j.setAmbientes(p.getAmbientes() > 0 ? p.getAmbientes() : null);
        j.setDormitorios(p.getDormitorios() > 0 ? p.getDormitorios() : null);
        j.setBanos(p.getBanos() > 0 ? p.getBanos() : null);
        j.setCocheras(p.getCocheras() > 0 ? p.getCocheras() : null);
        j.setAntiguedadAnios(p.getAntiguedadAnios() > 0 ? p.getAntiguedadAnios() : null);
        j.setDescripcion(p.getDescripcion());
        j.setVersion(p.getVersion());
        j.setSyncState(p.getSyncState());
        j.setDeleted(p.isDeleted());
        j.setCreatedAt(p.getCreatedAt());
        j.setUpdatedAt(p.getUpdatedAt());
        return j;
    }

    public Propiedad toDomain(JpaPropiedad j) {
        Propiedad p = new Propiedad(
            PropiedadId.of(j.getId()),
            TenantId.of(j.getTenantId()),
            j.getDireccion(),
            j.getTipo(),
            j.getOperacion()
        );
        if (j.getBarrio() != null) p.setBarrio(Barrio.of(j.getBarrio()));
        if (j.getLatitud() != null && j.getLongitud() != null)
            p.setCoordenadas(new Coordenadas(j.getLatitud(), j.getLongitud()));
        p.setM2Cubiertos(j.getM2Cubiertos());
        p.setM2Totales(j.getM2Totales());
        if (j.getAmbientes() != null) p.setAmbientes(j.getAmbientes());
        if (j.getDormitorios() != null) p.setDormitorios(j.getDormitorios());
        if (j.getBanos() != null) p.setBanos(j.getBanos());
        if (j.getCocheras() != null) p.setCocheras(j.getCocheras());
        if (j.getAntiguedadAnios() != null) p.setAntiguedadAnios(j.getAntiguedadAnios());
        p.setDescripcion(j.getDescripcion());
        p.cambiarEstado(j.getEstado());
        if (j.getPrecioUsd() != null)
            p.actualizarPrecio(j.getPrecioUsd(), j.getTipoCambioMep(), j.getFechaCotizacion());
        // restore version/sync/timestamps directly
        p.setVersion(j.getVersion() != null ? j.getVersion() : 0L);
        p.setSyncState(j.getSyncState());
        p.setCreatedAt(j.getCreatedAt());
        p.setUpdatedAt(j.getUpdatedAt());
        return p;
    }
}
